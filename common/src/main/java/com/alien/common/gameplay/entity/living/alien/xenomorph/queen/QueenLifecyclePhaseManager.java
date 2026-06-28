package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import com.alien.Alien;
import com.alien.common.gameplay.hive.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive.location.HiveLocationSpacing;
import com.alien.common.registry.tag.AlienBlockTags;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Per-queen front-end life-cycle driver: developing → location → hibernation → founding-handoff (see
 * {@code AVP_Queen_Lifecycle_Design.pdf}, Part 1). Owned by {@link Queen}, ticked server-side from {@link Queen#tick()},
 * and persisted alongside the other queen managers in {@link Queen#addAdditionalSaveData}/{@code readAdditionalSaveData}.
 * <p>
 * <b>Stage 2b scope.</b> {@link QueenLifecyclePhase#DEVELOPING} → {@link QueenLifecyclePhase#LOCATION} →
 * {@link QueenLifecyclePhase#FOUNDING_HANDOFF} are live. On entering LOCATION she commits an anchor — a weighted-Y
 * band pick plus an XZ search that prefers ~16 chunks from existing claims (reusing the exact spacing gate
 * {@code SpreadZoneCheck} enforces, so the spot passes founding). The {@code location_move} GOAP package then clip-digs
 * her there (this manager reconciles her {@code digging} state — noPhysics/noGravity/fire-immunity — each tick); on
 * arrival she carves a small breathable pocket and hands off, founding at the committed anchor. {@code HIBERNATION}
 * arrives in Stage 3, slotting between LOCATION and FOUNDING_HANDOFF (replacing the arrival pocket with the 6x6 clear
 * + 3-day sleep).
 * <p>
 * <b>Single disable point.</b> {@link #isEnabled()} reads the {@code queenFrontEndPhasesEnabled} hive-config flag, so
 * the whole front-end can be toggled at runtime (in-game config inspector / {@code HiveConfigUpdateHandler}) or shipped
 * off by default. When disabled, {@link #isReadyToFound()} returns {@code true} unconditionally and {@link #tick()}
 * no-ops, so the queen founds via the existing path exactly as she does today. No other code changes.
 * <p>
 * <b>Hand-off gate.</b> The only touch-point in the working founding system is
 * {@code HiveManager#tryQueenSettlement}, which early-returns while {@link #isReadyToFound()} is {@code false}. That is
 * what holds a developing/locating/hibernating queen back from settling.
 */
public class QueenLifecyclePhaseManager implements NBTSerializable {

    /** 5 minutes after adulting, per the design doc's developing window. */
    public static final int DEVELOPING_DURATION_TICKS = 5 * 60 * 20;

    /** Sleep length before she wakes and founds: 3 Minecraft days. Zeroed via the hibernation_skip debug command. */
    public static final int HIBERNATION_DURATION_TICKS = 3 * 24000;

    // ---- Weighted target-Y bands (AVP_Queen_Lifecycle_Design.pdf, location phase). Config candidates for later. ----
    private static final int COMMON_Y_MIN = -15;
    private static final int COMMON_Y_MAX = 20;
    private static final int RARE_Y_MIN = -50;
    private static final int RARE_Y_MAX = -25;
    private static final int VERY_RARE_Y_MIN = 25;
    private static final int VERY_RARE_Y_MAX = 45;

    private static final int COMMON_WEIGHT = 70;
    private static final int RARE_WEIGHT = 22;
    private static final int VERY_RARE_WEIGHT = 8;

    /** How far out (in chunks) the anchor search looks for a far-enough spot before giving up and settling in place. */
    private static final int MAX_ANCHOR_SEARCH_RADIUS_CHUNKS = 48;

    /** Arrival radius around the anchor. Must match {@code LocationMoveSensors.AT_ANCHOR_RADIUS}. */
    private static final double ARRIVAL_TOLERANCE = 2.0;

    private static final double ARRIVAL_TOLERANCE_SQ = ARRIVAL_TOLERANCE * ARRIVAL_TOLERANCE;

    /** Anti-suffocation pocket carved on arrival (horizontal radius / height up from the anchor floor). */
    private static final int ARRIVAL_POCKET_RADIUS = 2;

    private static final int ARRIVAL_POCKET_HEIGHT = 5;

    private static final String PHASE_TAG = "lifecyclePhase";

    private static final String DEVELOPING_TICKS_REMAINING_TAG = "lifecycleDevelopingTicksRemaining";

    private static final String ANCHOR_TAG = "lifecycleAnchor";

    private static final String HIBERNATION_TICKS_REMAINING_TAG = "lifecycleHibernationTicksRemaining";

    private final Queen queen;

    private QueenLifecyclePhase phase;

    private int developingTicksRemaining;

    private int hibernationTicksRemaining;

    /** The committed hive anchor (chunk-center XZ + target Y) chosen in LOCATION. Null until then; never re-chosen. */
    private @Nullable BlockPos anchor;

    public QueenLifecyclePhaseManager(Queen queen) {
        this.queen = queen;
        this.phase = QueenLifecyclePhase.DEVELOPING;
        this.developingTicksRemaining = DEVELOPING_DURATION_TICKS;
    }

    /** Whether the front-end phase machine is active. Backed by the {@code queenFrontEndPhasesEnabled} hive-config flag. */
    public static boolean isEnabled() {
        return HiveLocationRegistry.INSTANCE.config().queenFrontEndPhasesEnabled();
    }

    public QueenLifecyclePhase getPhase() {
        return phase;
    }

    /** The committed anchor, or null if she has not reached LOCATION yet. Read by the Stage 2b location_move package. */
    public @Nullable BlockPos getAnchor() {
        return anchor;
    }

    /** Debug read-out: ticks left in the developing window (only meaningful while in DEVELOPING). */
    public int getDevelopingTicksRemaining() {
        return developingTicksRemaining;
    }

    /** Debug read-out: ticks left in the hibernation sleep (only meaningful while in HIBERNATION). */
    public int getHibernationTicksRemaining() {
        return hibernationTicksRemaining;
    }

    /** Debug: zero the sleep timer so a hibernating queen wakes and founds on the next tick. No-op otherwise. */
    public void skipHibernation() {
        if (phase == QueenLifecyclePhase.HIBERNATION) {
            hibernationTicksRemaining = 0;
        }
    }

    /**
     * Whether the queen may hand off to the existing founding system. True only once she has completed the front-end
     * (terminal {@code FOUNDING_HANDOFF}), or always when the machine is disabled (preserving today's behaviour).
     */
    public boolean isReadyToFound() {
        return !isEnabled() || phase == QueenLifecyclePhase.FOUNDING_HANDOFF;
    }

    public void tick() {
        if (queen.level().isClientSide) {
            return;
        }

        // Safety net only: the DIG action owns the digging state (set while it performs, cleared on its finish), so she
        // is noclip *only* while actively digging — never while idle/combat movement is in control. This just clears any
        // lingering flag if she has left LOCATION without the action's finish firing (e.g. an abrupt state change).
        if (queen.isDigging() && phase != QueenLifecyclePhase.LOCATION) {
            queen.setDigging(false);
        }

        if (!isEnabled()) {
            return;
        }

        if (phase == QueenLifecyclePhase.FOUNDING_HANDOFF) {
            return;
        }

        // Migration / already-established guard. A queen restored from a save that predates this system (or any queen
        // that has already founded) has no front-end to run: if she owns a live location or already has an ovipositor,
        // jump straight to the inert terminal state so we never re-run developing on an established queen.
        if (isAlreadyEstablished()) {
            phase = QueenLifecyclePhase.FOUNDING_HANDOFF;
            return;
        }

        // Pause the front-end while she is still inside the molt/growth cocoon. A praetorian/crusher that just
        // metamorphosed exists as a Queen for the tail of the cocoon (destination cocoon + emerge) before her real
        // life begins; her developing clock should not start until the cocoon clears.
        if (queen.getCocoonManager().shouldRunCocoonAction()) {
            return;
        }

        switch (phase) {
            case DEVELOPING -> tickDeveloping();
            case LOCATION -> tickLocation();
            case HIBERNATION -> tickHibernation();
            case FOUNDING_HANDOFF -> { /* inert */ }
        }
    }

    private void tickDeveloping() {
        if (developingTicksRemaining > 0) {
            developingTicksRemaining--;
            return;
        }
        enterLocation();
    }

    /** Reads her situation and commits a hive anchor (chunk-center XZ + weighted target Y), then enters LOCATION. */
    private void enterLocation() {
        var chunk = pickAnchorChunk();
        var targetY = pickTargetY();
        this.anchor = chunk.getMiddleBlockPosition(targetY);
        this.phase = QueenLifecyclePhase.LOCATION;

        Alien.LOGGER.info(
                "Queen lifecycle: {} entering LOCATION — committed anchor chunk {} target Y {} (anchor {})",
                queen.getUUID(),
                chunk,
                targetY,
                anchor
        );
    }

    private void tickLocation() {
        if (anchor == null) {
            // Defensive: a LOCATION-phase queen with no committed anchor (partial/legacy state) recomputes one.
            enterLocation();
            return;
        }

        // The location_move GOAP package clip-digs her toward the anchor; wait until she arrives.
        if (!isAtAnchor()) {
            return;
        }

        // Arrived at depth. Clear a small air pocket (floor kept at the anchor) so she doesn't suffocate, then enter
        // HIBERNATION: she sleeps at the committed anchor for HIBERNATION_DURATION_TICKS and founds on waking.
        clearArrivalPocket();
        enterHibernation();
    }

    /** Begins the sleep at the committed anchor. The hibernation GOAP hold pins her here and runs the sleep animation. */
    private void enterHibernation() {
        this.phase = QueenLifecyclePhase.HIBERNATION;
        this.hibernationTicksRemaining = HIBERNATION_DURATION_TICKS;
        queen.isHibernating.set(true);

        // Settle her onto the pocket floor (anchor Y) instead of wherever she stopped digging. She arrives within the
        // arrival radius — typically a block or two above the anchor — and the hold then zeroes her velocity, which
        // would otherwise pin her floating in the middle of the cleared pocket. Snapping her down puts her on the floor.
        if (anchor != null) {
            queen.setPos(anchor.getX() + 0.5, anchor.getY(), anchor.getZ() + 0.5);
        }

        Alien.LOGGER.info(
                "Queen lifecycle: {} entering HIBERNATION at anchor {} — sleeping {} ticks",
                queen.getUUID(),
                anchor,
                hibernationTicksRemaining
        );
    }

    /** Counts the sleep down; on completion hands off to the founding system (Stage 3a: undisturbed sleep only). */
    private void tickHibernation() {
        // Re-assert the synced flag each tick so a queen restored mid-sleep drives the client hibernate pose too.
        queen.isHibernating.set(true);

        if (hibernationTicksRemaining > 0) {
            hibernationTicksRemaining--;
            return;
        }

        queen.isHibernating.set(false);

        Alien.LOGGER.info(
                "Queen lifecycle: {} woke from HIBERNATION at anchor {} — handing off to founding",
                queen.getUUID(),
                anchor
        );
        phase = QueenLifecyclePhase.FOUNDING_HANDOFF;
    }

    /** Within {@link #ARRIVAL_TOLERANCE} of the anchor (3D). Must match {@code LocationMoveSensors} so GOAP agrees. */
    private boolean isAtAnchor() {
        if (anchor == null) {
            return false;
        }
        var dx = queen.getX() - (anchor.getX() + 0.5);
        var dy = queen.getY() - anchor.getY();
        var dz = queen.getZ() - (anchor.getZ() + 0.5);
        return (dx * dx + dy * dy + dz * dz) <= ARRIVAL_TOLERANCE_SQ;
    }

    /**
     * Whether the dig may pass through this block. True for air/open space, blocks resin can replace
     * ({@link AlienBlockTags#RESIN_REPLACEABLE}), and blocks aliens can break — the latter mirrors
     * {@code Xenomorph.canPathBreakBlock}'s block test (no block entity, breakable, not
     * {@link AlienBlockTags#XENOMORPH_IMMUNE}). The mob-griefing gate is intentionally omitted: this is the queen's
     * lifecycle movement, not block-breaking, and her founding must not depend on that rule. Undiggable blocks
     * (bedrock, obsidian and other immune/reinforced blocks, block-entities) stop her.
     */
    public static boolean isDiggable(Level level, BlockPos pos) {
        var state = level.getBlockState(pos);
        if (state.isAir() || state.is(AlienBlockTags.RESIN_REPLACEABLE)) {
            return true;
        }
        return !state.hasBlockEntity()
                && state.getDestroySpeed(level, pos) >= 0.0F
                && !state.is(AlienBlockTags.XENOMORPH_IMMUNE);
    }

    /**
     * Called by the dig action when an undiggable block blocks her path to the anchor before she reaches it: she can't
     * pass, so she settles where she stands. Retargets the anchor to her current block, which trips the normal arrival
     * flow (pocket-clear + hand-off) on the next tick.
     */
    public void onDigBlocked() {
        if (phase == QueenLifecyclePhase.LOCATION) {
            this.anchor = queen.blockPosition();
        }
    }

    /** Carves a small breathable pocket around the anchor (floor preserved), skipping air and undiggable blocks. */
    private void clearArrivalPocket() {
        if (anchor == null || !(queen.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        for (var dx = -ARRIVAL_POCKET_RADIUS; dx <= ARRIVAL_POCKET_RADIUS; dx++) {
            for (var dz = -ARRIVAL_POCKET_RADIUS; dz <= ARRIVAL_POCKET_RADIUS; dz++) {
                for (var dy = 0; dy < ARRIVAL_POCKET_HEIGHT; dy++) {
                    var pos = anchor.offset(dx, dy, dz);
                    var state = serverLevel.getBlockState(pos);
                    // Nothing to clear for air; never carve undiggable blocks (immune/unbreakable/block-entities).
                    if (state.isAir() || !isDiggable(serverLevel, pos)) {
                        continue;
                    }
                    serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }
    }

    /**
     * Picks the anchor chunk. If her current chunk is already far enough from every existing location she digs in place;
     * otherwise she searches outward for the nearest far-enough chunk (the "prefer to relocate ~16 chunks away" rule),
     * and if boxed in on all sides within the search radius she settles for her current chunk (the "settle for what she
     * can get" rule — that overlap case is a parked design item). The far-enough test is the exact gate
     * {@code SpreadZoneCheck} enforces, so any chunk returned here that isn't the boxed-in fallback will pass founding.
     */
    private ChunkPos pickAnchorChunk() {
        var dimension = queen.level().dimension();
        var minimum = HiveLocationRegistry.INSTANCE.config().minimumHiveLocationDistanceChunks();
        var current = queen.chunkPosition();

        if (HiveLocationSpacing.isFarEnoughFromExistingLocations(dimension, current, minimum)) {
            return current;
        }

        var random = queen.getRandom();
        for (var radius = 1; radius <= MAX_ANCHOR_SEARCH_RADIUS_CHUNKS; radius++) {
            var ring = farEnoughChunksInRing(current, radius, dimension, minimum);
            if (!ring.isEmpty()) {
                return ring.get(random.nextInt(ring.size()));
            }
        }

        return current;
    }

    private static List<ChunkPos> farEnoughChunksInRing(
            ChunkPos center,
            int radius,
            ResourceKey<Level> dimension,
            int minimum
    ) {
        var out = new ArrayList<ChunkPos>();
        for (var dx = -radius; dx <= radius; dx++) {
            for (var dz = -radius; dz <= radius; dz++) {
                if (Math.max(Math.abs(dx), Math.abs(dz)) != radius) {
                    continue; // border of the ring only
                }
                var candidate = new ChunkPos(center.x + dx, center.z + dz);
                if (HiveLocationSpacing.isFarEnoughFromExistingLocations(dimension, candidate, minimum)) {
                    out.add(candidate);
                }
            }
        }
        return out;
    }

    private int pickTargetY() {
        var random = queen.getRandom();
        var roll = random.nextInt(COMMON_WEIGHT + RARE_WEIGHT + VERY_RARE_WEIGHT);
        if (roll < COMMON_WEIGHT) {
            return randomBetween(random, COMMON_Y_MIN, COMMON_Y_MAX);
        } else if (roll < COMMON_WEIGHT + RARE_WEIGHT) {
            return randomBetween(random, RARE_Y_MIN, RARE_Y_MAX);
        }
        return randomBetween(random, VERY_RARE_Y_MIN, VERY_RARE_Y_MAX);
    }

    private static int randomBetween(RandomSource random, int minInclusive, int maxInclusive) {
        return minInclusive + random.nextInt(maxInclusive - minInclusive + 1);
    }

    /**
     * Whether the queen is already past the front-end: she has an ovipositor, or she is the founder of any live
     * location in her dimension. Resolved by founder UUID across the registry, mirroring {@code FoundingMoveSensors}.
     */
    private boolean isAlreadyEstablished() {
        if (queen.hasOvipositor()) {
            return true;
        }

        var dimension = queen.level().dimension();
        var founderId = queen.getUUID();

        for (var location : HiveLocationRegistry.INSTANCE.all()) {
            if (location.isAlive() && founderId.equals(location.founderId()) && location.dimension().equals(dimension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(PHASE_TAG)) {
            this.phase = QueenLifecyclePhase.byNameOrDefault(
                    compoundTag.getString(PHASE_TAG),
                    QueenLifecyclePhase.DEVELOPING
            );
        }

        if (compoundTag.contains(DEVELOPING_TICKS_REMAINING_TAG)) {
            this.developingTicksRemaining = compoundTag.getInt(DEVELOPING_TICKS_REMAINING_TAG);
        }

        this.anchor = compoundTag.contains(ANCHOR_TAG) ? BlockPos.of(compoundTag.getLong(ANCHOR_TAG)) : null;

        if (compoundTag.contains(HIBERNATION_TICKS_REMAINING_TAG)) {
            this.hibernationTicksRemaining = compoundTag.getInt(HIBERNATION_TICKS_REMAINING_TAG);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putString(PHASE_TAG, phase.name());
        compoundTag.putInt(DEVELOPING_TICKS_REMAINING_TAG, developingTicksRemaining);
        compoundTag.putInt(HIBERNATION_TICKS_REMAINING_TAG, hibernationTicksRemaining);
        if (anchor != null) {
            compoundTag.putLong(ANCHOR_TAG, anchor.asLong());
        }
    }
}
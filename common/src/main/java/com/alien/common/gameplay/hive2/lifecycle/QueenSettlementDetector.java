package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Per-queen settlement timer. Tracks how long each queen has been standing in a single chunk without combat. Returns a
 * {@link BlockPos} when {@link com.alien.common.gameplay.hive2.config.HiveConfig#settlementTicks()} have elapsed since
 * the queen first anchored — at which point the caller should evaluate {@link SpreadZoneCheck} and (if permitted) hand
 * the position to {@link HiveLocationFoundingService}.
 * <p>
 * Reset conditions, per {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md} § 2 ("without combat") and
 * {@code HIVE_REDESIGN_03_LOCATIONS.md} § 9:
 * <ul>
 * <li>Queen moves to a different chunk (anchor re-established).</li>
 * <li>Queen has a target (in combat).</li>
 * <li>Queen took damage in the last few ticks ({@code hurtTime > 0}).</li>
 * </ul>
 * <p>
 * State is in-memory only — a server stop or world reload starts every queen fresh. That's the design intent;
 * settlements are rare events (60 seconds each) and persistence isn't worth the complexity.
 */
public final class QueenSettlementDetector {

    private static final Map<UUID, AnchorState> states = new HashMap<>();

    private QueenSettlementDetector() {}

    /**
     * Per-tick observation. Returns the anchor block position when the queen has stood there long enough without combat
     * — typically the chunk's middle block-position. Returns {@code null} otherwise.
     */
    public static @Nullable BlockPos observe(Queen queen, long currentGameTime) {
        var uuid = queen.getUUID();

        if (isInCombat(queen)) {
            states.remove(uuid);
            return null;
        }

        var currentChunk = new ChunkPos(queen.blockPosition());
        var existing = states.get(uuid);

        if (existing == null || !existing.chunk().equals(currentChunk)) {
            // First observation, or queen moved — re-anchor.
            states.put(uuid, new AnchorState(currentChunk, currentGameTime, queen.blockPosition()));
            return null;
        }

        var settlementTicks = HiveLocationRegistry.INSTANCE.config().settlementTicks();
        var elapsed = currentGameTime - existing.startedAtTick();

        if (elapsed < settlementTicks) {
            return null;
        }

        // Settled. Snap the position to the chunk center at the queen's current Y so the location's
        // centerPos is reproducible regardless of where exactly within the chunk she stopped.
        states.remove(uuid);
        return existing.chunk().getMiddleBlockPosition(queen.blockPosition().getY());
    }

    /** Drops the queen's anchor without firing settlement. Use when she dies, despawns, or is otherwise removed. */
    public static void forget(UUID queenId) {
        states.remove(queenId);
    }

    /** For debug commands — read-only snapshot of the current per-queen anchors. */
    public static Map<UUID, AnchorState> snapshot() {
        return Collections.unmodifiableMap(new HashMap<>(states));
    }

    /** Called from server-stop. Prevents per-UUID state from leaking between worlds. */
    public static void clear() {
        states.clear();
    }

    private static boolean isInCombat(Queen queen) {
        if (queen.getTarget() != null) {
            return true;
        }
        if (queen.hurtTime > 0) {
            return true;
        }
        return queen.getLastHurtByMob() != null;
    }

    public record AnchorState(
        ChunkPos chunk,
        long startedAtTick,
        BlockPos lastSeenPos
    ) {}
}

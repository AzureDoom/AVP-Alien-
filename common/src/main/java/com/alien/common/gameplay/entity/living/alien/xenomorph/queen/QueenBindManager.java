package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer 2 — queen-side capture bind state. Tracks up to 8 capture chains (anchor block positions, in attach order)
 * and the bind chunk locked when the first chain attaches. Drives the progressive movement restriction toward that
 * chunk's center; at 4+ chains she is pinned dead-center.
 *
 * <p>This is the source of truth for a queen's restraint; the per-anchor Layer 1 clamp is suppressed for queens (see
 * {@code AnchorBlockEntity.serverTick}). Attach/detach are fired from {@code AnchorBlockEntity.bind/release}, the
 * single choke points every chain attach/release flows through.
 *
 * <p>Slice 1 scope: state, attach/detach, persistence, and the restriction clamp (server-side). Geo reveal, the
 * anchor-to-shackle render, break-on-attack, AI suppression at 4 chains, and founding suppression are later slices;
 * the queries here ({@link #chainCount()}, {@link #isFullyBound()}, {@link #anchors()}) are the hooks they will read.
 */
public class QueenBindManager {

    /** Maximum chains on a queen: 4 to fully bind, plus 4 for extra securement. */
    public static final int MAX_CHAINS = 8;

    private static final String TAG_ANCHORS = "BindAnchors";
    private static final String TAG_BIND_CHUNK = "BindChunk";

    private final Queen queen;

    /** Anchor block positions in attach order. Index doubles as the shackle slot for later geo/render slices. */
    private final List<BlockPos> anchors = new ArrayList<>();

    /** Chunk locked when the first chain attaches; all restriction is measured from its center. Null when unbound. */
    private ChunkPos bindChunk;

    public QueenBindManager(Queen queen) {
        this.queen = queen;
    }

    // ---- queries (hooks for later slices) ----

    public int chainCount() {
        return anchors.size();
    }

    public boolean hasAnyChain() {
        return !anchors.isEmpty();
    }

    /** Fully restrained: 4+ chains, pinned at chunk center. (AI suppression that stops her fighting is a later slice.) */
    public boolean isFullyBound() {
        return anchors.size() >= 4;
    }

    /** Live view of the bound anchors, attach-ordered. */
    public List<BlockPos> anchors() {
        return anchors;
    }

    // ---- attach / detach ----

    /** Register a chain from {@code anchorPos}. The first chain locks the bind chunk to the queen's current chunk. */
    public void attach(BlockPos anchorPos) {
        if (anchors.size() >= MAX_CHAINS || anchors.contains(anchorPos)) {
            return;
        }
        if (anchors.isEmpty()) {
            bindChunk = new ChunkPos(queen.blockPosition());
        }
        anchors.add(anchorPos.immutable());
    }

    /** Drop the chain from {@code anchorPos}. Clearing the last chain releases the bind chunk. */
    public void detach(BlockPos anchorPos) {
        anchors.remove(anchorPos);
        if (anchors.isEmpty()) {
            bindChunk = null;
        }
    }

    // ---- tick: restriction clamp (server-side) ----

    public void tick() {
        if (anchors.isEmpty()) {
            bindChunk = null;
            return;
        }
        if (bindChunk == null || queen.level().isClientSide()) {
            return;
        }
        applyRestrictionClamp();
    }

    /** Horizontal tether radius around the bind chunk's center, by current chain count. */
    private double tetherRadius() {
        return switch (anchors.size()) {
            case 1 -> 16.0;
            case 2 -> 10.0;
            case 3 -> 5.0;
            default -> 0.0; // 4+ chains: locked at center
        };
    }

    private void applyRestrictionClamp() {
        double radius = tetherRadius();
        double centerX = bindChunk.getMiddleBlockX() + 0.5;
        double centerZ = bindChunk.getMiddleBlockZ() + 0.5;
        double dx = queen.getX() - centerX;
        double dz = queen.getZ() - centerZ;
        double distSq = dx * dx + dz * dz;

        if (radius <= 0.0) {
            // Fully bound: pin to chunk center, preserving Y so she stays grounded.
            if (distSq > 1.0e-6) {
                queen.setPos(centerX, queen.getY(), centerZ);
                Vec3 v = queen.getDeltaMovement();
                queen.setDeltaMovement(0.0, v.y, 0.0);
                queen.hurtMarked = true;
            }
            return;
        }

        if (distSq > radius * radius) {
            double dist = Math.sqrt(distSq);
            double nx = dx / dist;
            double nz = dz / dist;
            queen.setPos(centerX + nx * radius, queen.getY(), centerZ + nz * radius);

            Vec3 v = queen.getDeltaMovement();
            double outward = v.x * nx + v.z * nz;
            if (outward > 0.0) {
                queen.setDeltaMovement(v.x - nx * outward, v.y, v.z - nz * outward);
            }
            queen.hurtMarked = true;
        }
    }

    // ---- persistence ----

    public void load(CompoundTag tag) {
        anchors.clear();
        bindChunk = null;
        if (tag.contains(TAG_ANCHORS)) {
            for (long packed : tag.getLongArray(TAG_ANCHORS)) {
                anchors.add(BlockPos.of(packed));
            }
        }
        if (tag.contains(TAG_BIND_CHUNK)) {
            bindChunk = new ChunkPos(tag.getLong(TAG_BIND_CHUNK));
        }
    }

    public void save(CompoundTag tag) {
        if (anchors.isEmpty()) {
            return;
        }
        long[] packed = new long[anchors.size()];
        for (int i = 0; i < anchors.size(); i++) {
            packed[i] = anchors.get(i).asLong();
        }
        tag.putLongArray(TAG_ANCHORS, packed);
        if (bindChunk != null) {
            tag.putLong(TAG_BIND_CHUNK, bindChunk.toLong());
        }
    }
}
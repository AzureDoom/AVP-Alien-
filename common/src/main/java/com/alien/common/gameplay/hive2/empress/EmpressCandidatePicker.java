package com.alien.common.gameplay.hive2.empress;

import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.UUID;

/**
 * Picks which queen of a lineage should become its empress, deterministically. Per
 * {@code HIVE_REDESIGN_07_LEADERSHIP.md} § 6.1, comparators apply in order:
 * <ol>
 * <li>Older queen wins (compare entity {@code tickCount}).</li>
 * <li>Queen at the older location wins (compare seat location's {@code ageInTicks}).</li>
 * <li>Founder tiebreak — if the lineage's {@code founderId} matches a still-tied queen, she wins.</li>
 * <li>UUID lex compare — last-resort total order so the result is stable across restarts.</li>
 * </ol>
 * <p>
 * Returns {@code null} when no loaded queens exist in any of the lineage's locations. Phase 10 calls this from
 * {@link EmpressEmergenceTask}.
 */
public final class EmpressCandidatePicker {

    private EmpressCandidatePicker() {}

    public static @Nullable Queen pick(MinecraftServer server, LineageFactionData lineage) {
        var serverLevel = server.getLevel(lineage.dimension());
        if (serverLevel == null) {
            return null;
        }

        Queen best = null;
        Comparator<Queen> tieBreak = compareCandidates(lineage);

        for (var location : lineage.locationsById().values()) {
            for (var entry : location.loadedMembersByType().entrySet()) {
                if (!entry.getKey().is(AlienEntityTypeTags.QUEENS)) {
                    continue;
                }
                for (var uuid : entry.getValue()) {
                    var entity = serverLevel.getEntity(uuid);
                    if (!(entity instanceof Queen queen) || !queen.isAlive()) {
                        continue;
                    }
                    if (best == null || tieBreak.compare(queen, best) > 0) {
                        best = queen;
                    }
                }
            }
        }

        return best;
    }

    /**
     * Returns a comparator where higher = preferred. Walks the comparator chain until one differentiates.
     */
    private static Comparator<Queen> compareCandidates(LineageFactionData lineage) {
        return (a, b) -> {
            // 1. Older queen wins.
            var tickCmp = Integer.compare(a.tickCount, b.tickCount);
            if (tickCmp != 0) {
                return tickCmp;
            }

            // 2. Queen at older location wins (location ageInTicks).
            var seatA = locationAge(lineage, a.blockPosition());
            var seatB = locationAge(lineage, b.blockPosition());
            var seatCmp = Long.compare(seatA, seatB);
            if (seatCmp != 0) {
                return seatCmp;
            }

            // 3. Founder match wins.
            var founderId = lineage.founderId();
            if (founderId != null) {
                if (founderId.equals(a.getUUID()) && !founderId.equals(b.getUUID())) {
                    return 1;
                }
                if (founderId.equals(b.getUUID()) && !founderId.equals(a.getUUID())) {
                    return -1;
                }
            }

            // 4. UUID lex compare — lex-greater wins so smaller UUIDs lose. Stable across restarts.
            return uuidLex(a.getUUID(), b.getUUID());
        };
    }

    /** Find the location whose claimed chunks contain {@code pos}, return its ageInTicks. -1 if none. */
    private static long locationAge(LineageFactionData lineage, net.minecraft.core.BlockPos pos) {
        var chunk = new net.minecraft.world.level.ChunkPos(pos);
        for (var location : lineage.locationsById().values()) {
            if (location.claimedChunks().contains(chunk)) {
                return location.ageInTicks();
            }
        }
        return -1L;
    }

    private static int uuidLex(UUID a, UUID b) {
        var hi = Long.compare(a.getMostSignificantBits(), b.getMostSignificantBits());
        if (hi != 0) {
            return hi;
        }
        return Long.compare(a.getLeastSignificantBits(), b.getLeastSignificantBits());
    }
}

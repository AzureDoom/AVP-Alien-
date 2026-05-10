package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.LineageRemovalReason;
import com.alien.common.gameplay.hive2.faction.VariantFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

/**
 * Lineage-tier death: a lineage with zero locations for {@code LINEAGE_DECAY_TICKS} (default 1 hour) gets removed.
 * <p>
 * Per {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md} § 4. The grace period gives surviving members time to refound a
 * location before the lineage formally dies — so a lineage that just lost its last location stays in a "zero-locations"
 * pending state rather than being killed instantly.
 * <p>
 * On death:
 * <ul>
 * <li>Lineage pool overflows into the dimension's variant pool (with default-uncapped variant pool, no actual
 * loss).</li>
 * <li>Members (including any empress) lose lineage membership when BLib removes the faction. They become foragers.</li>
 * <li>{@link LineageRemovalReason.NoLocationsRemain} is recorded.</li>
 * <li>BLib {@code factions().remove(lineageId)} fires; the {@code BLibFactionRemoveEvent} listener (Phase 11) handles
 * defensive cleanup.</li>
 * </ul>
 */
public final class LineageDeathHandler {

    private LineageDeathHandler() {}

    /**
     * Walk every lineage; advance the 0-locations grace period; kill any whose grace has elapsed. Called from
     * {@link com.alien.common.gameplay.hive2.faction.LineageInvariantTask} on the slow scan cadence.
     */
    public static void scanAndKill(MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();
        var config = HiveLocationRegistry.INSTANCE.config();

        var deathQueue = new ArrayList<ResourceLocation>();

        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }

            var locationCount = lineage.locationsById().size();

            if (locationCount > 0) {
                // Lineage has locations — clear any pending grace timestamp (a refound rescued it).
                if (lineage.zeroLocationsSinceTick() != Long.MIN_VALUE) {
                    lineage.setZeroLocationsSinceTick(Long.MIN_VALUE);
                }
                continue;
            }

            // Zero locations.
            if (lineage.zeroLocationsSinceTick() == Long.MIN_VALUE) {
                lineage.setZeroLocationsSinceTick(currentTick);
                Alien.LOGGER.info(
                    "Hive2: lineage {} dropped to 0 locations; grace period until tick {}",
                    factionId,
                    currentTick + config.lineageDecayTicks()
                );
                continue;
            }

            var elapsed = currentTick - lineage.zeroLocationsSinceTick();
            if (elapsed >= config.lineageDecayTicks()) {
                deathQueue.add(factionId);
            }
        }

        for (var lineageId : deathQueue) {
            kill(lineageId);
        }
    }

    /**
     * Forces lineage death now. Drains the pool into the variant pool, sets removal reason, removes from BLib.
     * Idempotent — a lineage already dead returns false.
     */
    public static boolean kill(ResourceLocation lineageId) {
        var faction = Alien.MOD.factions().get(lineageId);
        if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
            return false;
        }

        // Drain lineage pool → variant pool with cap-aware overflow (per HIVE_REDESIGN_05_RESERVES.md § 6).
        drainPoolToVariantPool(lineage);

        lineage.setRemovalReason(new LineageRemovalReason.NoLocationsRemain());
        Alien.MOD.factions().remove(lineageId);

        Alien.LOGGER.info(
            "Hive2: lineage {} dead (no locations remain); pool drained to variant pool, faction removed",
            lineageId
        );
        return true;
    }

    private static void drainPoolToVariantPool(LineageFactionData lineage) {
        var variantId = lineage.parentVariantFactionId();
        if (variantId == null) {
            Alien.LOGGER.warn(
                "Hive2: lineage death drain — lineage has no parent variant id; pool of {} entries lost",
                lineage.lineagePool().getCount()
            );
            return;
        }

        var variantFaction = Alien.MOD.factions().get(variantId);
        if (variantFaction == null || !(variantFaction.data() instanceof VariantFactionData variantData)) {
            Alien.LOGGER.warn(
                "Hive2: lineage death drain — variant faction {} missing or wrong type; pool lost",
                variantId
            );
            return;
        }

        var pool = lineage.lineagePool();
        for (var type : new ArrayList<>(pool.getAvailableEntityTypes())) {
            var count = pool.getCount(type);
            if (count <= 0) {
                continue;
            }
            variantData.tryAddToVariantPool(lineage.dimension(), type, count);
            pool.add(type, -count);
        }
    }

}

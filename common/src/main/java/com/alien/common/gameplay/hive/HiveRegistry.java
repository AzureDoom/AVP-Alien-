package com.alien.common.gameplay.hive;

import com.alien.Alien;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienFactionDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class HiveRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(HiveRegistry.class);

    public static final HiveRegistry INSTANCE = new HiveRegistry();

    private final Map<ResourceLocation, Hive> hives;

    private HiveRegistry() {
        this.hives = new HashMap<>();
    }

    public Hive createHive(
        MinecraftServer server,
        BlockPos centerPos,
        ResourceKey<Level> dimension,
        AlienVariant variant
    ) {
        var factionId = HiveIds.create();
        var result = Alien.MOD.factions().getOrCreate(factionId, AlienFactionDataTypes.HIVE);

        if (result.isErr()) {
            throw new IllegalStateException("Failed to create hive faction: " + result.unwrapErr());
        }

        var tuple = result.unwrap();
        var relationships = tuple.v1();
        var factionData = tuple.v2();

        factionData.setCenterPos(centerPos);
        factionData.setDimension(dimension);
        factionData.setVariant(variant);

        var hive = new Hive(server, factionId, relationships, factionData);
        hives.put(factionId, hive);

        LOGGER.info("Created hive {} at {} in {}", factionId, centerPos, dimension.location());

        return hive;
    }

    public void removeHive(ResourceLocation factionId) {
        hives.remove(factionId);
        Alien.MOD.factions().remove(factionId);
    }

    public @Nullable Hive findNearestHive(
        BlockPos pos,
        ResourceKey<Level> dimension,
        Predicate<Hive> predicate
    ) {
        Hive nearest = null;
        var nearestDistanceSqr = Double.MAX_VALUE;

        for (var hive : hives.values()) {
            if (!hive.getFactionData().getDimension().equals(dimension)) {
                continue;
            }

            if (!predicate.test(hive)) {
                continue;
            }

            var distanceSqr = hive.getFactionData().getCenterPos().distSqr(pos);

            if (distanceSqr < nearestDistanceSqr) {
                nearestDistanceSqr = distanceSqr;
                nearest = hive;
            }
        }

        return nearest;
    }

    public @Nullable Hive findNearestHive(BlockPos pos, ResourceKey<Level> dimension) {
        return findNearestHive(pos, dimension, $ -> true);
    }

    public @Nullable Hive getHive(ResourceLocation factionId) {
        return hives.get(factionId);
    }

    public Collection<Hive> allHives() {
        return Collections.unmodifiableCollection(hives.values());
    }

    public void tick(MinecraftServer server) {
        var hivesToRemove = new ArrayList<ResourceLocation>();

        for (var hive : hives.values()) {
            hive.tick();

            if (!hive.isAlive()) {
                hivesToRemove.add(hive.getFactionId());
            }
        }

        for (var factionId : hivesToRemove) {
            var hive = hives.get(factionId);

            if (hive != null) {
                hive.onRemove();
            }

            removeHive(factionId);
        }
    }

    public void onServerStarted(MinecraftServer server) {
        hives.clear();

        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (!HiveIds.isHiveId(factionId)) {
                continue;
            }

            var dataResult = Alien.MOD.factions().getData(factionId, AlienFactionDataTypes.HIVE);

            if (dataResult.isErr()) {
                LOGGER.warn("Failed to load hive faction data for {}: {}", factionId, dataResult.unwrapErr());
                continue;
            }

            var relationships = Alien.MOD.factions().getRelationships(factionId);
            var factionData = dataResult.unwrap();
            var hive = new Hive(server, factionId, relationships, factionData);

            hives.put(factionId, hive);
        }

        LOGGER.info("Loaded {} hives from faction data", hives.size());
    }

    public void onServerStopped(MinecraftServer server) {
        hives.clear();
    }

    public void onFactionRemoved(ResourceLocation factionId) {
        hives.remove(factionId);
    }
}

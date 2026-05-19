package com.alien.fabric.data.reinforcement_profile;

import com.alien.AlienResources;
import com.alien.common.data.ReinforcementProfileReloadListener;
import com.alien.common.gameplay.hive.convoy.RaidWaveProfile;
import com.alien.common.gameplay.hive.convoy.ReinforcementProfile;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.ReinforcementProfileRegistry;
import com.alien.common.registry.init.AlienEntityTypes;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ReinforcementProfileDataProvider implements DataProvider {

    private static final int UNLIMITED = Integer.MAX_VALUE;

    private final FabricDataOutput output;

    private final Map<ResourceLocation, ReinforcementProfile> profilesById = new LinkedHashMap<>();

    public ReinforcementProfileDataProvider(FabricDataOutput output) {
        this.output = output;
    }

    private void generate() {
        profilesById.put(AlienResources.location("default"), ReinforcementProfile.fallback());
        profilesById.put(
            ReinforcementProfileRegistry.profileIdFor(AlienVariant.NORMAL),
            profile(
                new VariantReinforcementEntities(
                    AlienEntityTypes.RUNNER.get(),
                    AlienEntityTypes.DRONE.get(),
                    AlienEntityTypes.WARRIOR.get(),
                    AlienEntityTypes.PROWLER.get(),
                    AlienEntityTypes.CHRYSALIS.get(),
                    AlienEntityTypes.RAZOR_CLAW.get(),
                    AlienEntityTypes.BURSTER.get(),
                    AlienEntityTypes.RAVAGER.get(),
                    AlienEntityTypes.CARRIER.get(),
                    AlienEntityTypes.PRAETORIAN.get(),
                    AlienEntityTypes.CRUSHER.get()
                )
            )
        );
        profilesById.put(
            ReinforcementProfileRegistry.profileIdFor(AlienVariant.NETHER),
            profile(
                new VariantReinforcementEntities(
                    AlienEntityTypes.NETHER_RUNNER.get(),
                    AlienEntityTypes.NETHER_DRONE.get(),
                    AlienEntityTypes.NETHER_WARRIOR.get(),
                    AlienEntityTypes.NETHER_PROWLER.get(),
                    AlienEntityTypes.NETHER_CHRYSALIS.get(),
                    AlienEntityTypes.NETHER_RAZOR_CLAW.get(),
                    AlienEntityTypes.NETHER_BURSTER.get(),
                    AlienEntityTypes.NETHER_RAVAGER.get(),
                    AlienEntityTypes.NETHER_CARRIER.get(),
                    AlienEntityTypes.NETHER_PRAETORIAN.get(),
                    AlienEntityTypes.NETHER_CRUSHER.get()
                )
            )
        );
        profilesById.put(
            ReinforcementProfileRegistry.profileIdFor(AlienVariant.ABERRANT),
            profile(
                new VariantReinforcementEntities(
                    AlienEntityTypes.ABERRANT_RUNNER.get(),
                    AlienEntityTypes.ABERRANT_DRONE.get(),
                    AlienEntityTypes.ABERRANT_WARRIOR.get(),
                    AlienEntityTypes.ABERRANT_PROWLER.get(),
                    AlienEntityTypes.ABERRANT_CHRYSALIS.get(),
                    AlienEntityTypes.ABERRANT_RAZOR_CLAW.get(),
                    AlienEntityTypes.ABERRANT_BURSTER.get(),
                    AlienEntityTypes.ABERRANT_RAVAGER.get(),
                    AlienEntityTypes.ABERRANT_CARRIER.get(),
                    AlienEntityTypes.ABERRANT_PRAETORIAN.get(),
                    AlienEntityTypes.ABERRANT_CRUSHER.get()
                )
            )
        );
    }

    private static ReinforcementProfile profile(VariantReinforcementEntities entities) {
        return new ReinforcementProfile(
            List.of(
                pool(entities.runner(), 3),
                pool(entities.drone(), 3),
                pool(entities.warrior(), 2),
                pool(entities.prowler(), 2),
                pool(entities.chrysalis(), 1, 2),
                pool(entities.razorClaw(), 1, 2),
                pool(entities.burster(), 1, 3),
                pool(entities.ravager(), 1, 1),
                pool(entities.carrier(), 1, 1),
                pool(entities.praetorian(), 1, 1),
                pool(entities.crusher(), 1, 1)
            )
        );
    }

    private static RaidWaveProfile.PoolEntry pool(EntityType<?> entityType, int weight) {
        return pool(entityType, weight, UNLIMITED);
    }

    private static RaidWaveProfile.PoolEntry pool(EntityType<?> entityType, int weight, int maxCount) {
        return RaidWaveProfile.PoolEntry.entityPool(entityType, weight, maxCount);
    }

    @Override
    public final @NotNull CompletableFuture<?> run(CachedOutput cached) {
        generate();

        var pathProvider = output.createPathProvider(
            PackOutput.Target.DATA_PACK,
            ReinforcementProfileReloadListener.DIRECTORY_NAME
        );
        var futures = new ArrayList<CompletableFuture<?>>();

        for (var entry : profilesById.entrySet()) {
            var filePath = pathProvider.json(entry.getKey());
            var jsonElement = ReinforcementProfile.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
            futures.add(DataProvider.saveStable(cached, jsonElement, filePath));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public final @NotNull String getName() {
        return "Reinforcement Profiles";
    }

    private record VariantReinforcementEntities(
        EntityType<?> runner,
        EntityType<?> drone,
        EntityType<?> warrior,
        EntityType<?> prowler,
        EntityType<?> chrysalis,
        EntityType<?> razorClaw,
        EntityType<?> burster,
        EntityType<?> ravager,
        EntityType<?> carrier,
        EntityType<?> praetorian,
        EntityType<?> crusher
    ) {}
}

package com.alien.common.registry;

import com.alien.common.model.lifecycle.growth.GrowthRequirement;
import com.alien.common.model.lifecycle.growth.GrowthStage;
import com.alien.common.model.lifecycle.growth.GrowthStageKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrowthStageRegistry {

    private static final List<GrowthStage> GROWTH_STAGES = new ArrayList<>();

    private static final Map<GrowthStageKey, List<GrowthStage>> GROWTH_STAGE_KEY_TO_GROWTH_STAGES = new HashMap<>();

    public static List<GrowthStage> getCandidates(@Nullable EntityType<?> host, EntityType<?> currentForm) {
        var directMappings = GROWTH_STAGE_KEY_TO_GROWTH_STAGES.get(new GrowthStageKey(host, currentForm));

        if (directMappings != null && !directMappings.isEmpty()) {
            return directMappings;
        }

        var genericMappings = GROWTH_STAGE_KEY_TO_GROWTH_STAGES.get(new GrowthStageKey(null, currentForm));

        return genericMappings != null ? genericMappings : List.of();
    }

    public static void clear() {
        GROWTH_STAGES.clear();
    }

    public static void register(GrowthStage growthStage) {
        GROWTH_STAGES.add(growthStage);
    }

    public static void rebuildLookupMappings() {
        GROWTH_STAGE_KEY_TO_GROWTH_STAGES.clear();
        GROWTH_STAGES.forEach(GrowthStageRegistry::compute);
        GROWTH_STAGE_KEY_TO_GROWTH_STAGES.values().forEach(list -> list.sort(MORE_SPECIFIC_FIRST));
    }

    private static final Comparator<GrowthStage> MORE_SPECIFIC_FIRST = (a, b) -> {
        var specificityA = getMaxAmplifier(a);
        var specificityB = getMaxAmplifier(b);

        return Integer.compare(specificityB, specificityA);
    };

    private static int getMaxAmplifier(GrowthStage stage) {
        var max = -1;

        for (var requirement : stage.requirements()) {
            if (requirement instanceof GrowthRequirement.MobEffectRequirement effectRequirement) {
                max = Math.max(max, effectRequirement.minAmplifier());
            }
        }

        return max;
    }

    private static void compute(GrowthStage growthStage) {
        var hostTypePredicate = growthStage.hostTypePredicate().orElse(null);

        if (hostTypePredicate == null) {
            var lookupKey = new GrowthStageKey(null, growthStage.from());
            GROWTH_STAGE_KEY_TO_GROWTH_STAGES.computeIfAbsent(lookupKey, $ -> new ArrayList<>()).add(growthStage);
        } else {
            BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(hostTypePredicate::test)
                .forEach(entityType -> {
                    var lookupKey = new GrowthStageKey(entityType, growthStage.from());
                    GROWTH_STAGE_KEY_TO_GROWTH_STAGES.computeIfAbsent(lookupKey, $ -> new ArrayList<>()).add(growthStage);
                });
        }
    }
}

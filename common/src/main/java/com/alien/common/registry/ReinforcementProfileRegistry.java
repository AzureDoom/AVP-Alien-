package com.alien.common.registry;

import com.alien.AlienResources;
import com.alien.common.gameplay.hive2.convoy.ReinforcementProfile;
import com.alien.common.model.alien.variant.AlienVariant;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class ReinforcementProfileRegistry {

    public static final ResourceLocation DEFAULT_ID = AlienResources.location("default");

    private static final Map<ResourceLocation, ReinforcementProfile> PROFILES = new LinkedHashMap<>();

    private ReinforcementProfileRegistry() {}

    public static void clear() {
        PROFILES.clear();
    }

    public static void register(ResourceLocation id, ReinforcementProfile profile) {
        PROFILES.put(id, profile);
    }

    public static ReinforcementProfile active() {
        var profile = PROFILES.get(DEFAULT_ID);
        if (profile != null) {
            return profile;
        }
        return ReinforcementProfile.fallback();
    }

    public static ReinforcementProfile forVariant(AlienVariant variant) {
        var profile = PROFILES.get(profileIdFor(variant));
        if (profile != null) {
            return profile;
        }
        return active();
    }

    public static ResourceLocation profileIdFor(AlienVariant variant) {
        return AlienResources.location(variant.name().toLowerCase(Locale.ROOT));
    }

    public static @Nullable ReinforcementProfile get(ResourceLocation id) {
        return PROFILES.get(id);
    }

    public static Map<ResourceLocation, ReinforcementProfile> all() {
        return Collections.unmodifiableMap(PROFILES);
    }
}

package com.alien.common.registry;

import com.alien.AlienResources;
import com.alien.common.gameplay.hive2.convoy.RaidWaveProfile;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RaidWaveProfileRegistry {

    public static final ResourceLocation DEFAULT_ID = AlienResources.location("default");

    private static final Map<ResourceLocation, RaidWaveProfile> PROFILES = new LinkedHashMap<>();

    private RaidWaveProfileRegistry() {}

    public static void clear() {
        PROFILES.clear();
    }

    public static void register(ResourceLocation id, RaidWaveProfile profile) {
        PROFILES.put(id, profile);
    }

    public static RaidWaveProfile active() {
        var profile = PROFILES.get(DEFAULT_ID);
        if (profile != null) {
            return profile;
        }
        if (!PROFILES.isEmpty()) {
            return PROFILES.values().iterator().next();
        }
        return RaidWaveProfile.fallback();
    }

    public static @Nullable RaidWaveProfile get(ResourceLocation id) {
        return PROFILES.get(id);
    }

    public static Map<ResourceLocation, RaidWaveProfile> all() {
        return Collections.unmodifiableMap(PROFILES);
    }
}

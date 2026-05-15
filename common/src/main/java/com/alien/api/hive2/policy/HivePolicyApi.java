package com.alien.api.hive2.policy;

import com.alien.common.gameplay.hive2.policy.HivePolicyResolver;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

/**
 * Public entry point for other mods to adjust hive behavior without replacing AVP Alien internals.
 */
public final class HivePolicyApi {

    private HivePolicyApi() {}

    /**
     * Registers a policy modifier. Modifiers run after AVP Alien's config-backed defaults and may override individual
     * policy values for the supplied context.
     */
    public static void registerModifier(ResourceLocation id, HivePolicyModifier modifier) {
        HivePolicyResolver.registerModifier(Objects.requireNonNull(id, "id"), Objects.requireNonNull(modifier, "modifier"));
    }

    public static boolean unregisterModifier(ResourceLocation id) {
        return HivePolicyResolver.unregisterModifier(Objects.requireNonNull(id, "id"));
    }
}

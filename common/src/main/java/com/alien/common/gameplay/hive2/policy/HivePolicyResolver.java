package com.alien.common.gameplay.hive2.policy;

import com.alien.Alien;
import com.alien.api.hive2.policy.HivePolicyContext;
import com.alien.api.hive2.policy.HivePolicyKey;
import com.alien.api.hive2.policy.HivePolicyKeys;
import com.alien.api.hive2.policy.HivePolicyModifier;
import com.alien.api.hive2.policy.MutableHivePolicy;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class HivePolicyResolver {

    private static final Map<ResourceLocation, HivePolicyModifier> MODIFIERS = new LinkedHashMap<>();

    private HivePolicyResolver() {}

    public static synchronized void registerModifier(ResourceLocation id, HivePolicyModifier modifier) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(modifier, "modifier");

        if (MODIFIERS.containsKey(id)) {
            throw new IllegalArgumentException("Hive policy modifier already registered: " + id);
        }

        MODIFIERS.put(id, modifier);
    }

    public static synchronized boolean unregisterModifier(ResourceLocation id) {
        Objects.requireNonNull(id, "id");
        return MODIFIERS.remove(id) != null;
    }

    public static MutableHivePolicy resolve(HivePolicyContext context) {
        Objects.requireNonNull(context, "context");

        var policy = new ResolvedHivePolicy();
        policy.set(HivePolicyKeys.RESERVE_SPAWNS_CAN_IGNORE_RESIN, context.config().reserveSpawnsCanIgnoreResin());

        for (var entry : modifiersSnapshot().entrySet()) {
            try {
                entry.getValue().modify(context, policy);
            } catch (RuntimeException exception) {
                Alien.LOGGER.error("Hive policy modifier {} failed", entry.getKey(), exception);
            }
        }

        return policy;
    }

    private static synchronized Map<ResourceLocation, HivePolicyModifier> modifiersSnapshot() {
        return new LinkedHashMap<>(MODIFIERS);
    }

    private static final class ResolvedHivePolicy implements MutableHivePolicy {

        private final Map<HivePolicyKey<?>, Object> values = new HashMap<>();

        @Override
        public <T> T get(HivePolicyKey<T> key) {
            Objects.requireNonNull(key, "key");
            var value = values.get(key);
            if (value == null) {
                throw new IllegalArgumentException("Hive policy value is not set: " + key.id());
            }
            return key.cast(value);
        }

        @Override
        public <T> void set(HivePolicyKey<T> key, T value) {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");
            values.put(key, key.type().cast(value));
        }
    }
}

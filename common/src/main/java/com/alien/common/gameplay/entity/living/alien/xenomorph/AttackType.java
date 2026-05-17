package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.just.codec.stream.StreamCodec;
import com.just.codec.stream.impl.StreamCodecs;
import com.just.codec.stream.schema.StreamCodecSchema;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

// TODO(refactor): the static REGISTRY is keyed by id only — multiple xenomorphs that declare attacks with
// the same id (e.g. "claw") overwrite each other. The codec round-trip resolves to whichever instance
// registered last, which works only because all instances of a given id currently agree on their fields.
// A proper fix is per-xenomorph namespacing or a global registry that rejects conflicts.
public record AttackType(
    String id,
    int defaultDurationInTicks,
    float damageThresholdPercent,
    int weight,
    int cooldownInTicks,
    @Nullable Supplier<SoundEvent> sound,
    DamageApplicator damageApplicator,
    Supplier<? extends AttackExecutor> executorFactory,
    Predicate<Xenomorph> activationCondition
) {

    private static final Map<String, AttackType> REGISTRY = new ConcurrentHashMap<>();

    public static final AttackType NONE = builder("none")
        .defaultDurationInTicks(0)
        .damageApplicator(DamageApplicator.NOOP)
        .build();

    public AttackType {
        REGISTRY.put(id, this);
    }

    public static AttackType byId(String id) {
        return REGISTRY.getOrDefault(id, NONE);
    }

    public boolean isNone() {
        return this == NONE;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static final class Builder {

        private final String id;

        private int defaultDurationInTicks = 10;

        private float damageThresholdPercent = 0.5F;

        private int weight = 1;

        private int cooldownInTicks = 0;

        private @Nullable Supplier<SoundEvent> sound = null;

        private DamageApplicator damageApplicator = DamageApplicator.DEFAULT;

        private Supplier<? extends AttackExecutor> executorFactory = AttackExecutor.DEFAULT_FACTORY;

        private Predicate<Xenomorph> activationCondition = xenomorph -> true;

        private Builder(String id) {
            this.id = id;
        }

        public Builder defaultDurationInTicks(int defaultDurationInTicks) {
            this.defaultDurationInTicks = defaultDurationInTicks;
            return this;
        }

        public Builder damageThresholdPercent(float damageThresholdPercent) {
            this.damageThresholdPercent = damageThresholdPercent;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder cooldownInTicks(int cooldownInTicks) {
            this.cooldownInTicks = cooldownInTicks;
            return this;
        }

        public Builder sound(Supplier<SoundEvent> sound) {
            this.sound = sound;
            return this;
        }

        public Builder damageApplicator(DamageApplicator damageApplicator) {
            this.damageApplicator = damageApplicator;
            return this;
        }

        public Builder executorFactory(Supplier<? extends AttackExecutor> executorFactory) {
            this.executorFactory = executorFactory;
            return this;
        }

        public Builder activationCondition(Predicate<Xenomorph> activationCondition) {
            this.activationCondition = activationCondition;
            return this;
        }

        public AttackType build() {
            return new AttackType(
                id,
                defaultDurationInTicks,
                damageThresholdPercent,
                weight,
                cooldownInTicks,
                sound,
                damageApplicator,
                executorFactory,
                activationCondition
            );
        }
    }

    public static final StreamCodec<AttackType> CODEC = new StreamCodec<>() {

        @Override
        public <T> @NotNull AttackType decode(@NotNull StreamCodecSchema<T> schema, @NotNull T input) {
            return byId(StreamCodecs.STRING_UTF8.decode(schema, input));
        }

        @Override
        public <T> void encode(@NotNull StreamCodecSchema<T> schema, @NotNull T input, @NotNull AttackType value) {
            StreamCodecs.STRING_UTF8.encode(schema, input, value.id());
        }
    };
}

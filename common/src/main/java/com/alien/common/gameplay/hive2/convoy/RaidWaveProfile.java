package com.alien.common.gameplay.hive2.convoy;

import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Optional;

public record RaidWaveProfile(List<Wave> waves) {

    public static final int MIN_WAVE_SIZE = 5;

    public static final Codec<RaidWaveProfile> CODEC = RecordCodecBuilder.<RaidWaveProfile>create(
        instance -> instance.group(
            Wave.CODEC.listOf().fieldOf("waves").forGetter(RaidWaveProfile::waves)
        ).apply(instance, RaidWaveProfile::new)
    ).flatXmap(RaidWaveProfile::validate, RaidWaveProfile::validate);

    public RaidWaveProfile {
        waves = List.copyOf(waves);
    }

    public static RaidWaveProfile fallback() {
        return new RaidWaveProfile(
            List.of(
                new Wave(
                    5,
                    List.of(
                        PoolEntry.tagPool(AlienEntityTypeTags.WARRIORS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.PROWLERS, 2, Integer.MAX_VALUE)
                    )
                ),
                new Wave(
                    8,
                    List.of(
                        PoolEntry.tagPool(AlienEntityTypeTags.WARRIORS, 4, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.PROWLERS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.CHRYSALISES, 1, 1),
                        PoolEntry.tagPool(AlienEntityTypeTags.RAZOR_CLAWS, 1, 1)
                    )
                ),
                new Wave(
                    13,
                    List.of(
                        PoolEntry.tagPool(AlienEntityTypeTags.WARRIORS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.PROWLERS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.CHRYSALISES, 2, 3),
                        PoolEntry.tagPool(AlienEntityTypeTags.RAZOR_CLAWS, 2, 3),
                        PoolEntry.tagPool(AlienEntityTypeTags.BURSTERS, 2, 4)
                    )
                ),
                new Wave(
                    21,
                    List.of(
                        PoolEntry.tagPool(AlienEntityTypeTags.WARRIORS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.PROWLERS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.CHRYSALISES, 2, 4),
                        PoolEntry.tagPool(AlienEntityTypeTags.RAZOR_CLAWS, 2, 4),
                        PoolEntry.tagPool(AlienEntityTypeTags.BURSTERS, 2, 5),
                        PoolEntry.tagPool(AlienEntityTypeTags.RAVAGERS, 1, 3),
                        PoolEntry.tagPool(AlienEntityTypeTags.CARRIERS, 1, 3)
                    )
                ),
                new Wave(
                    34,
                    List.of(
                        PoolEntry.tagPool(AlienEntityTypeTags.WARRIORS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.PROWLERS, 3, Integer.MAX_VALUE),
                        PoolEntry.tagPool(AlienEntityTypeTags.CHRYSALISES, 2, 6),
                        PoolEntry.tagPool(AlienEntityTypeTags.RAZOR_CLAWS, 2, 6),
                        PoolEntry.tagPool(AlienEntityTypeTags.BURSTERS, 2, 8),
                        PoolEntry.tagPool(AlienEntityTypeTags.RAVAGERS, 1, 4),
                        PoolEntry.tagPool(AlienEntityTypeTags.CARRIERS, 1, 4),
                        PoolEntry.tagPool(AlienEntityTypeTags.HARBINGERS, 1, 1)
                    )
                )
            )
        );
    }

    public Wave wave(int index) {
        return waves.get(Math.clamp(index, 0, waves.size() - 1));
    }

    public int totalSize() {
        var total = 0;
        for (var wave : waves) {
            total += wave.size();
        }
        return total;
    }

    public int nonHarbingerSize() {
        return Math.max(0, totalSize() - 1);
    }

    private static DataResult<RaidWaveProfile> validate(RaidWaveProfile profile) {
        if (profile.waves().size() != Convoy.Raid.WAVE_COUNT) {
            return DataResult.error(
                () -> "Raid wave profile must define exactly " + Convoy.Raid.WAVE_COUNT + " waves"
            );
        }
        for (var i = 0; i < profile.waves().size(); i++) {
            var wave = profile.waves().get(i);
            var waveNumber = i + 1;
            if (wave.size() < MIN_WAVE_SIZE) {
                return DataResult.error(
                    () -> "Raid wave " + waveNumber + " must have at least " + MIN_WAVE_SIZE + " members"
                );
            }
            if (wave.pools().isEmpty()) {
                return DataResult.error(() -> "Raid wave " + waveNumber + " must define at least one pool");
            }
        }
        return DataResult.success(profile);
    }

    public record Wave(int size, List<PoolEntry> pools) {

        public static final Codec<Wave> CODEC = RecordCodecBuilder.<Wave>create(
            instance -> instance.group(
                Codec.INT.fieldOf("size").forGetter(Wave::size),
                PoolEntry.CODEC.listOf().fieldOf("pools").forGetter(Wave::pools)
            ).apply(instance, Wave::new)
        );

        public Wave {
            pools = List.copyOf(pools);
        }
    }

    public record PoolEntry(
        Optional<EntityType<?>> entity,
        Optional<ResourceLocation> tag,
        int weight,
        int maxCount
    ) {

        public static final Codec<PoolEntry> CODEC = RecordCodecBuilder.<PoolEntry>create(
            instance -> instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().optionalFieldOf("entity").forGetter(PoolEntry::entity),
                ResourceLocation.CODEC.optionalFieldOf("tag").forGetter(entry -> entry.tag()),
                Codec.INT.optionalFieldOf("weight", 1).forGetter(PoolEntry::weight),
                Codec.INT.optionalFieldOf("max_count", Integer.MAX_VALUE).forGetter(PoolEntry::maxCount)
            ).apply(instance, PoolEntry::new)
        ).flatXmap(PoolEntry::validate, PoolEntry::validate);

        public static PoolEntry tagPool(TagKey<EntityType<?>> tag, int weight, int maxCount) {
            return new PoolEntry(Optional.empty(), Optional.of(tag.location()), weight, maxCount);
        }

        public boolean matches(EntityType<?> entityType) {
            if (entity.isPresent() && entity.get() == entityType) {
                return true;
            }
            return tag.isPresent() && entityType.is(TagKey.create(Registries.ENTITY_TYPE, tag.get()));
        }

        private static DataResult<PoolEntry> validate(PoolEntry entry) {
            if (entry.entity().isEmpty() == entry.tag().isEmpty()) {
                return DataResult.error(() -> "Raid wave pool entry must define exactly one of entity or tag");
            }
            if (entry.weight() <= 0) {
                return DataResult.error(() -> "Raid wave pool entry weight must be positive");
            }
            if (entry.maxCount() <= 0) {
                return DataResult.error(() -> "Raid wave pool entry max_count must be positive");
            }
            return DataResult.success(entry);
        }
    }
}

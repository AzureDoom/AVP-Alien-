package com.alien.common.gameplay.entity.living.alien;

import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.Boiler;
import com.alien.common.model.lifecycle.growth.GrowthStage;
import com.alien.common.registry.GrowthStageRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.compatibility.avp_human.AVPHuman;
import com.alien.compatibility.avp_human.GeneManagerProxy;
import com.blib.common.gameplay.model.NBTSerializable;
import com.blib.common.util.EntityTransitionUtil;
import com.human.common.gameplay.gene.GeneOperationType;
import com.human.common.gameplay.gene.Genes;
import com.human.common.model.GeneCarrier;
import com.human.common.util.GeneIntegrityUtil;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class GrowthManager implements NBTSerializable {

    private static final String GROWTH_TIME_IN_TICKS_TAG_KEY = "growthTimeInTicks";

    private static final Set<String> TRANSITION_NBT_KEY_BLACKLIST = Util.make(() -> {
        var set = new HashSet<>(EntityTransitionUtil.DEFAULT_NBT_KEY_BLACKLIST);
        set.add(GROWTH_TIME_IN_TICKS_TAG_KEY);
        return set;
    });

    private final Alien entity;

    private final @Nullable Consumer<Entity> onGrowUpCallback;

    private boolean growOverTime;

    private int growthTimeInTicks;

    private int growthRetryTimeInTicks = 0;

    private boolean readyToGrow;

    public GrowthManager(Alien entity) {
        this(entity, null);
    }

    public GrowthManager(Alien entity, @Nullable Consumer<Entity> onGrowUpCallback) {
        this.entity = entity;
        this.onGrowUpCallback = onGrowUpCallback;
        this.growOverTime = true;
        this.readyToGrow = false;
    }

    public void tick() {
        if (
            entity.level().isClientSide
                || canNeverGrow()
        ) {
            return;
        }

        var growthStage = getNextGrowthStage();

        if (growthStage == null) {
            return;
        }

        var canBypassGrowthTime = entity.getMaxJellyToGrowth() != null && entity.getJellyCount() >= entity.getMaxJellyToGrowth();

        if (canBypassGrowthTime) {
            // If we can bypass growing over time thanks to royal jelly, then do so.
            this.readyToGrow = true;
        } else if (growOverTime) {
            // Otherwise if we can't bypass growth time, tick the entity's growth progress.
            growOverTime();
        }

        if (!readyToGrow) {
            // If the entity isn't ready to grow, then don't continue any further.
            return;
        }

        this.growthRetryTimeInTicks = Math.max(growthRetryTimeInTicks - 1, 0);

        if (growthRetryTimeInTicks > 0) {
            return;
        }

        // Growth attempts can fail for a lot of reasons. This switch covers every possible reason.
        switch (grow()) {
            case GrowthResult.AlreadyFullyGrown $ -> {/* NO-OP */}
            case GrowthResult.CanNotGrow $ -> {/* NO-OP */}
            case GrowthResult.Success $ -> {/* NO-OP */}
            case GrowthResult.FailedTransitionResult failedTransitionResult -> {
                switch (failedTransitionResult.result) {
                    case EntityTransitionUtil.EntityTransitionResult.ClientSide $1 -> {/* NO-OP */}
                    case EntityTransitionUtil.EntityTransitionResult.EntityCreation $1 -> {/* NO-OP */}
                    case EntityTransitionUtil.EntityTransitionResult.Obstructed $1 ->
                        // If the entity failed to grow, then retry in 10 seconds.
                        // TODO: Add particles here maybe if the alien can't grow up, to indicate "frustration"?
                        // Apply a buffer time period before we retry growing.
                        this.growthRetryTimeInTicks = 20 * 10;
                    case EntityTransitionUtil.EntityTransitionResult.Success<?> $1 -> {/* NO-OP */ }
                }
            }
        }
    }

    private @Nullable GrowthStage getNextGrowthStage() {
        var hostType = entity.getHostType().unwrapOr(null);
        return GrowthStageRegistry.getOrNull(hostType, entity.getType());
    }

    private void growOverTime() {
        this.growthTimeInTicks++;

        var growthStage = getNextGrowthStage();

        if (growthStage == null) {
            return;
        }

        var requiredGrowthTimeInTicks = growthStage.growthTimeInTicks();
        var growthTimeReductionMultiplier = 1F;

        if (growthTimeInTicks < requiredGrowthTimeInTicks * growthTimeReductionMultiplier) {
            return;
        }

        this.readyToGrow = true;
    }

    public GrowthResult grow() {
        // Reset growth time at this point.
        this.growthTimeInTicks = 0;
        var growthStage = getNextGrowthStage();

        if (growthStage == null) {
            return GrowthResult.AlreadyFullyGrown.INSTANCE;
        } else if (canNeverGrow()) {
            return GrowthResult.CanNotGrow.INSTANCE;
        }

        var nextFormType = growthStage.to();

        var canBecomeBoiler = canBecomeBoiler(nextFormType);

        Entity nextForm;

        if (canBecomeBoiler) {
            nextFormType = Boiler.getType(entity.getVariant());
        }

        var transitionResult = EntityTransitionUtil.transitionInto(entity, nextFormType, TRANSITION_NBT_KEY_BLACKLIST);

        nextForm = switch (transitionResult) {
            case EntityTransitionUtil.EntityTransitionResult.ClientSide ignored -> null;
            case EntityTransitionUtil.EntityTransitionResult.EntityCreation ignored -> null;
            case EntityTransitionUtil.EntityTransitionResult.Obstructed ignored -> null;
            case EntityTransitionUtil.EntityTransitionResult.Success<?> success -> success.newEntity();
        };

        if (nextForm == null) {
            return new GrowthResult.FailedTransitionResult(transitionResult);
        }

        if (nextForm instanceof Alien alien) {
            var jellyCountToSubtract = entity.getMaxJellyToGrowth() == null
                ? 0
                : entity.getMaxJellyToGrowth();

            alien.setJellyCount(entity.getJellyCount() - jellyCountToSubtract);
        }

        if (onGrowUpCallback != null) {
            onGrowUpCallback.accept(nextForm);
        }

        return new GrowthResult.Success(nextForm);
    }

    private boolean canNeverGrow() {
        return entity.isPoisoned()
            || entity.isIrradiated();
    }

    private boolean canBecomeBoiler(EntityType<?> nextFormType) {
        if (!isProperTransition(nextFormType)) {
            return false;
        }

        return shouldBecomeBoilerFromGeneDecay() || shouldBecomeBoilerFromAcidVolatility();
    }

    private boolean isProperTransition(EntityType<?> nextFormType) {
        var isCurrentlyAdolescent = entity.getType().is(AlienEntityTypeTags.ADOLESCENTS);
        var willGrowIntoAdult = nextFormType.is(AlienEntityTypeTags.XENOMORPHS);

        return isCurrentlyAdolescent
            && willGrowIntoAdult;
    }

    private boolean shouldBecomeBoilerFromAcidVolatility() {
        return switch (entity.getGeneManager()) {
            case GeneManagerProxy.EMPTY ignored -> false;
            case GeneManagerProxy.Wrapper wrapper -> {
                var geneContainer = wrapper.geneManager().getGeneContainer();
                var additiveAcidVolatility = geneContainer.getActiveGeneMap()
                    .getValue(Genes.ACID_VOLATILITY, GeneOperationType.ADDITIVE);
                var multiplicativeAcidVolatility = geneContainer.getActiveGeneMap()
                    .getValue(Genes.ACID_VOLATILITY, GeneOperationType.MULTIPLICATIVE);

                var totalAcidVolatility = additiveAcidVolatility + multiplicativeAcidVolatility;

                yield entity.getRandom().nextDouble() < totalAcidVolatility;
            }
        };
    }

    private boolean shouldBecomeBoilerFromGeneDecay() {
        if (!AVPHuman.MOD.isLoaded()) {
            return false;
        }

        var geneCarrier = (GeneCarrier) entity;
        var geneDecayLevel = GeneIntegrityUtil.getGeneDecayLevel(geneCarrier);

        return switch (geneDecayLevel) {
            case FATAL -> true;
            case STABLE, UNSTABLE -> false;
            case VOLATILE -> {
                // Ex. -2.75 -> 2.75
                var totalGeneIntegrity = Math.abs(GeneIntegrityUtil.getTotalGeneticIntegrity(geneCarrier));
                // Ex. 2.75 - 2 = 0.75
                var chance = totalGeneIntegrity - Math.floor(totalGeneIntegrity);
                // Ex. 0.75 means 75% chance to be a boiler.
                yield entity.getRandom().nextDouble() < chance;
            }
        };
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(GROWTH_TIME_IN_TICKS_TAG_KEY)) {
            this.growthTimeInTicks = compoundTag.getInt(GROWTH_TIME_IN_TICKS_TAG_KEY);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(GROWTH_TIME_IN_TICKS_TAG_KEY, growthTimeInTicks);
    }

    public GrowthManager setGrowOverTime(boolean growOverTime) {
        this.growOverTime = growOverTime;
        return this;
    }

    public sealed interface GrowthResult {

        enum AlreadyFullyGrown implements GrowthResult {
            INSTANCE
        }

        enum CanNotGrow implements GrowthResult {
            INSTANCE
        }

        record FailedTransitionResult(EntityTransitionUtil.EntityTransitionResult result) implements GrowthResult {}

        record Success(Entity newEntity) implements GrowthResult {}
    }
}

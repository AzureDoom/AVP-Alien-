package com.alien.common.gameplay.entity.living.alien;

import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.Boiler;
import com.alien.common.model.lifecycle.growth.GrowthRequirement;
import com.alien.common.model.lifecycle.growth.GrowthStage;
import com.alien.common.registry.GrowthStageRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.compatibility.avp_human.AVPHuman;
import com.alien.compatibility.avp_human.GeneManagerProxy;
import com.blib.api.common.entity.v1.EntityTransitionUtil;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
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
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class GrowthManager implements NBTSerializable {

    private static final String GROWTH_TIME_IN_TICKS_TAG_KEY = "growthTimeInTicks";

    private static final Set<String> TRANSITION_NBT_KEY_BLACKLIST = Util.make(() -> {
        var set = new HashSet<>(EntityTransitionUtil.DEFAULT_NBT_KEY_BLACKLIST);
        set.add(GROWTH_TIME_IN_TICKS_TAG_KEY);
        set.add(MoltingManager.FORM_SCALE_PHASE_INDEX_TAG);
        set.add(MoltingManager.FORM_SCALE_PHASE_TICKS_TAG);
        return set;
    });

    private final Alien entity;

    private final @Nullable Consumer<Entity> onGrowUpCallback;

    private boolean growOverTime;

    private int growthTimeInTicks;

    private int growthRetryTimeInTicks;

    private boolean readyToGrow;

    private @Nullable GrowthStage activeRequirementGrowthStage;

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
        if (entity.level().isClientSide || canNeverGrow()) {
            return;
        }

        var matchingStage = findActiveOrMatchingGrowthStage();

        if (matchingStage == null) {
            return;
        }

        if (matchingStage.hasRequirements()) {
            tickEffectBasedGrowth(matchingStage);
        } else if (growOverTime) {
            tickTimeBasedGrowth(matchingStage);
        }

        if (!readyToGrow) {
            return;
        }

        if (!entity.getMoltingManager().hasReachedTargetScale()) {
            return;
        }

        this.growthRetryTimeInTicks = Math.max(growthRetryTimeInTicks - 1, 0);

        if (growthRetryTimeInTicks > 0) {
            return;
        }

        switch (grow(matchingStage)) {
            case GrowthResult.AlreadyFullyGrown ignored -> {/* NO-OP */}
            case GrowthResult.CanNotGrow ignored -> {/* NO-OP */}
            case GrowthResult.Success ignored -> {/* NO-OP */}
            case GrowthResult.FailedTransitionResult failedTransitionResult -> {
                if (failedTransitionResult.result instanceof EntityTransitionUtil.EntityTransitionResult.Obstructed) {
                    this.growthRetryTimeInTicks = 20 * 10;
                }
            }
        }
    }

    private @Nullable GrowthStage findActiveOrMatchingGrowthStage() {
        if (activeRequirementGrowthStage != null) {
            return activeRequirementGrowthStage;
        }

        return findMatchingGrowthStage();
    }

    private @Nullable GrowthStage findMatchingGrowthStage() {
        var hostType = entity.getHostType().unwrapOr(null);
        var candidates = GrowthStageRegistry.getCandidates(hostType, entity.getType());

        for (var candidate : candidates) {
            if (!candidate.hasRequirements()) {
                return candidate;
            }

            if (allRequirementsMet(candidate.requirements())) {
                return candidate;
            }
        }

        return null;
    }

    private boolean allRequirementsMet(List<GrowthRequirement> requirements) {
        for (var requirement : requirements) {
            if (!requirement.test(entity)) {
                return false;
            }
        }

        return true;
    }

    private void tickEffectBasedGrowth(GrowthStage stage) {
        var requirementsMet = allRequirementsMet(stage.requirements());

        if (requirementsMet) {
            activeRequirementGrowthStage = stage;
        }

        if (activeRequirementGrowthStage == null) {
            this.readyToGrow = false;
            return;
        }

        if (!requirementsMet && !entity.getMoltingManager().isMolting() && !entity.getMoltingManager().hasReachedTargetScale()) {
            activeRequirementGrowthStage = null;
            this.readyToGrow = false;
            return;
        }

        this.readyToGrow = true;
    }

    private void tickTimeBasedGrowth(GrowthStage stage) {
        this.growthTimeInTicks++;

        if (growthTimeInTicks >= stage.growthTimeInTicks()) {
            this.readyToGrow = true;
        }
    }

    public GrowthResult grow() {
        var stage = findMatchingGrowthStage();

        if (stage == null) {
            return GrowthResult.AlreadyFullyGrown.INSTANCE;
        }

        return grow(stage);
    }

    public GrowthResult grow(GrowthStage growthStage) {
        this.growthTimeInTicks = 0;
        this.readyToGrow = false;
        this.activeRequirementGrowthStage = null;

        if (canNeverGrow()) {
            return GrowthResult.CanNotGrow.INSTANCE;
        }

        var nextFormType = growthStage.to();
        var canBecomeBoiler = canBecomeBoiler(nextFormType);

        if (canBecomeBoiler) {
            nextFormType = Boiler.getType(entity.getVariant());
        }

        removeRequirementEffects(growthStage);

        var transitionResult = EntityTransitionUtil.transitionInto(entity, nextFormType, TRANSITION_NBT_KEY_BLACKLIST);

        Entity nextForm = null;

        if (transitionResult instanceof EntityTransitionUtil.EntityTransitionResult.Success<?> success) {
            nextForm = success.newEntity();
        }

        if (nextForm == null) {
            return new GrowthResult.FailedTransitionResult(transitionResult);
        }

        if (onGrowUpCallback != null) {
            onGrowUpCallback.accept(nextForm);
        }

        return new GrowthResult.Success(nextForm);
    }

    private boolean canNeverGrow() {
        return entity.isPoisoned() || entity.isIrradiated();
    }

    private void removeRequirementEffects(GrowthStage stage) {
        for (var requirement : stage.requirements()) {
            if (requirement instanceof GrowthRequirement.MobEffectRequirement effectRequirement) {
                entity.removeEffect(effectRequirement.effect());
            }
        }
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

        return isCurrentlyAdolescent && willGrowIntoAdult;
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
                var totalGeneIntegrity = Math.abs(GeneIntegrityUtil.getTotalGeneticIntegrity(geneCarrier));
                var chance = totalGeneIntegrity - Math.floor(totalGeneIntegrity);
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

    public boolean hasActiveGrowthRequirement() {
        var stage = activeRequirementGrowthStage != null ? activeRequirementGrowthStage : findMatchingGrowthStage();
        return stage != null && stage.hasRequirements() && allRequirementsMet(stage.requirements());
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

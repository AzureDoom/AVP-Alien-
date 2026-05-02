package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.RazorClawGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack.RazorClawSpecialAttackActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack.RazorClawSpecialAttackConfig;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienMobEffects;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienBlockTags;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.goap.v1.GOAPUser;
import com.blib.api.common.pathfinding.v1.cache.TerrainCacheRegistry;
import com.blib.api.common.pathfinding.v1.evaluator.TerrainEvaluatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigator;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import com.blib.api.common.pathfinding.v1.search.SearchConfig;
import com.blib.api.common.pathfinding.v1.terrain.BlockBreakabilityEvaluators;
import com.blib.api.common.pathfinding.v1.terrain.TerrainClassifiers;
import com.blib.api.common.pathfinding.v1.terrain.TerrainType;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class RazorClaw extends Xenomorph implements GOAPUser<RazorClaw>, PathNavigatorUser {

    private static final int BLOOD_LOSS_DURATION_IN_TICKS = 20 * 15;

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    public static AttributeSupplier.Builder createRazorClawAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 12.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 12.0F)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.75F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.7f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 5F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1.2F);
    }

    public final DataAccessor<XenomorphAttackType> attackType;

    private final RazorClawAnimationDispatcher animationDispatcher;

    private final RazorClawData razorClawData;

    private final PathNavigator pathNavigator;

    private final Set<Integer> specialAttackHitEntityIds;

    private int specialAttackTicksRemaining;

    private int specialAttackDurationInTicks;

    private float specialAttackYaw;

    public RazorClaw(EntityType<? extends RazorClaw> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_ATTACK_TYPE.get());
        this.animationDispatcher = new RazorClawAnimationDispatcher(this);
        this.razorClawData = new RazorClawData();
        this.pathNavigator = createPathNavigator(level);
        this.specialAttackHitEntityIds = new HashSet<>();
        getXenomorphData().setParallelDigCount(2);
    }

    private PathNavigator createPathNavigator(Level level) {
        var evaluatorConfig = TerrainEvaluatorConfig.builder()
            .addTerrain(TerrainType.GROUND, 1.0f)
            .addTerrain(TerrainType.WATER, 4.0f)
            .addTerrain(TerrainType.BREAKABLE, 8.0f)
            .withTerrainClassifier(TerrainClassifiers.GROUND_AND_WATER)
            .withBreakabilityEvaluator(
                BlockBreakabilityEvaluators.withExcludedTag(
                    BlockBreakabilityEvaluators.defaultEvaluator(MAX_BREAKABLE_DESTROY_TIME),
                    AlienBlockTags.XENOMORPH_IMMUNE
                )
            )
            .withEntitySize(1, 4)
            .withMaxFallDistance(14)
            .withCanOpenDoors(false)
            .build();

        var followRange = (float) getAttributeValue(Attributes.FOLLOW_RANGE);

        var navigatorConfig = PathNavigatorConfig.builder(evaluatorConfig)
            .withSearchConfig(SearchConfig.fromFollowRange(followRange))
            .build();

        var classificationCache = TerrainCacheRegistry.getOrCreate(level, evaluatorConfig.getTerrainClassifier());

        return new PathNavigator(level, navigatorConfig, classificationCache);
    }

    @Override
    public Agent.Builder<RazorClaw> blib$applyGOAPAgentProperties(Agent.Builder<RazorClaw> agentBuilder) {
        return RazorClawGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<RazorClaw> blib$getGOAPGraphOrNull() {
        return getActiveGOAPGraph(RazorClawGOAP.GRAPH);
    }

    @Override
    public PathNavigator getPathNavigator() {
        return pathNavigator;
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant);
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        var result = super.doHurtTarget(entity);

        if (result && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(
                new MobEffectInstance(
                    AlienMobEffects.getBloodLossHolder(),
                    BLOOD_LOSS_DURATION_IN_TICKS,
                    0
                )
            );
        }

        return result;
    }

    @Override
    public boolean isAttacking() {
        return attackType.get() != XenomorphAttackType.NONE;
    }

    @Override
    protected void resetAttackType() {
        attackType.set(XenomorphAttackType.NONE);
    }

    @Override
    public void runAttackAnimations() {
        var attackVariant = random.nextInt(0, 3);

        playSound(
            AlienSoundEvents.ENTITY_XENOMORPH_ATTACK.get(),
            getSoundVolume(),
            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );

        var attack = switch (attackVariant) {
            case 0 -> XenomorphAttackType.CLAW;
            case 1 -> XenomorphAttackType.BITE;
            default -> XenomorphAttackType.TAIL;
        };

        attackType.set(attack);
        beginAttack(attack.defaultDurationInTicks());
    }

    public void startSpecialAttack(int durationInTicks, LivingEntity target) {
        specialAttackYaw = computeYawTowards(target);
        specialAttackTicksRemaining = durationInTicks;
        specialAttackDurationInTicks = durationInTicks;
        specialAttackHitEntityIds.clear();
        attackType.set(XenomorphAttackType.SPECIAL);
        beginAttack(durationInTicks + 1);
    }

    public boolean isUsingSpecialAttack() {
        return specialAttackTicksRemaining > 0 || attackType.get() == XenomorphAttackType.SPECIAL;
    }

    public long getNearbyMeleeAttackTargetCount() {
        var closeTargetRange = getBbWidth() + 1.0;
        var closeTargetRangeSquared = closeTargetRange * closeTargetRange;

        return getEntitySenseCache()
            .getByClass(LivingEntity.class)
            .stream()
            .filter(target -> distanceToSqr(target) <= closeTargetRangeSquared)
            .filter(target -> getSensing().hasLineOfSight(target))
            .filter(target -> AlienPredicates.canTarget(this, target))
            .count();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            razorClawData.tick();
            tickSpecialAttack();
        }
    }

    private void tickSpecialAttack() {
        if (specialAttackTicksRemaining <= 0) {
            return;
        }

        lockSpecialAttackYaw();

        var previousElapsedTicks = specialAttackDurationInTicks - specialAttackTicksRemaining;
        specialAttackTicksRemaining--;
        var elapsedTicks = specialAttackDurationInTicks - specialAttackTicksRemaining;

        RazorClawSpecialAttackActions.damageSweptArc(
            this,
            RazorClawSpecialAttackConfig.DEFAULT,
            previousElapsedTicks,
            elapsedTicks,
            specialAttackYaw,
            specialAttackHitEntityIds
        );

        if (specialAttackTicksRemaining <= 0) {
            specialAttackHitEntityIds.clear();
            resetAttackType();
            attackDurationInTicks.set(0);
        }
    }

    private void lockSpecialAttackYaw() {
        setYRot(specialAttackYaw);
        setYHeadRot(specialAttackYaw);
        setYBodyRot(specialAttackYaw);
        getNavigation().stop();
    }

    private float computeYawTowards(LivingEntity target) {
        var dx = target.getX() - getX();
        var dz = target.getZ() - getZ();
        return (float) (Mth.atan2(-dx, dz) * Mth.RAD_TO_DEG);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        razorClawData.load(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        razorClawData.save(compoundTag);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public RazorClawAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public RazorClawData getRazorClawData() {
        return razorClawData;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.RAZOR_CLAW.get();
            case NETHER -> AlienEntityTypes.NETHER_RAZOR_CLAW.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_RAZOR_CLAW.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_RAZOR_CLAW.get();
        };
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.RavagerGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackConfig;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienBlockTags;
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
import com.just.goap.Agent;
import com.just.goap.graph.Graph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Ravager extends Xenomorph implements GOAPUser<Ravager>, PathNavigatorUser {

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    private static final int ATTACK_DURATION_MULTIPLIER = 3;

    public static AttributeSupplier.Builder createRavagerAttributes() {
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

    private final RavagerAnimationDispatcher animationDispatcher;

    private final RavagerData ravagerData;

    private final PathNavigator pathNavigator;

    private int specialWindupTicksRemaining;

    private int specialAttackTicksRemaining;

    private int specialAttackDurationInTicks;

    private boolean specialAttackDamageDealt;

    private float specialAttackYaw;

    public Ravager(EntityType<? extends Ravager> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_ATTACK_TYPE.get());
        this.animationDispatcher = new RavagerAnimationDispatcher(this);
        this.ravagerData = new RavagerData();
        this.pathNavigator = createPathNavigator(level);
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
    public Agent.Builder<Ravager> blib$applyGOAPAgentProperties(Agent.Builder<Ravager> agentBuilder) {
        return RavagerGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Ravager> blib$getGOAPGraphOrNull() {
        return RavagerGOAP.GRAPH;
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
    public boolean isAttacking() {
        return attackType.get() != XenomorphAttackType.NONE;
    }

    @Override
    protected void resetAttackType() {
        attackType.set(XenomorphAttackType.NONE);
    }

    @Override
    public void runAttackAnimations() {
        var attackVariant = random.nextInt(0, 4);

        playSound(
            AlienSoundEvents.ENTITY_XENOMORPH_ATTACK.get(),
            getSoundVolume(),
            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );

        var attack = isUnderWater()
            ? XenomorphAttackType.SWIM_ATTACK
            : switch (attackVariant) {
                case 0 -> XenomorphAttackType.CLAW;
                case 1 -> XenomorphAttackType.CLAW_DOUBLE;
                case 2 -> XenomorphAttackType.BITE;
                default -> XenomorphAttackType.TAIL;
            };

        attackType.set(attack);
        beginAttack(attack.defaultDurationInTicks() * ATTACK_DURATION_MULTIPLIER);
    }

    public void startSpecialAttackWindup(int durationInTicks, LivingEntity target) {
        specialAttackYaw = computeYawTowards(target);
        specialWindupTicksRemaining = durationInTicks;
        specialAttackTicksRemaining = 0;
        specialAttackDurationInTicks = 0;
        specialAttackDamageDealt = false;
        attackType.set(XenomorphAttackType.SPECIAL_WINDUP);
        beginAttack(durationInTicks + 2);
    }

    public void activateSpecialAttack(int durationInTicks) {
        specialWindupTicksRemaining = 0;
        specialAttackTicksRemaining = durationInTicks;
        specialAttackDurationInTicks = durationInTicks;
        specialAttackDamageDealt = false;
        attackType.set(XenomorphAttackType.SPECIAL);
        beginAttack(durationInTicks + 1);
    }

    public boolean isUsingSpecialAttack() {
        var currentAttackType = attackType.get();
        return specialWindupTicksRemaining > 0
            || specialAttackTicksRemaining > 0
            || currentAttackType == XenomorphAttackType.SPECIAL_WINDUP
            || currentAttackType == XenomorphAttackType.SPECIAL;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            ravagerData.tick();
            tickSpecialAttack();
        }
    }

    private void tickSpecialAttack() {
        if (specialWindupTicksRemaining > 0) {
            lockSpecialAttackYaw();
            specialWindupTicksRemaining--;

            if (specialWindupTicksRemaining <= 0) {
                activateSpecialAttack(RavagerSpecialAttackConfig.DEFAULT.attackDurationInTicks());
            }

            return;
        }

        if (specialAttackTicksRemaining <= 0) {
            return;
        }

        lockSpecialAttackYaw();
        specialAttackTicksRemaining--;

        var elapsedTicks = specialAttackDurationInTicks - specialAttackTicksRemaining;
        var damageTickThreshold = (int) (specialAttackDurationInTicks * RavagerSpecialAttackConfig.DEFAULT.damagePointPercent());

        if (!specialAttackDamageDealt && elapsedTicks >= damageTickThreshold) {
            RavagerSpecialAttackActions.damageEntitiesInFront(this, RavagerSpecialAttackConfig.DEFAULT);
            specialAttackDamageDealt = true;
        }

        if (specialAttackTicksRemaining <= 0) {
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
        ravagerData.load(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ravagerData.save(compoundTag);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public RavagerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public RavagerData getRavagerData() {
        return ravagerData;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.RAVAGER.get();
            case NETHER -> AlienEntityTypes.NETHER_RAVAGER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_RAVAGER.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_RAVAGER.get();
        };
    }
}

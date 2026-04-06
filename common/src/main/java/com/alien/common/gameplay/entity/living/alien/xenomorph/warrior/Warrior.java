package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphNavigationManager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai.WarriorGOAP;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienBlockTags;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.goap.v1.GOAPUser;
import com.blib.api.common.pathfinding.v1.cache.TerrainCacheRegistry;
import com.blib.api.common.pathfinding.v1.evaluator.Posture;
import com.blib.api.common.pathfinding.v1.evaluator.TerrainEvaluatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigator;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import com.blib.api.common.pathfinding.v1.physics.ClimbingMoveControl;
import com.blib.api.common.pathfinding.v1.search.SearchConfig;
import com.blib.api.common.pathfinding.v1.terrain.BlockBreakabilityEvaluators;
import com.blib.api.common.pathfinding.v1.terrain.TerrainClassifiers;
import com.blib.api.common.pathfinding.v1.terrain.TerrainType;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Warrior extends Xenomorph implements GOAPUser<Warrior>, PathNavigatorUser {

    public static AttributeSupplier.Builder createWarriorAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 8.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.5F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.5f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 3F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1.1F);
    }

    public final DataAccessor<XenomorphAttackType> attackType;

    private final WarriorAnimationDispatcher animationDispatcher;

    private final PathNavigator pathNavigator;

    public Warrior(EntityType<? extends Warrior> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_ATTACK_TYPE.get());
        this.animationDispatcher = new WarriorAnimationDispatcher(this);
        this.pathNavigator = createPathNavigator(level);
        getXenomorphData().setParallelDigCount(1);
    }

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    private static final Posture STANDING = new Posture("default", 1, 2);

    private static final Posture CRAWLING = new Posture("crawling", 1, 1);

    private static final float CRAWL_TRANSITION_COST = 0.5f;

    private PathNavigator createPathNavigator(Level level) {
        var evaluatorConfig = TerrainEvaluatorConfig.builder()
            .addTerrain(TerrainType.GROUND, 1.0f)
            .addTerrain(TerrainType.CLIMBABLE, 1.0f)
            .addTerrain(TerrainType.WATER, 4.0f)
            .addTerrain(TerrainType.BREAKABLE, 8.0f)
            .withTerrainClassifier(TerrainClassifiers.GROUND_AND_WATER)
            .withBreakabilityEvaluator(
                BlockBreakabilityEvaluators.withExcludedTag(
                    BlockBreakabilityEvaluators.defaultEvaluator(MAX_BREAKABLE_DESTROY_TIME),
                    AlienBlockTags.XENOMORPH_IMMUNE
                )
            )
            .addPosture(STANDING)
            .addPosture(CRAWLING, CRAWL_TRANSITION_COST)
            .withClimbingPostureIndex(1)
            .withMaxFallDistance(14)
            .withCanOpenDoors(true)
            .build();

        var followRange = (float) getAttributeValue(Attributes.FOLLOW_RANGE);

        var navigatorConfig = PathNavigatorConfig.builder(evaluatorConfig)
            .withSearchConfig(SearchConfig.fromFollowRange(followRange))
            .onPostureEnter(0, () -> isCrawling.set(false))
            .onPostureEnter(1, () -> isCrawling.set(true))
            .build();

        var classificationCache = TerrainCacheRegistry.getOrCreate(level, evaluatorConfig.getTerrainClassifier());

        return new PathNavigator(level, navigatorConfig, classificationCache);
    }

    @Override
    protected @NotNull XenomorphNavigationManager createNavigationManager() {
        return new XenomorphNavigationManager(this, new ClimbingMoveControl(this));
    }

    @Override
    public Agent.Builder<Warrior> blib$applyGOAPAgentProperties(Agent.Builder<Warrior> agentBuilder) {
        return WarriorGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Warrior> blib$getGOAPGraphOrNull() {
        return WarriorGOAP.GRAPH;
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
    protected void registerGoals() {
        // GOAP handles all AI for the warrior.
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

    @Override
    public void runDigAnimation() {
        playSound(
            AlienSoundEvents.ENTITY_XENOMORPH_ATTACK.get(),
            getSoundVolume(),
            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );

        attackType.set(XenomorphAttackType.CLAW);
        beginAttack(XenomorphAttackType.CLAW.defaultDurationInTicks());
    }

    public WarriorAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.WARRIOR.get();
            case NETHER -> AlienEntityTypes.NETHER_WARRIOR.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_WARRIOR.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_WARRIOR.get();
        };
    }
}

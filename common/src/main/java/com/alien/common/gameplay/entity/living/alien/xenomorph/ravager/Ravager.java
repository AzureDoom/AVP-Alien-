package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.AttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.DamageApplicator;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphPathConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.RavagerClawAttackActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.RavagerGOAP;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.goap.v1.GOAPUser;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Ravager extends Xenomorph implements GOAPUser<Ravager> {

    public static final double FRONT_AOE_RANGE_IN_BLOCKS = 5.0;

    public static final double FRONT_AOE_CONE_ANGLE_DEGREES = 90.0;

    private static final int ATTACK_DURATION_MULTIPLIER = 3;

    private static final DamageApplicator SINGLE_CLAW_APPLICATOR = (xenomorph, target) -> {
        if (!(xenomorph instanceof Ravager ravager)) {
            return;
        }
        ravager.swing(InteractionHand.MAIN_HAND);
        RavagerClawAttackActions.singleClaw(ravager);
    };

    private static final DamageApplicator DOUBLE_CLAW_APPLICATOR = (xenomorph, target) -> {
        if (!(xenomorph instanceof Ravager ravager)) {
            return;
        }
        ravager.swing(InteractionHand.MAIN_HAND);
        RavagerClawAttackActions.doubleClaw(ravager);
    };

    public static final AttackType CLAW = AttackType.builder("ravager_claw")
        .defaultDurationInTicks(10 * ATTACK_DURATION_MULTIPLIER)
        .sound(AlienSoundEvents.ENTITY_XENOMORPH_ATTACK)
        .damageApplicator(SINGLE_CLAW_APPLICATOR)
        .build();

    public static final AttackType CLAW_DOUBLE = AttackType.builder("ravager_claw_double")
        .defaultDurationInTicks(10 * ATTACK_DURATION_MULTIPLIER)
        .sound(AlienSoundEvents.ENTITY_XENOMORPH_ATTACK)
        .damageApplicator(DOUBLE_CLAW_APPLICATOR)
        .build();

    public static final AttackType BITE = AttackType.builder("ravager_bite")
        .defaultDurationInTicks(8 * ATTACK_DURATION_MULTIPLIER)
        .sound(AlienSoundEvents.ENTITY_XENOMORPH_ATTACK)
        .build();

    public static final AttackType TAIL = AttackType.builder("ravager_tail")
        .defaultDurationInTicks(12 * ATTACK_DURATION_MULTIPLIER)
        .sound(AlienSoundEvents.ENTITY_XENOMORPH_ATTACK)
        .build();

    public static final AttackType SWIM_ATTACK = AttackType.builder("ravager_swim_attack")
        .defaultDurationInTicks(10 * ATTACK_DURATION_MULTIPLIER)
        .sound(AlienSoundEvents.ENTITY_XENOMORPH_ATTACK)
        .build();

    private static final XenomorphConfig CONFIG = XenomorphConfig.builder(XenomorphPathConfig.LARGE, Ravager::getType)
        .attackConfig(
            XenomorphAttackConfig.builder()
                .addTriggered(RavagerChargeAttack.ATTACK)
                .build()
        )
        .parallelDigCount(2)
        .pushedByFluid(false)
        .build();

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

    private final RavagerAnimationDispatcher animationDispatcher;

    public Ravager(EntityType<? extends Ravager> entityType, Level level) {
        super(entityType, level, CONFIG);
        this.animationDispatcher = new RavagerAnimationDispatcher(this);
    }

    @Override
    public Agent.Builder<Ravager> blib$applyGOAPAgentProperties(Agent.Builder<Ravager> agentBuilder) {
        return RavagerGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Ravager> blib$getGOAPGraphOrNull() {
        return getActiveGOAPGraph(RavagerGOAP.GRAPH);
    }

    @Override
    public void runAttackAnimations() {
        startAttack(selectAttack(), getTarget());
    }

    private AttackType selectAttack() {
        if (isUnderWater()) {
            return SWIM_ATTACK;
        }

        if (getNearbyCloseAttackTargetCount() > 1) {
            return random.nextBoolean() ? CLAW : CLAW_DOUBLE;
        }

        return switch (random.nextInt(0, 4)) {
            case 0 -> CLAW;
            case 1 -> CLAW_DOUBLE;
            case 2 -> BITE;
            default -> TAIL;
        };
    }

    private long getNearbyCloseAttackTargetCount() {
        var closeTargetRangeSquared = FRONT_AOE_RANGE_IN_BLOCKS * FRONT_AOE_RANGE_IN_BLOCKS;

        return getEntitySenseCache()
            .getByClass(LivingEntity.class)
            .stream()
            .filter(target -> distanceToSqr(target) <= closeTargetRangeSquared)
            .filter(target -> getSensing().hasLineOfSight(target))
            .filter(target -> AlienPredicates.canTarget(this, target))
            .count();
    }

    public RavagerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
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

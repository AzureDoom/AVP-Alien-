package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.model.resin.ResinData;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.entity.v1.ai.goal.combat.LungeAtTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Warrior extends Xenomorph {

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

    public Warrior(EntityType<? extends Warrior> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_ATTACK_TYPE.get());
        this.animationDispatcher = new WarriorAnimationDispatcher(this);
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
    protected @Nullable ResinData createResinData() {
        return new ResinData(0, 32, 1, 20);
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
        super.registerGoals();
        goalSelector.addGoal(3, new LungeAtTargetGoal(this, 0.1F, 20 * 5, 6, 15).setOnLungeCallback(this::runLungeAnimation));
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

    private void runLungeAnimation() {
        playSound(AlienSoundEvents.ENTITY_XENOMORPH_LUNGE.get(), getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        isLunging.set(true);
    }

    @Override
    public Integer getMaxJellyToGrowth() {
        return 4;
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

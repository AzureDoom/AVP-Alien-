package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler;

import com.alien.common.gameplay.ai.InvestigateVibrationGoal;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.model.resin.ResinData;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.util.AcidBleedUtil;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.entity.v1.vibration.VibrationSystemManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Boiler extends Xenomorph {

    public static AttributeSupplier.Builder createBoilerAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 2.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.25F)
            .add(Attributes.FOLLOW_RANGE, 16F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.3f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 2F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1F);
    }

    private final BoilerAnimationDispatcher animationDispatcher;

    private final VibrationSystemManager vibrationSystemManager;

    public Boiler(EntityType<? extends Boiler> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new BoilerAnimationDispatcher(this);
        this.vibrationSystemManager = new VibrationSystemManager(this, 2.5F, 32);
    }

    @Override
    public void tick() {
        super.tick();
        vibrationSystemManager.tick();
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        vibrationSystemManager.updateDynamicGameEventListener(biConsumer);
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant);
    }

    @Override
    protected @Nullable ResinData createResinData() {
        return null;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new InvestigateVibrationGoal(this));
    }

    @Override
    protected boolean canTargetInitially(LivingEntity target) {
        return target.distanceToSqr(this) <= 4 * 4
            && super.canTargetInitially(target);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        var radius = 2F;
        level().explode(this, getX(), getY(), getZ(), radius, Level.ExplosionInteraction.MOB);
        // TODO:
        // this.spawnLingeringCloud();
        triggerOnDeathMobEffects(RemovalReason.KILLED);
        discard();

        // TODO: Redo the way acid strength is determined here, it isn't the greatest.
        getBlockArea(blockPosition(), (int) radius, (int) radius, (int) radius)
            .stream()
            .filter(blockPos -> {
                var blockState = level().getBlockState(blockPos);
                return blockState.isAir() || blockState.canBeReplaced();
            })
            .forEach(blockPos -> AcidBleedUtil.spawnAcid(this, 3, blockPos.getCenter()));

        return true;
    }

    // TODO: Move this to a util class.
    private List<BlockPos> getBlockArea(BlockPos center, int radiusX, int radiusY, int radiusZ) {
        var positions = new ArrayList<BlockPos>();

        for (var dx = -radiusX; dx <= radiusX; dx++) {
            for (var dy = -radiusY; dy <= radiusY; dy++) {
                for (var dz = -radiusZ; dz <= radiusZ; dz++) {
                    positions.add(center.offset(dx, dy, dz));
                }
            }
        }

        return positions;
    }

    @Override
    protected void addDigToTargetGoal() {}

    @Override
    public void runAttackAnimations() {}

    @Override
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    @Override
    public Integer getMaxJellyToGrowth() {
        return null;
    }

    public BoilerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public VibrationSystemManager getVibrationSystemManager() {
        return vibrationSystemManager;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.BOILER.get();
            case NETHER -> AlienEntityTypes.NETHER_BOILER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_BOILER.get();
            case IRRADIATED -> null;
        };
    }
}

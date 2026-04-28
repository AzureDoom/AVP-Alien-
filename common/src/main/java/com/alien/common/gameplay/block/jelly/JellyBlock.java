package com.alien.common.gameplay.block.jelly;

import com.alien.common.registry.init.block.AlienBlocks;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class JellyBlock extends Block {

    private static final double SLIDE_STARTS_WHEN_VERTICAL_SPEED_IS_AT_LEAST = 0.13;

    private static final double MIN_FALL_SPEED_TO_BE_CONSIDERED_SLIDING = 0.08;

    private static final double THROTTLE_SLIDE_SPEED_TO = 0.05;

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);

    public JellyBlock(Properties properties) {
        super(properties);
    }

    private static boolean doesEntityDoSlideEffects(Entity entity) {
        return entity instanceof LivingEntity || entity instanceof AbstractMinecart || entity instanceof PrimedTnt
            || entity instanceof Boat;
    }

    private static boolean isAlien(Entity entity) {
        return entity.getType().is(AlienEntityTypeTags.ALIENS);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(
        @NotNull BlockState state,
        @NotNull BlockGetter level,
        @NotNull BlockPos pos,
        @NotNull CollisionContext context
    ) {
        return SHAPE;
    }

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (isAlien(entity)) {
            super.fallOn(level, state, pos, entity, fallDistance);
            return;
        }

        // TODO: Jelly block slide sfx
//        entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);

        if (!level.isClientSide) {
            // TODO: Jelly block jump particles
//            level.broadcastEntityEvent(entity, EntityEvent.HONEY_JUMP);
        }

        if (entity.causeFallDamage(fallDistance, 0.2F, level.damageSources().fall())) {
            entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
        }
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!isAlien(entity) && this.isSlidingDown(pos, entity)) {
            this.doSlideMovement(entity);
            this.maybeDoSlideEffects(level, entity);
        }

        super.entityInside(state, level, pos, entity);
    }

    private boolean isSlidingDown(BlockPos pos, Entity entity) {
        if (entity.onGround()) {
            return false;
        } else if (entity.getY() > (double) pos.getY() + 0.9375 - 1.0E-7) {
            return false;
        } else if (entity.getDeltaMovement().y >= -MIN_FALL_SPEED_TO_BE_CONSIDERED_SLIDING) {
            return false;
        } else {
            double dx = Math.abs((double) pos.getX() + 0.5 - entity.getX());
            double dz = Math.abs((double) pos.getZ() + 0.5 - entity.getZ());
            double edge = 0.4375 + (double) (entity.getBbWidth() / 2.0F);
            return dx + 1.0E-7 > edge || dz + 1.0E-7 > edge;
        }
    }

    private void doSlideMovement(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < -SLIDE_STARTS_WHEN_VERTICAL_SPEED_IS_AT_LEAST) {
            double d = -THROTTLE_SLIDE_SPEED_TO / vec3.y;
            entity.setDeltaMovement(new Vec3(vec3.x * d, -THROTTLE_SLIDE_SPEED_TO, vec3.z * d));
        } else {
            entity.setDeltaMovement(new Vec3(vec3.x, -THROTTLE_SLIDE_SPEED_TO, vec3.z));
        }

        entity.resetFallDistance();
    }

    private void maybeDoSlideEffects(Level level, Entity entity) {
        if (doesEntityDoSlideEffects(entity)) {
            if (level.random.nextInt(5) == 0) {
                // TODO: Jelly block slide sfx
//                entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }

            if (!level.isClientSide && level.random.nextInt(5) == 0) {
                // TODO: Jelly block slide particles
//                level.broadcastEntityEvent(entity, EntityEvent.HONEY_SLIDE);
            }
        }
    }

    @Override
    protected boolean skipRendering(
        @NotNull BlockState state,
        @NotNull BlockState adjacentState,
        @NotNull net.minecraft.core.Direction direction
    ) {
        return adjacentState.is(this) || super.skipRendering(state, adjacentState, direction);
    }
}

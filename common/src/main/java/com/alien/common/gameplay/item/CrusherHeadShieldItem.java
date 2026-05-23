package com.alien.common.gameplay.item;

import com.alien.common.data.AlienAdvancements;
import com.alien.common.gameplay.item.ability.ShieldAbilityItem;
import com.alien.common.registry.key.AlienDamageTypeKeys;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.shield.v1.BLibShieldConfig;
import com.blib.api.common.shield.v1.BLibShieldItem;
import com.blib.api.common.shield.v1.BlockResult;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * The shield variant crafted from a {@link CrusherHeadItem} + vanilla shield. Pure shield behavior — held to block,
 * takes durability damage on block, can be disabled by axes / explosions / shield-disabling attacks. Not wearable (no
 * head slot) and not placeable (no block form). Conversion is one-way: there's no recipe to revert this back into a
 * wearable {@link CrusherHeadItem}.
 */
public class CrusherHeadShieldItem extends Item implements BLibShieldItem, ShieldAbilityItem {

    private static final BLibShieldConfig CONFIG = new BLibShieldConfig(
        72000,
        180f,
        SoundEvents.SHIELD_BLOCK
    );

    private static final int ABILITY_COOLDOWN_TICKS = 40;
    private static final int ABILITY_DURABILITY_COST = 8;
    private static final double DASH_SPEED = 1.75;
    private static final double DASH_VERTICAL_BOOST = 0.12;
    private static final double DASH_RANGE = 4.5;
    private static final double DASH_HALF_WIDTH = 1.1;
    private static final double DASH_VERTICAL_REACH = 1.25;
    private static final double KNOCKBACK_STRENGTH = 2.5;
    private static final double KNOCKBACK_VERTICAL_BOOST = 0.175;

    public CrusherHeadShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public BLibShieldConfig getShieldConfig() {
        return CONFIG;
    }

    @Override
    public BlockResult onBlocked(LivingEntity user, ItemStack stack, DamageSource source, float damage) {
        // Xenomorph shields resist acid spit for free with no damage applied.
        if (source.is(AlienDamageTypeKeys.ACID_SPIT)) {
            grantSpitBlockAdvancement(user, source);
            return BlockResult.fullBlock();
        }

        var damageDealt = (int) Math.max(1, Math.ceil(damage));
        stack.hurtAndBreak(damageDealt, user, EquipmentSlot.MAINHAND);

        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return BlockResult.blockedAndDisabled(60);
        }

        if (
            source.getDirectEntity() instanceof LivingEntity attacker
                && (attacker.getMainHandItem().getItem() instanceof AxeItem
                    || attacker.canDisableShield())
        ) {
            return BlockResult.blockedAndDisabled(100);
        }

        return BlockResult.fullBlock();
    }

    @Override
    public void activateShieldAbility(ServerPlayer player, ItemStack stack) {
        var direction = getHorizontalLookDirection(player);
        dashPlayer(player, direction);
        knockEntitiesInPath(player, direction);

        player.level()
            .playSound(
                null,
                player,
                AlienSoundEvents.ENTITY_XENOMORPH_LUNGE.get(),
                SoundSource.PLAYERS,
                0.9F,
                0.85F + player.getRandom().nextFloat() * 0.2F
            );

        player.getCooldowns().addCooldown(stack.getItem(), ABILITY_COOLDOWN_TICKS);
        stack.hurtAndBreak(ABILITY_DURABILITY_COST, player, getUsingEquipmentSlot(player));
        player.stopUsingItem();
    }

    private static void dashPlayer(ServerPlayer player, Vec3 direction) {
        var verticalBoost = player.onGround() ? DASH_VERTICAL_BOOST : 0.0;
        player.setDeltaMovement(direction.x * DASH_SPEED, verticalBoost, direction.z * DASH_SPEED);
        player.hurtMarked = true;
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    private static void knockEntitiesInPath(ServerPlayer player, Vec3 direction) {
        var searchBox = player.getBoundingBox()
            .expandTowards(direction.scale(DASH_RANGE))
            .inflate(DASH_HALF_WIDTH, DASH_VERTICAL_REACH, DASH_HALF_WIDTH);

        var targets = player.level()
            .getEntitiesOfClass(
                Entity.class,
                searchBox,
                target -> target != player && target.isAlive() && target.isPushable() && isInDashPath(player, target, direction)
            );

        for (var target : targets) {
            knockEntity(direction, target);
        }
    }

    private static void knockEntity(Vec3 direction, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.knockback(KNOCKBACK_STRENGTH, -direction.x, -direction.z);
            livingTarget.setDeltaMovement(livingTarget.getDeltaMovement().add(0.0, KNOCKBACK_VERTICAL_BOOST, 0.0));
        } else {
            target.setDeltaMovement(
                target.getDeltaMovement()
                    .add(direction.scale(KNOCKBACK_STRENGTH))
                    .add(0.0, KNOCKBACK_VERTICAL_BOOST, 0.0)
            );
        }
        target.hurtMarked = true;
    }

    private static boolean isInDashPath(ServerPlayer player, Entity target, Vec3 direction) {
        var toTarget = target.position()
            .add(0.0, target.getBbHeight() * 0.5, 0.0)
            .subtract(player.position().add(0.0, player.getBbHeight() * 0.5, 0.0));
        var horizontalToTarget = new Vec3(toTarget.x, 0.0, toTarget.z);
        var forwardDistance = horizontalToTarget.dot(direction);

        if (forwardDistance < -target.getBbWidth() * 0.5 || forwardDistance > DASH_RANGE + target.getBbWidth() * 0.5) {
            return false;
        }

        if (Math.abs(toTarget.y) > DASH_VERTICAL_REACH + target.getBbHeight() * 0.5) {
            return false;
        }

        var lateral = horizontalToTarget.subtract(direction.scale(forwardDistance));
        var allowedDistance = DASH_HALF_WIDTH + target.getBbWidth() * 0.5;
        return lateral.lengthSqr() <= allowedDistance * allowedDistance;
    }

    private static Vec3 getHorizontalLookDirection(ServerPlayer player) {
        var forward = player.getLookAngle();
        var horizontal = new Vec3(forward.x, 0.0, forward.z);
        if (horizontal.lengthSqr() > 1.0E-6) {
            return horizontal.normalize();
        }

        var yawRadians = player.getYRot() * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(yawRadians), 0.0, Mth.cos(yawRadians)).normalize();
    }

    private static EquipmentSlot getUsingEquipmentSlot(ServerPlayer player) {
        return player.getUsedItemHand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
    }

    private static void grantSpitBlockAdvancement(LivingEntity user, DamageSource source) {
        if (!(user instanceof ServerPlayer player)) {
            return;
        }

        var attacker = source.getEntity();
        if (attacker != null && attacker.getType().is(AlienEntityTypeTags.SPITTERS)) {
            AlienAdvancements.BLOCK_SPITTER_SPIT_WITH_HEAD_SHIELD.grant(player);
        }
    }
}

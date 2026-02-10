package com.alien.common.gameplay.entity.living.alien.ovomorph;

import com.alien.common.constant.HealthConstants;
import com.alien.common.constant.HealthRegenConstants;
import com.alien.common.constant.KnockbackResistanceConstants;
import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.ovomorph.ai.OvomorphGOAP;
import com.alien.common.model.alien.HatchState;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.vibration.VibrationSystemManager;
import com.blib.api.common.goap.v1.GOAPUser;
import com.just.core.functional.option.Option;
import com.just.goap.graph.Graph;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class Ovomorph extends Alien implements GOAPUser<Ovomorph>, Shearable {

    public static final HatchState DEFAULT_HATCH_STATE = HatchState.SLEEPING;

    public static AttributeSupplier.Builder createOvomorphAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 0f)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, 0f)
            .add(Attributes.FOLLOW_RANGE, 0f)
            .add(Attributes.KNOCKBACK_RESISTANCE, KnockbackResistanceConstants.OVOMORPH_KNOCKBACK_RESISTANCE)
            .add(Attributes.MAX_HEALTH, HealthConstants.OVOMORPH_HEALTH)
            .add(Attributes.MOVEMENT_SPEED, 0f);
    }

    public final DataAccessor<Byte> hatchStateId;

    public final DataAccessor<Byte> maxSpawnCount;

    public final DataAccessor<Boolean> isRooted;

    private final OvomorphAnimationDispatcher animationDispatcher;

    private final VibrationSystemManager vibrationSystemManager;

    private final HatchManager hatchManager;

    public boolean pickupRequestAcknowledged;

    public boolean wantsPickup;

    public Ovomorph(EntityType<? extends Ovomorph> entityType, Level level) {
        super(entityType, level);

        this.hatchStateId = new DataAccessor<>(this, AlienDataSyncKeys.OVOMORPH_HATCH_STATE.get());
        this.maxSpawnCount = new DataAccessor<>(this, AlienDataSyncKeys.OVOMORPH_MAXIMUM_SPAWN_COUNT.get());
        this.isRooted = new DataAccessor<>(this, AlienDataSyncKeys.OVOMORPH_IS_ROOTED.get());

        this.animationDispatcher = new OvomorphAnimationDispatcher(this);
        this.hatchManager = new HatchManager(this, 3 * 20, 3 * 20);
        this.wantsPickup = false;
        this.vibrationSystemManager = new VibrationSystemManager(this, 2.5F, 8);
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        vibrationSystemManager.updateDynamicGameEventListener(biConsumer);
    }

    @Override
    public @Nullable Graph<Ovomorph> blib$getGOAPGraphOrNull() {
        return OvomorphGOAP.GRAPH;
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant, isRoyal());
    }

    @Override
    public void tick() {
        super.tick();
        hatchManager.tick();
        vibrationSystemManager.tick();

        if (!level().isClientSide) {
            this.wantsPickup = canBePickedUp();

            if (!pickupRequestAcknowledged && wantsPickup && tickCount % 20 == 0) {
                var alienVariantType = AlienVariantTypes.getFor(this);
                var deferredHolder = alienVariantType.eggPickupRequestEvent();

                if (deferredHolder != null) {
                    gameEvent(deferredHolder);
                }
            }

            if (isPassenger()) {
                this.pickupRequestAcknowledged = false;
            }
        }
    }

    public boolean canBeHeld() {
        return isAlive()
            && !isDeadOrDying()
            && !isRooted.get()
            && getHatchState().contains(HatchState.SLEEPING);
    }

    public boolean canBePickedUp() {
        return canBeHeld()
            && onGround()
            && !isPassenger();
    }

    public void tryHatch() {
        if (
            !level().isClientSide
                && !hatchManager.isHatching()
                && !hatchManager.isHatched()
                && !isIrradiated()
        ) {
            hatchManager.hatch();
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (level().isClientSide) {
            return super.mobInteract(player, interactionHand);
        }

        var itemStack = player.getItemInHand(interactionHand);
        var resinBallItem = AlienVariantTypes.getFor(this).resinBall().get();

        if (isRooted.get() && itemStack.is(Items.SHEARS)) {
            shear(SoundSource.PLAYERS);
            gameEvent(GameEvent.SHEAR, player);
            itemStack.hurtAndBreak(1, player, getSlotForHand(interactionHand));

            return InteractionResult.SUCCESS;
        } else if (!isRooted.get() && itemStack.is(resinBallItem)) {
            level().playSound(null, this, AlienSoundEvents.ENTITY_OVOMORPH_ROOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            isRooted.set(true);
            itemStack.consume(1, player);
        }

        return super.mobInteract(player, interactionHand);
    }

    @Override
    public void shear(@NotNull SoundSource soundSource) {
        isRooted.set(false);
        level().playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
        level().playSound(null, this, AlienSoundEvents.ENTITY_OVOMORPH_SHEAR.get(), soundSource, 1.0F, 1.0F);
        var resinBallItem = AlienVariantTypes.getFor(this).resinBall().get();

        var itemEntity = this.spawnAtLocation(resinBallItem, 1);

        if (itemEntity != null) {
            itemEntity.setDeltaMovement(
                itemEntity.getDeltaMovement()
                    .add(
                        (random.nextFloat() - random.nextFloat()) * 0.1F,
                        random.nextFloat() * 0.05F,
                        (random.nextFloat() - random.nextFloat()) * 0.1F
                    )
            );
        }
    }

    @Override
    public boolean readyForShearing() {
        return isRooted.get();
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float damage) {
        var isHurt = super.hurt(damageSource, damage);

        if (!level().isClientSide && isHurt && damageSource.getEntity() != null) {
            tryHatch();
        }

        return isHurt;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            super.doPush(entity);
            return;
        }

        if (AlienPredicates.isFreeHost(this, entity)) {
            tryHatch();
        }

        if (
            // Entity is not an alien...
            !entity.getType().is(AlienEntityTypeTags.ALIENS)
                // OR entity is an ovomorph.
                || entity.getType().is(AlienEntityTypeTags.OVOMORPHS)
        ) {
            super.doPush(entity);
        }
    }

    @Override
    protected boolean canBleedAcid() {
        return !hatchManager.isHatching()
            && !hatchManager.isHatched();
    }

    @Override
    public boolean isPushedByFluid() {
        return !isRooted.get();
    }

    @Override
    public boolean isPushable() {
        return !isRooted.get();
    }

    @Override
    public boolean isPersistenceRequired() {
        if (hatchManager.isHatched()) {
            // If the ovomorph is hatched, then defer to super and no other factors.
            return super.isPersistenceRequired();
        }

        // Otherwise if super check passes or if ovomorph is not rooted, then persist the ovomorph.
        return super.isPersistenceRequired() || !isRooted.get();
    }

    @Override
    protected boolean canHeal() {
        return !hatchManager.isHatching()
            && !hatchManager.isHatched()
            && super.canHeal();
    }

    @Override
    protected boolean canAlienRideVehicle(@NotNull Entity vehicle) {
        return !isRooted.get();
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return HealthRegenConstants.OVOMORPH_HEALTH_REGEN;
    }

    public HatchManager getHatchManager() {
        return hatchManager;
    }

    public Option<HatchState> getHatchState() {
        var id = (int) hatchStateId.get();
        return Option.ofNullable(HatchState.ID_TO_HATCH_STATE_MAP.get(id));
    }

    public void setHatchState(HatchState hatchState) {
        hatchStateId.set((byte) hatchState.getId());
    }

    public OvomorphAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public VibrationSystemManager getVibrationSystemManager() {
        return vibrationSystemManager;
    }

    public static @Nullable EntityType<? extends Ovomorph> getType(AlienVariant alienVariant, boolean isRoyal) {
        if (isRoyal) {
            return switch (alienVariant) {
                case NORMAL -> AlienEntityTypes.ROYAL_OVOMORPH.get();
                case NETHER -> AlienEntityTypes.ROYAL_NETHER_OVOMORPH.get();
                case ABERRANT -> AlienEntityTypes.ROYAL_ABERRANT_OVOMORPH.get();
                case IRRADIATED -> null;
            };
        }

        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.OVOMORPH.get();
            case NETHER -> AlienEntityTypes.NETHER_OVOMORPH.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_OVOMORPH.get();
            case IRRADIATED -> null;
        };
    }
}

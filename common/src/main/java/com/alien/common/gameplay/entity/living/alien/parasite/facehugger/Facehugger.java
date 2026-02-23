package com.alien.common.gameplay.entity.living.alien.parasite.facehugger;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.parasite.Parasite;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.FacehuggerGOAP;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.EntitySenseCache;
import com.blib.api.common.entity.v1.EntitySenseCacheUser;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.goap.v1.GOAPUser;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Facehugger extends Parasite implements EntitySenseCacheUser, GOAPUser<Facehugger> {

    public static final int MAX_IDLE_TIME_IN_TICKS = 12 * 20;

    public static final int MIN_IDLE_TIME_IN_TICKS = 7 * 20;

    public static AttributeSupplier.Builder createFacehuggerAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 0f)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, 0f)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 0.15F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1.1F);
    }

    private final FacehuggerAnimationDispatcher animationDispatcher;

    private final EntitySenseCache entitySenseCache;

    public final DataAccessor<Integer> ticksUntilBored;

    public Facehugger(EntityType<? extends Facehugger> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new FacehuggerAnimationDispatcher(this);
        this.entitySenseCache = new EntitySenseCache(this, 40);
        this.ticksUntilBored = new DataAccessor<>(this, AlienDataSyncKeys.FACEHUGGER_TICKS_UNTIL_BORED.get());
    }

    @Override
    public Agent.Builder<Facehugger> blib$applyGOAPAgentProperties(Agent.Builder<Facehugger> agentBuilder) {
        return FacehuggerGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Facehugger> blib$getGOAPGraphOrNull() {
        return FacehuggerGOAP.GRAPH;
    }

    @Override
    public EntitySenseCache getEntitySenseCache() {
        return entitySenseCache;
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant, isRoyal());
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            ticksUntilBored.set(Math.max(ticksUntilBored.get() - 1, 0));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        resetTicksUntilBored();
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
        if (!entity.getType().is(AlienEntityTypeTags.ALIENS)) {
            // Facehuggers shouldn't collide with any other aliens (especially not other small aliens or ovomorphs).
            super.doPush(entity);
        }
    }

    @Override
    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.015F, 2F);
    }

    @Override
    public boolean isPersistenceRequired() {
        return super.isPersistenceRequired() || attachmentManager.isAttachedToHost();
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return 0;
    }

    public int getTicksUntilBored() {
        return ticksUntilBored.get();
    }

    public void resetTicksUntilBored() {
        this.ticksUntilBored.set(getRandom().nextIntBetweenInclusive(MIN_IDLE_TIME_IN_TICKS, MAX_IDLE_TIME_IN_TICKS));
    }

    public FacehuggerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static @Nullable EntityType<? extends Alien> getType(AlienVariant alienVariant, boolean isRoyal) {
        if (isRoyal) {
            return switch (alienVariant) {
                case NORMAL -> AlienEntityTypes.ROYAL_FACEHUGGER.get();
                case NETHER -> AlienEntityTypes.ROYAL_NETHER_FACEHUGGER.get();
                case ABERRANT -> AlienEntityTypes.ROYAL_ABERRANT_FACEHUGGER.get();
                case IRRADIATED -> null;
            };
        }

        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.FACEHUGGER.get();
            case NETHER -> AlienEntityTypes.NETHER_FACEHUGGER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_FACEHUGGER.get();
            case IRRADIATED -> null;
        };
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ExplosiveXenomorphUtil;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphPathConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.ai.BoilerGOAP;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienEntityTypes;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.entity.v1.vibration.VibrationSystemManager;
import com.blib.api.common.goap.v1.GOAPUser;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class Boiler extends Xenomorph implements GOAPUser<Boiler> {

    private static final XenomorphConfig CONFIG = XenomorphConfig.builder(XenomorphPathConfig.MEDIUM_DOOR, Boiler::getType)
        .parallelDigCount(2)
        .build();

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

    private final BoilerData boilerData;

    public Boiler(EntityType<? extends Boiler> entityType, Level level) {
        super(entityType, level, CONFIG);
        this.animationDispatcher = new BoilerAnimationDispatcher(this);
        this.vibrationSystemManager = new VibrationSystemManager(this, 2.5F, 32);
        this.boilerData = new BoilerData();
    }

    @Override
    public Agent.Builder<Boiler> blib$applyGOAPAgentProperties(Agent.Builder<Boiler> agentBuilder) {
        return BoilerGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Boiler> blib$getGOAPGraphOrNull() {
        return getActiveGOAPGraph(BoilerGOAP.GRAPH);
    }

    @Override
    public void tick() {
        super.tick();
        vibrationSystemManager.tick();
        boilerData.tick();
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        vibrationSystemManager.updateDynamicGameEventListener(biConsumer);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        var radius = 2F;
        ExplosiveXenomorphUtil.explodeWithAcid(this, radius, 3);
        triggerOnDeathMobEffects(RemovalReason.KILLED);
        discard();

        return true;
    }

    public BoilerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public VibrationSystemManager getVibrationSystemManager() {
        return vibrationSystemManager;
    }

    public BoilerData getBoilerData() {
        return boilerData;
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

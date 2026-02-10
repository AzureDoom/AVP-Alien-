package com.alien.common.gameplay.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.Boiler;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.time.v1.Cooldown;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.time.Duration;
import java.util.EnumSet;
import java.util.Objects;

public class InvestigateVibrationGoal extends Goal {

    private static final int PISSED_METER_MAX = 100;

    private static final int PISSED_METER_MIN = 0;

    private final Cooldown hissCooldownInTicks;

    private final Boiler boiler;

    private Path path;

    private int pissedMeter;

    public InvestigateVibrationGoal(Boiler boiler) {
        this.hissCooldownInTicks = Cooldown.withCooldownTime("hissCooldownInTicks", Duration.ofSeconds(3));
        this.boiler = boiler;
        this.path = null;
        this.pissedMeter = PISSED_METER_MIN;

        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (boiler.getTarget() != null) {
            return false;
        }

        var lastVibration = boiler.getVibrationSystemManager()
            .getVibrationData()
            .getCurrentVibration();

        if (lastVibration == null || !(lastVibration.entity() instanceof LivingEntity livingEntity)) {
            return false;
        }

        if (!AlienPredicates.canTarget(boiler, livingEntity)) {
            return false;
        }

        var pos = lastVibration.pos();
        this.path = boiler.getNavigation().createPath(pos.x, pos.y, pos.z, 0);

        return path != null;
    }

    @Override
    public boolean canContinueToUse() {
        return path != null
            && boiler.getTarget() == null;
    }

    @Override
    public void start() {
        if (path != null) {
            boiler.getNavigation().moveTo(path, 0.5);
            getPissed();
        }
    }

    @Override
    public void tick() {
        var lastVibration = boiler.getVibrationSystemManager()
            .getVibrationData()
            .getCurrentVibration();

        if (lastVibration != null && !Objects.equals(path.getTarget(), BlockPos.containing(lastVibration.pos()))) {
            var pos = lastVibration.pos();
            this.path = boiler.getNavigation().createPath(pos.x, pos.y, pos.z, 0);

            if (path != null) {
                boiler.getNavigation().moveTo(path, 0.5);
                getPissed();
            }

            if (pissedMeter >= PISSED_METER_MAX && lastVibration.entity() instanceof LivingEntity livingEntity) {
                boiler.setTarget(livingEntity);
                pissedMeter = PISSED_METER_MIN;
                boiler.level()
                    .playSound(
                        null,
                        boiler.getX(),
                        boiler.getY(),
                        boiler.getZ(),
                        AlienSoundEvents.ENTITY_XENOMORPH_LUNGE.get(),
                        boiler.getSoundSource(),
                        1F,
                        (boiler.getRandom().nextFloat() - boiler.getRandom().nextFloat()) * 0.2F + 1.0F
                    );
            }
        }

        if (boiler.getNavigation().isDone()) {
            this.path = null;
        }

        hissCooldownInTicks.tick();
        this.pissedMeter = Math.clamp(pissedMeter - 1, PISSED_METER_MIN, PISSED_METER_MAX);
    }

    @Override
    public void stop() {
        super.stop();
        this.path = null;
        this.pissedMeter = PISSED_METER_MIN;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    private void getPissed() {
        this.pissedMeter += Math.clamp((int) (PISSED_METER_MAX * 0.34), PISSED_METER_MIN, PISSED_METER_MAX);

        if (!hissCooldownInTicks.isActive()) {
            boiler.level()
                .playSound(
                    null,
                    boiler.getX(),
                    boiler.getY(),
                    boiler.getZ(),
                    AlienSoundEvents.ENTITY_XENOMORPH_HISS.get(),
                    boiler.getSoundSource(),
                    1F,
                    (boiler.getRandom().nextFloat() - boiler.getRandom().nextFloat()) * 0.2F + 1.0F
                );
            hissCooldownInTicks.reset();
        }
    }
}

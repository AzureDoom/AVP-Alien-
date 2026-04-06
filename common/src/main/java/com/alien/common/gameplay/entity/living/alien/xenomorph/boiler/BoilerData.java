package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler;

import com.blib.api.common.time.v1.Cooldown;

import java.time.Duration;

public class BoilerData {

    private static final int PISSED_METER_MAX = 100;

    private static final int PISSED_METER_MIN = 0;

    private static final int PISSED_INCREMENT = (int) (PISSED_METER_MAX * 0.34);

    private final Cooldown hissCooldown;

    private int pissedMeter;

    public BoilerData() {
        this.hissCooldown = Cooldown.withCooldownTime("hissCooldownInTicks", Duration.ofSeconds(3));
        this.pissedMeter = PISSED_METER_MIN;
    }

    public void tick() {
        hissCooldown.tick();
        this.pissedMeter = Math.clamp(pissedMeter - 1, PISSED_METER_MIN, PISSED_METER_MAX);
    }

    public void increasePissedMeter() {
        this.pissedMeter = Math.clamp(pissedMeter + PISSED_INCREMENT, PISSED_METER_MIN, PISSED_METER_MAX);
    }

    public boolean isMaxPissed() {
        return pissedMeter >= PISSED_METER_MAX;
    }

    public void resetPissedMeter() {
        this.pissedMeter = PISSED_METER_MIN;
    }

    public boolean tryHiss() {
        if (!hissCooldown.isActive()) {
            hissCooldown.reset();
            return true;
        }

        return false;
    }
}

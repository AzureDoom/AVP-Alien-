package com.alien.common.gameplay.entity.living.alien;

import com.alien.AlienResources;
import com.alien.common.model.lifecycle.growth.FormSizeScale;
import com.alien.common.model.lifecycle.growth.MoltPhase;
import com.alien.common.registry.FormSizeScaleRegistry;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

public class FormSizeScaleManager implements NBTSerializable {

    public static final String FORM_SCALE_PHASE_INDEX_TAG = "formScalePhaseIndex";
    public static final String FORM_SCALE_PHASE_TICKS_TAG = "formScalePhaseTicks";

    private static final ResourceLocation FORM_SIZE_SCALE_MODIFIER = AlienResources.location("form_size_scale");

    private static final int MOLT_FADE_TICKS = 20;

    private final Alien entity;

    private int phaseIndex;

    private int phaseElapsedTicks;

    private @Nullable FormSizeScale cachedData;

    private boolean dataCacheDirty;

    public FormSizeScaleManager(Alien entity) {
        this.entity = entity;
        this.phaseIndex = 0;
        this.phaseElapsedTicks = 0;
        this.dataCacheDirty = true;

        var data = getData();

        if (data != null) {
            applyScaleModifier(data);
        }
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

        var data = getData();

        if (data == null) {
            return;
        }

        if (data.isFullyMatured(phaseIndex)) {
            entity.moltAlpha.set(0F);
            return;
        }

        var currentPhase = data.phases().get(phaseIndex);
        phaseElapsedTicks++;

        if (phaseElapsedTicks >= currentPhase.totalTicks()) {
            phaseIndex++;
            phaseElapsedTicks = 0;

            applyScaleModifier(data);
            entity.moltAlpha.set(0F);
            return;
        }

        var moltAlpha = computeMoltAlpha(currentPhase);
        entity.moltAlpha.set(moltAlpha);

        if (isMolting(currentPhase)) {
            applyScaleModifier(data);
        }
    }

    public boolean hasReachedTargetScale() {
        var data = getData();

        if (data == null) {
            return true;
        }

        return data.isFullyMatured(phaseIndex);
    }

    public float getCurrentScale() {
        var data = getData();

        if (data == null) {
            return 1.0f;
        }

        if (data.isFullyMatured(phaseIndex)) {
            return data.endScale();
        }

        var currentPhase = data.phases().get(phaseIndex);

        if (!isMolting(currentPhase)) {
            return data.scaleBeforePhase(phaseIndex);
        }

        var moltElapsed = phaseElapsedTicks - currentPhase.idleTicks();
        var moltProgress = (float) moltElapsed / currentPhase.moltTicks();
        var phaseStartScale = data.scaleBeforePhase(phaseIndex);
        var phaseEndScale = data.scaleForPhase(phaseIndex);

        return phaseStartScale + (phaseEndScale - phaseStartScale) * moltProgress;
    }

    private boolean isMolting(MoltPhase phase) {
        return phaseElapsedTicks >= phase.idleTicks();
    }

    private float computeMoltAlpha(MoltPhase phase) {
        if (!isMolting(phase)) {
            var ticksUntilMolt = phase.idleTicks() - phaseElapsedTicks;

            if (ticksUntilMolt <= MOLT_FADE_TICKS) {
                return 1.0F - (float) ticksUntilMolt / MOLT_FADE_TICKS;
            }

            return 0F;
        }

        var moltElapsed = phaseElapsedTicks - phase.idleTicks();
        var moltRemaining = phase.moltTicks() - moltElapsed;

        if (moltRemaining <= MOLT_FADE_TICKS) {
            return (float) moltRemaining / MOLT_FADE_TICKS;
        }

        return 1.0F;
    }

    private void applyScaleModifier(FormSizeScale data) {
        var scaleInstance = entity.getAttribute(Attributes.SCALE);

        if (scaleInstance == null) {
            return;
        }

        var currentScale = getCurrentScale();
        var modifierValue = currentScale - 1.0;

        scaleInstance.removeModifier(FORM_SIZE_SCALE_MODIFIER);

        if (Math.abs(modifierValue) > 0.001) {
            scaleInstance.addTransientModifier(
                new AttributeModifier(FORM_SIZE_SCALE_MODIFIER, modifierValue, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            );
        }
    }

    private @Nullable FormSizeScale getData() {
        if (dataCacheDirty) {
            cachedData = FormSizeScaleRegistry.get(entity.getType());
            dataCacheDirty = false;
        }

        return cachedData;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(FORM_SCALE_PHASE_INDEX_TAG)) {
            this.phaseIndex = compoundTag.getInt(FORM_SCALE_PHASE_INDEX_TAG);
        }

        if (compoundTag.contains(FORM_SCALE_PHASE_TICKS_TAG)) {
            this.phaseElapsedTicks = compoundTag.getInt(FORM_SCALE_PHASE_TICKS_TAG);
        }

        var data = getData();

        if (data != null) {
            applyScaleModifier(data);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(FORM_SCALE_PHASE_INDEX_TAG, phaseIndex);
        compoundTag.putInt(FORM_SCALE_PHASE_TICKS_TAG, phaseElapsedTicks);
    }
}

package com.alien.common.gameplay.entity.living.alien;

import com.alien.AlienResources;
import com.alien.common.model.lifecycle.growth.FormSizeScale;
import com.alien.common.registry.FormSizeScaleRegistry;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

public class FormSizeScaleManager implements NBTSerializable {

    public static final String FORM_SCALE_ELAPSED_TICKS_TAG = "formScaleElapsedTicks";

    private static final ResourceLocation FORM_SIZE_SCALE_MODIFIER = AlienResources.location("form_size_scale");

    private final Alien entity;

    private int elapsedTicks;

    private @Nullable FormSizeScale cachedData;

    private boolean dataCacheDirty;

    public FormSizeScaleManager(Alien entity) {
        this.entity = entity;
        this.elapsedTicks = 0;
        this.dataCacheDirty = true;
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

        var data = getData();

        if (data == null) {
            return;
        }

        if (data.isFullyMatured(elapsedTicks)) {
            return;
        }

        this.elapsedTicks++;
        applyScaleModifier(data);
    }

    public boolean hasReachedTargetScale() {
        var data = getData();

        if (data == null) {
            return true;
        }

        return data.isFullyMatured(elapsedTicks);
    }

    public float getCurrentScale() {
        var data = getData();

        if (data == null) {
            return 1.0f;
        }

        return data.computeScale(elapsedTicks);
    }

    private void applyScaleModifier(FormSizeScale data) {
        var scaleInstance = entity.getAttribute(Attributes.SCALE);

        if (scaleInstance == null) {
            return;
        }

        var currentScale = data.computeScale(elapsedTicks);
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
        if (compoundTag.contains(FORM_SCALE_ELAPSED_TICKS_TAG)) {
            this.elapsedTicks = compoundTag.getInt(FORM_SCALE_ELAPSED_TICKS_TAG);
        }

        // Reapply the scale modifier immediately on load so the entity
        // has the correct scale from the first tick.
        var data = getData();

        if (data != null) {
            applyScaleModifier(data);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(FORM_SCALE_ELAPSED_TICKS_TAG, elapsedTicks);
    }
}

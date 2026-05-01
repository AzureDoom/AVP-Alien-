package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.alien.common.gameplay.entity.living.alien.GrowthManager;
import com.alien.common.model.lifecycle.growth.CocooningConfig;
import com.blib.api.common.entity.v1.EntityTransitionUtil;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CocoonManager implements NBTSerializable {

    public static final String COCOON_STATE_TAG = "cocoonState";
    public static final String COCOON_TARGET_TYPE_TAG = "cocoonTargetType";
    public static final String COCOON_SOURCE_TIME_TAG = "cocoonSourceTimeInTicks";
    public static final String COCOON_DESTINATION_TIME_TAG = "cocoonDestinationTimeInTicks";
    public static final String COCOON_ELAPSED_TICKS_TAG = "cocoonElapsedTicks";

    private static final int EMERGE_TIME_IN_TICKS = 20;

    private static final int TRANSITION_RETRY_TIME_IN_TICKS = 10 * 20;

    private final Xenomorph xenomorph;

    private @Nullable EntityType<?> targetType;

    private int sourceTimeInTicks;

    private int destinationTimeInTicks;

    private int elapsedTicks;

    private int retryTicks;

    public CocoonManager(Xenomorph xenomorph) {
        this.xenomorph = xenomorph;
        this.sourceTimeInTicks = CocooningConfig.DEFAULT_SOURCE_TIME_IN_TICKS;
        this.destinationTimeInTicks = CocooningConfig.DEFAULT_DESTINATION_TIME_IN_TICKS;
    }

    public void prepare(EntityType<?> targetType, CocooningConfig config) {
        if (getState().shouldRunCocoonAction()) {
            return;
        }

        this.targetType = targetType;
        this.sourceTimeInTicks = Math.max(config.sourceTimeInTicks(), 1);
        this.destinationTimeInTicks = Math.max(config.destinationTimeInTicks(), 1);
        this.elapsedTicks = 0;
        this.retryTicks = 0;
        setState(CocoonState.PENDING);
    }

    public boolean shouldRunCocoonAction() {
        return getState().shouldRunCocoonAction();
    }

    public boolean isLocked() {
        return getState().isLocked();
    }

    public CocoonState getState() {
        return xenomorph.cocoonState.get();
    }

    public int getAnimationId() {
        return xenomorph.cocoonAnimationId.get();
    }

    public boolean performCocoonTick() {
        stopMovementAndTargeting();

        return switch (getState()) {
            case NONE -> false;
            case PENDING -> {
                beginSourceCocooning();
                yield true;
            }
            case SOURCE_COCOONING -> tickSourceCocooning();
            case DESTINATION_COCOONING -> tickDestinationCocooning();
            case EMERGING -> tickEmerging();
        };
    }

    public void maintainLockedState() {
        if (isLocked()) {
            stopMovementAndTargeting();
        }
    }

    public void beginDestinationCocooning(int destinationTimeInTicks) {
        this.targetType = null;
        this.sourceTimeInTicks = 1;
        this.destinationTimeInTicks = Math.max(destinationTimeInTicks, 1);
        this.elapsedTicks = 0;
        this.retryTicks = 0;
        setState(CocoonState.DESTINATION_COCOONING);
        incrementAnimationId();
        stopMovementAndTargeting();
    }

    private void beginSourceCocooning() {
        this.elapsedTicks = 0;
        setState(CocoonState.SOURCE_COCOONING);
        incrementAnimationId();
    }

    private boolean tickSourceCocooning() {
        elapsedTicks++;

        if (elapsedTicks < sourceTimeInTicks) {
            return true;
        }

        if (retryTicks > 0) {
            retryTicks--;
            return true;
        }

        return transitionToDestination();
    }

    private boolean transitionToDestination() {
        if (targetType == null) {
            clear();
            return false;
        }

        var transitionResult = EntityTransitionUtil.transitionInto(
            xenomorph,
            targetType,
            GrowthManager.TRANSITION_NBT_KEY_BLACKLIST,
            true
        );

        if (transitionResult instanceof EntityTransitionUtil.EntityTransitionResult.Success<?> success) {
            var newEntity = success.newEntity();

            if (newEntity instanceof Xenomorph newXenomorph) {
                newXenomorph.getCocoonManager().beginDestinationCocooning(destinationTimeInTicks);
            }

            return false;
        }

        if (transitionResult instanceof EntityTransitionUtil.EntityTransitionResult.Obstructed) {
            retryTicks = TRANSITION_RETRY_TIME_IN_TICKS;
            return true;
        }

        clear();
        return false;
    }

    private boolean tickDestinationCocooning() {
        elapsedTicks++;

        if (elapsedTicks < destinationTimeInTicks) {
            return true;
        }

        elapsedTicks = 0;
        setState(CocoonState.EMERGING);
        incrementAnimationId();
        return true;
    }

    private boolean tickEmerging() {
        elapsedTicks++;

        if (elapsedTicks < EMERGE_TIME_IN_TICKS) {
            return true;
        }

        clear();
        return false;
    }

    private void stopMovementAndTargeting() {
        xenomorph.setDeltaMovement(Vec3.ZERO);
        xenomorph.getNavigation().stop();
        xenomorph.setTarget(null);
    }

    private void clear() {
        this.targetType = null;
        this.elapsedTicks = 0;
        this.retryTicks = 0;
        setState(CocoonState.NONE);
    }

    private void setState(CocoonState state) {
        xenomorph.cocoonState.set(state);
    }

    private void incrementAnimationId() {
        xenomorph.cocoonAnimationId.set(xenomorph.cocoonAnimationId.get() + 1);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(COCOON_STATE_TAG)) {
            setState(CocoonState.valueOf(compoundTag.getString(COCOON_STATE_TAG)));
        }

        if (compoundTag.contains(COCOON_TARGET_TYPE_TAG)) {
            this.targetType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(compoundTag.getString(COCOON_TARGET_TYPE_TAG)));
        }

        if (compoundTag.contains(COCOON_SOURCE_TIME_TAG)) {
            this.sourceTimeInTicks = compoundTag.getInt(COCOON_SOURCE_TIME_TAG);
        }

        if (compoundTag.contains(COCOON_DESTINATION_TIME_TAG)) {
            this.destinationTimeInTicks = compoundTag.getInt(COCOON_DESTINATION_TIME_TAG);
        }

        if (compoundTag.contains(COCOON_ELAPSED_TICKS_TAG)) {
            this.elapsedTicks = compoundTag.getInt(COCOON_ELAPSED_TICKS_TAG);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        var state = getState();

        if (state == CocoonState.NONE) {
            return;
        }

        compoundTag.putString(COCOON_STATE_TAG, state.name());

        if (targetType != null) {
            compoundTag.putString(COCOON_TARGET_TYPE_TAG, BuiltInRegistries.ENTITY_TYPE.getKey(targetType).toString());
        }

        compoundTag.putInt(COCOON_SOURCE_TIME_TAG, sourceTimeInTicks);
        compoundTag.putInt(COCOON_DESTINATION_TIME_TAG, destinationTimeInTicks);
        compoundTag.putInt(COCOON_ELAPSED_TICKS_TAG, elapsedTicks);
    }
}

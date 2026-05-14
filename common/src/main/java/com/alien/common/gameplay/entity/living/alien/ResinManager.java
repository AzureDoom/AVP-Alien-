package com.alien.common.gameplay.entity.living.alien;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.hive2.spawning.HiveLocationSpawnGate;
import com.alien.common.gameplay.level.gameevent.listener.ResinSpreadListener;
import com.alien.common.model.resin.ResinData;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import com.just.core.functional.option.Option;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ResinManager implements GameEventListener.Provider<ResinSpreadListener>, NBTSerializable {

    private static final String NBT_LAST_SPREAD_TICK = "lastSpreadTick";

    private static final int SPREAD_COOLDOWN_IN_TICKS = 15 * 20;

    private static final int SPREAD_CHARGE = 16;

    private final Alien alien;

    private final DynamicGameEventListener<ResinSpreadListener> dynamicResinSpreadListener;

    private final ResinSpreadListener resinSpreadListener;

    private final ResinData resinData;

    private long lastSpreadTick;

    private int ticksSinceAttemptedNodePlacement = 0;

    public ResinManager(Alien alien) {
        this.alien = alien;
        this.resinData = new ResinData(0, SPREAD_CHARGE, 0, 0);
        var positionSource = new EntityPositionSource(alien, 0F);
        var spreadType = new ResinSpreadListener.SpreaderType.Entity(alien);
        this.resinSpreadListener = new ResinSpreadListener(positionSource, spreadType);
        this.dynamicResinSpreadListener = new DynamicGameEventListener<>(resinSpreadListener);
    }

    @Override
    public @NotNull ResinSpreadListener getListener() {
        return resinSpreadListener;
    }

    public void tick() {
        if (alien.level().isClientSide) {
            return;
        }

        ticksSinceAttemptedNodePlacement = Math.max(0, ticksSinceAttemptedNodePlacement - 1);
    }

    public boolean canSpreadResin() {
        return alien.tickCount - lastSpreadTick >= SPREAD_COOLDOWN_IN_TICKS
            && !isNodePlacementOnCooldown()
            && canSpreadResinAtAlienPosition();
    }

    public void spreadResin() {
        // Set the charge so the nearest resin node listener can consume it.
        resinData.setResin(SPREAD_CHARGE);

        var alienVariantType = AlienVariantTypes.getFor(alien);

        // Signal to the nearest resin node that we want to spread resin.
        alien.gameEvent(alienVariantType.resinSpreadEvent());

        lastSpreadTick = alien.tickCount;

        // If the alien still has resin even after signalling a resin spread event, that means there was no resin node
        // to intercept the event. So we try to place a resin node down here.
        if (resinData.resin() > 0) {
            var level = alien.level();
            // Try and find a suitable resin node block location.
            var suitableResinNodeBlockPosOption = findSuitableResinNodeBlockPos(level, alienVariantType.resinReplaceableTag());

            if (suitableResinNodeBlockPosOption.isNone()) {
                // Could not find a suitable resin node block position, so reset the node place cooldown and return.
                ticksSinceAttemptedNodePlacement = 20 * 10;
                resinData.setResin(0);
                return;
            }

            // If the resin holder still has more resin, then we place a resin node manually.
            var resinNodeBlockState = alienVariantType.resinNode().get().defaultBlockState();
            // Place the resin node block at the suitable position.
            level.setBlockAndUpdate(suitableResinNodeBlockPosOption.unwrap(), resinNodeBlockState);
            resinData.setResin(0);
        }
    }

    public ResinData resinData() {
        return resinData;
    }

    private boolean isNodePlacementOnCooldown() {
        return ticksSinceAttemptedNodePlacement > 0;
    }

    private boolean canSpreadResinAtAlienPosition() {
        // Alien must not have an attack target...
        if (alien.getTarget() != null) {
            return false;
        }
        if (alien.isUnderWater()) {
            return false;
        }
        // AND alien must have not been hurt for more than 10 seconds...
        if (alien.tickCount <= alien.getLastHurtTimeInTicks() + (10 * 20)) {
            return false;
        }
        return isInsideHiveForResinSpread();
    }

    /**
     * "Is in a hive that's calm enough to spread resin." Under hive2, "in a hive" means inside a claimed chunk of any
     * location; the angry check is the location's boss-bar angry state.
     */
    private boolean isInsideHiveForResinSpread() {
        var location = HiveLocationSpawnGate.locationContaining(alien.level(), alien.blockPosition());
        if (location == null) {
            return false;
        }
        var bossBar = location.bossBar();
        return bossBar == null || !bossBar.isAngry();
    }

    private Option<BlockPos> findSuitableResinNodeBlockPos(Level level, TagKey<Block> replaceableTagKey) {
        var origin = alien.blockPosition();
        var below = origin.below();
        var belowState = level.getBlockState(below);

        if (belowState.is(replaceableTagKey)) {
            return Option.some(below);
        }

        // Use mutable block pos for memory efficiency.
        var targetMutablePos = new BlockPos.MutableBlockPos();
        var belowTargetMutablePos = new BlockPos.MutableBlockPos();

        var radius = 2;

        for (var r = 0; r <= radius; r++) {
            for (var dx = -r; dx <= r; dx++) {
                var dz = r - Math.abs(dx);

                for (var sign : new int[] { 1, -1 }) {
                    var actualDz = dz * sign;

                    for (var dy = -1; dy <= 1; dy++) {
                        targetMutablePos.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + actualDz);
                        belowTargetMutablePos.set(targetMutablePos.getX(), targetMutablePos.getY() - 1, targetMutablePos.getZ());

                        var targetStateToReplace = level.getBlockState(targetMutablePos);

                        if (
                            // If the target state is air OR can be replaced...
                            (targetStateToReplace.isAir()
                                || targetStateToReplace.canBeReplaced())
                                // AND if the supporting state beneath the target state is a solid render...
                                && level.getBlockState(belowTargetMutablePos).isSolidRender(level, belowTargetMutablePos)
                        ) {
                            // Then return the target state pos.
                            return Option.some(targetMutablePos.immutable());
                        }
                    }
                }
            }
        }

        return Option.none();
    }

    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        if (alien.level() instanceof ServerLevel serverLevel) {
            biConsumer.accept(dynamicResinSpreadListener, serverLevel);
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_LAST_SPREAD_TICK)) {
            this.lastSpreadTick = compoundTag.getLong(NBT_LAST_SPREAD_TICK);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putLong(NBT_LAST_SPREAD_TICK, lastSpreadTick);
    }
}

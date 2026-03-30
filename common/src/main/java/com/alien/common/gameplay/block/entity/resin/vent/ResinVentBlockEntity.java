package com.alien.common.gameplay.block.entity.resin.vent;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.hive.Hive;
import com.alien.common.gameplay.hive.HiveRegistry;
import com.alien.common.gameplay.level.gameevent.listener.CryForHelpListener;
import com.alien.common.registry.init.AlienBlockEntityTypes;
import com.blib.api.common.time.v1.Cooldown;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;

public class ResinVentBlockEntity extends BlockEntity implements GameEventListener.Provider<CryForHelpListener> {

    private final Cooldown alienSpawnCooldown;

    private final CryForHelpListener cryForHelpListener;

    private @Nullable Hive hive;

    public ResinVentBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AlienBlockEntityTypes.RESIN_VENT.get(), blockPos, blockState);

        var positionSource = new BlockPositionSource(blockPos);

        this.alienSpawnCooldown = Cooldown.withCooldownTime("spawnAlienCooldown", Duration.ofSeconds(3));
        this.cryForHelpListener = new CryForHelpListener(positionSource);
    }

    public @Nullable Hive getHive() {
        return hive;
    }

    public Cooldown getAlienSpawnCooldown() {
        return alienSpawnCooldown;
    }

    public void setHive(@Nullable Hive hive) {
        this.hive = hive;
    }

    public static void serverTick(Level level, BlockPos ventPos, BlockState blockState, ResinVentBlockEntity resinVentBlockEntity) {
        resinVentBlockEntity.getAlienSpawnCooldown().tick();

        var hive = resinVentBlockEntity.getHive();

        if (hive != null) {
            if (
                // If the hive is not alive...
                !hive.isAlive()
                    // OR if the hive has moved such that the vent pos is no longer in range...
                    || !hive.getSpaceManager().isBlockPosWithinHive(ventPos)
            ) {
                // ...then we remove the hive reference as we can no longer use it.
                resinVentBlockEntity.setHive(null);
                hive.getVentManager().removeVent(ventPos);
                return;
            }

            hive.getVentManager().addVent(ventPos);
            return;
        }

        if (level.getGameTime() % 20 == 0) {
            var ventVariantTypeOption = AlienVariantTypes.getFor(blockState);

            if (ventVariantTypeOption.isNone()) {
                return;
            }

            var ventAlienVariant = ventVariantTypeOption.unwrap().variant();

            var nearestHive = HiveRegistry.INSTANCE.findNearestHive(
                ventPos,
                level.dimension(),
                nearestCandidate -> Objects.equals(nearestCandidate.getVariant(), ventAlienVariant)
            );

            if (nearestHive != null && nearestHive.isAlive() && nearestHive.getSpaceManager().isBlockPosWithinHive(ventPos)) {
                resinVentBlockEntity.setHive(nearestHive);
            }
        }
    }

    @Override
    public @NotNull CryForHelpListener getListener() {
        return cryForHelpListener;
    }
}

package com.alien.common.gameplay.block.entity.resin.vent;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.level.gameevent.listener.CryForHelpListener;
import com.alien.common.registry.init.AlienBlockEntityTypes;
import com.blib.api.common.time.v1.Cooldown;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

/**
 * Vent block entity. In hive2 this binds to a {@link HiveLocation} (not a faction) — the location's chunk-claim set is
 * the source of truth for "which hive owns this vent." Each tick we re-resolve which location currently owns this
 * vent's chunk. If that location's variant matches the vent's variant, the vent registers itself with the location's
 * {@link com.alien.common.gameplay.hive2.vent.HiveVentManager} so AI queries can find it.
 */
public class ResinVentBlockEntity extends BlockEntity implements GameEventListener.Provider<CryForHelpListener> {

    private final Cooldown alienSpawnCooldown;

    private final CryForHelpListener cryForHelpListener;

    private @Nullable HiveLocationId boundLocationId;

    public ResinVentBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AlienBlockEntityTypes.RESIN_VENT.get(), blockPos, blockState);

        var positionSource = new BlockPositionSource(blockPos);

        this.alienSpawnCooldown = Cooldown.withCooldownTime("spawnAlienCooldown", Duration.ofSeconds(3));
        this.cryForHelpListener = new CryForHelpListener(positionSource);
    }

    public Cooldown getAlienSpawnCooldown() {
        return alienSpawnCooldown;
    }

    public @Nullable HiveLocationId getBoundLocationId() {
        return boundLocationId;
    }

    public @Nullable HiveLocation getBoundLocation() {
        return boundLocationId == null ? null : HiveLocationRegistry.INSTANCE.get(boundLocationId);
    }

    public static void serverTick(Level level, BlockPos ventPos, BlockState blockState, ResinVentBlockEntity vent) {
        vent.alienSpawnCooldown.tick();

        var ventVariantTypeOption = AlienVariantTypes.getFor(blockState);
        if (ventVariantTypeOption.isNone()) {
            return;
        }
        var ventVariant = ventVariantTypeOption.unwrap().variant();

        // Re-resolve every tick: the chunk's owning location may have changed (claim transfer or death).
        var owningLocation = HiveLocationRegistry.INSTANCE.getByChunk(level.dimension(), new ChunkPos(ventPos));

        if (owningLocation == null) {
            // No location owns this chunk anymore. Drop the binding.
            if (vent.boundLocationId != null) {
                vent.boundLocationId = null;
            }
            return;
        }

        // The owning location's lineage variant must match the vent's variant — otherwise this vent isn't part of
        // that hive's network (e.g., a normal-variant vent inside a normal lineage's territory after the chunk was
        // contested away from an aberrant lineage).
        var lineageFaction = com.alien.Alien.MOD.factions().get(owningLocation.lineageFactionId());
        if (
            lineageFaction == null
                || !(lineageFaction.data() instanceof com.alien.common.gameplay.hive2.faction.LineageFactionData lineage)
                || lineage.variant() != ventVariant
        ) {
            // Variant mismatch — disown.
            if (vent.boundLocationId != null) {
                vent.boundLocationId = null;
            }
            return;
        }

        // Bound to this location. Register the vent in its vent manager.
        vent.boundLocationId = owningLocation.id();
        owningLocation.ventManager().addVent(ventPos);
    }

    @Override
    public @NotNull CryForHelpListener getListener() {
        return cryForHelpListener;
    }
}

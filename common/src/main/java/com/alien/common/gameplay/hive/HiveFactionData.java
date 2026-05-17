package com.alien.common.gameplay.hive;

import com.alien.Alien;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import com.blib.api.common.faction.v1.FactionData;
import com.blib.api.common.nbt.v1.CompoundTagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Minimal legacy-hive faction data. Kept around solely to let the Phase 12 {@code OldHiveMigrator} read existing
 * worlds; the live behavior (leadership, reserves, space, vents) is no longer wired through this class. Once a world
 * has been migrated, {@code OldHiveMigrator.run} drops the {@code avp_alien:hive/*} factions entirely; on the
 * subsequent server start there are no more {@link HiveFactionData} instances to load.
 * <p>
 * Only the fields the migrator needs are loaded:
 * <ul>
 * <li>{@code variant} — drives the new lineage's variant</li>
 * <li>{@code dimension} — drives the new lineage's + location's dimension</li>
 * <li>{@code centerPos} — becomes the new location's center</li>
 * <li>{@code leaderId} — becomes the new lineage's founder</li>
 * <li>{@code reserves} — copied verbatim into the new location's local reserves</li>
 * </ul>
 */
public class HiveFactionData extends FactionData {

    private static final String NBT_CENTER_POS = "CenterPos";

    private static final String NBT_DIMENSION = "Dimension";

    private static final String NBT_VARIANT_ID = "VariantId";

    private static final String NBT_LEADER_ID = "HiveLeaderId";

    private static final String NBT_RESERVES_ROOT = "hiveMemberReserves";

    private static final AlienVariant DEFAULT_VARIANT = AlienVariant.NORMAL;

    private final EntityReserves reserves;

    private BlockPos centerPos;

    private ResourceKey<Level> dimension;

    private AlienVariant variant;

    private @Nullable UUID leaderId;

    public HiveFactionData() {
        this.reserves = new EntityReserves();
        this.centerPos = BlockPos.ZERO;
        this.dimension = Level.OVERWORLD;
        this.variant = DEFAULT_VARIANT;
        this.leaderId = null;
    }

    public BlockPos getCenterPos() {
        return centerPos;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public AlienVariant getVariant() {
        return variant;
    }

    public @Nullable UUID getLeaderId() {
        return leaderId;
    }

    public EntityReserves getReserves() {
        return reserves;
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains(NBT_CENTER_POS)) {
            var components = tag.getIntArray(NBT_CENTER_POS);
            if (components.length >= 3) {
                this.centerPos = new BlockPos(components[0], components[1], components[2]);
            }
        }

        if (tag.contains(NBT_DIMENSION)) {
            this.dimension = ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION,
                net.minecraft.resources.ResourceLocation.parse(tag.getString(NBT_DIMENSION))
            );
        }

        if (tag.contains(NBT_VARIANT_ID)) {
            this.variant = AlienVariant.getById(tag.getByte(NBT_VARIANT_ID)).unwrapOr(DEFAULT_VARIANT);
        }

        this.leaderId = CompoundTagUtil.getUUIDOrNull(tag, NBT_LEADER_ID);

        if (tag.contains(NBT_RESERVES_ROOT)) {
            EntityReserves.CODEC.decode(BLibCodecs.Schema.NBT, tag.getCompound(NBT_RESERVES_ROOT))
                .inspectErr(failure -> Alien.LOGGER.error("Failed to load legacy hive reserves: {}", failure))
                .ifOk(loaded -> reserves.putAll(loaded.getBackingMap()));
        }
    }

    @Override
    public void save(CompoundTag tag) {
        // Read-only: the migrator drops the faction after running, so we never need to persist anything new here.
        // Keep an empty save() so BLib's faction tick doesn't NPE on dirty flush.
    }
}

package com.alien.common.gameplay.hive2.faction;

import com.blib.api.common.faction.v1.FactionData;
import net.minecraft.nbt.CompoundTag;

/**
 * Per-{@link com.alien.common.gameplay.hive2.location.HiveLocation} faction. The faction id equals the location's
 * {@link com.alien.common.gameplay.hive2.id.HiveLocationId} ResourceLocation, so
 * {@code factions().get(location.id().value())} returns the right faction.
 * <p>
 * Membership encodes "born here" — aliens spawned from this location's machinery (founder queen, post-transition
 * xenomorphs in claimed chunks, in-place reinforcements, etc.). The whole point of this tier is that BLib's persistent
 * UUID-keyed membership survives chunk unloads, so location death can be keyed off real membership rather than the
 * load-state-sensitive {@code loadedMembersByType} on {@link com.alien.common.gameplay.hive2.location.HiveLocation}.
 * <p>
 * Stateless. All location data (claimed chunks, reserves, leadership, no-contact counter) lives on
 * {@link com.alien.common.gameplay.hive2.location.HiveLocation}. The parent lineage id is derived on demand from
 * {@link com.alien.common.gameplay.hive2.location.HiveLocationRegistry#get} so absorption and civil-war transfers don't
 * have to keep a denormalized copy in sync.
 */
public class LocationFactionData extends FactionData {

    @Override
    public void load(CompoundTag tag) {}

    @Override
    public void save(CompoundTag tag) {}
}

package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.blib.api.common.faction.v1.FactionData;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * Per-{@link com.alien.common.gameplay.hive2.location.HiveLocation} faction. The faction id equals the location's
 * {@link HiveLocationId} ResourceLocation, so {@code factions().get(location.id().value())} returns the right faction.
 * <p>
 * This tier owns the BLib chunk claims for the location and also tracks "born here" membership — aliens spawned from this
 * location's machinery (founder queen, post-transition xenomorphs in claimed chunks, in-place reinforcements, etc.).
 * BLib's persistent UUID-keyed membership survives chunk unloads, so location death can be keyed off real membership
 * rather than the load-state-sensitive {@code loadedMembersByType} on
 * {@link com.alien.common.gameplay.hive2.location.HiveLocation}.
 * <p>
 * The {@link #locationId} backref lets the reactive {@link #onMemberAdded(FactionMember, Entity)} guard look up the
 * owning location → parent lineage → variant chain, and reject joins from the wrong variant or from aliens not in the
 * parent lineage.
 */
public class LocationFactionData extends FactionData {

    private static final String NBT_LOCATION_ID = "LocationId";

    private @Nullable HiveLocationId locationId;

    public LocationFactionData() {
        this.locationId = null;
    }

    public @Nullable HiveLocationId locationId() {
        return locationId;
    }

    public void setLocationId(HiveLocationId locationId) {
        this.locationId = locationId;
        markDirty();
    }

    @Override
    public void onMemberAdded(FactionMember member, Entity entity) {
        if (locationId == null) {
            return;
        }
        var location = HiveLocationRegistry.INSTANCE.get(locationId);
        if (location == null) {
            return;
        }

        var lineageFaction = Alien.MOD.factions().get(location.lineageFactionId());
        if (lineageFaction == null || !(lineageFaction.data() instanceof LineageFactionData lineage)) {
            return;
        }

        var mismatch = !FactionVariantPolicy.variantMatches(entity, lineage.variant());
        var notInLineage = !lineageFaction.membership().hasMember(member);

        if (mismatch || notInLineage) {
            Alien.LOGGER.warn(
                "Hive2: evicting {} from location faction {} — variantMatch={}, inParentLineage={} (lineage={}, variant={})",
                entity.getUUID(),
                locationId,
                !mismatch,
                !notInLineage,
                location.lineageFactionId(),
                lineage.variant()
            );
            var locationFaction = Alien.MOD.factions().get(locationId.value());
            if (locationFaction != null) {
                locationFaction.membership().removeMember(member);
            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains(NBT_LOCATION_ID)) {
            this.locationId = HiveLocationId.of(ResourceLocation.parse(tag.getString(NBT_LOCATION_ID)));
        }
    }

    @Override
    public void save(CompoundTag tag) {
        if (locationId != null) {
            tag.putString(NBT_LOCATION_ID, locationId.value().toString());
        }
    }
}

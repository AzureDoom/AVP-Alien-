package com.alien.common.gameplay.hive.faction;

import net.minecraft.nbt.CompoundTag;

/**
 * The recorded cause of a lineage faction's removal. Sealed so we can add cases later without breaking exhaustive
 * switches.
 * <p>
 * See {@code HIVE_REDESIGN_01_FACTIONS.md} § 2 and {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md}.
 */
public sealed interface LineageRemovalReason {

    String NBT_KIND = "Kind";

    String NBT_REASON = "Reason";

    String typeKind();

    void writeBody(CompoundTag tag);

    static LineageRemovalReason load(CompoundTag tag) {
        return switch (tag.getString(NBT_KIND)) {
            case "no_locations_remain" -> new NoLocationsRemain();
            case "absorbed_by" -> new NoLocationsRemain();
            case "civil_war" -> new NoLocationsRemain();
            case "admin_removed" -> new AdminRemoved(tag.getString(NBT_REASON));
            default -> new NoLocationsRemain();
        };
    }

    static CompoundTag save(LineageRemovalReason reason) {
        var tag = new CompoundTag();
        tag.putString(NBT_KIND, reason.typeKind());
        reason.writeBody(tag);
        return tag;
    }

    record NoLocationsRemain() implements LineageRemovalReason {

        @Override
        public String typeKind() {
            return "no_locations_remain";
        }

        @Override
        public void writeBody(CompoundTag tag) {}
    }

    record AdminRemoved(String reason) implements LineageRemovalReason {

        @Override
        public String typeKind() {
            return "admin_removed";
        }

        @Override
        public void writeBody(CompoundTag tag) {
            tag.putString(NBT_REASON, reason);
        }
    }
}

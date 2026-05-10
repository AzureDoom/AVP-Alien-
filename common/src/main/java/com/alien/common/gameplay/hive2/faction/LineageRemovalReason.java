package com.alien.common.gameplay.hive2.faction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The recorded cause of a lineage faction's removal. Sealed so we can add cases later without breaking exhaustive
 * switches.
 * <p>
 * See {@code HIVE_REDESIGN_01_FACTIONS.md} § 2 and {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md}.
 */
public sealed interface LineageRemovalReason {

    String NBT_KIND = "Kind";

    String NBT_ABSORBER = "AbsorberLineageId";

    String NBT_SUCCESSORS = "SuccessorLineageIds";

    String NBT_REASON = "Reason";

    String typeKind();

    void writeBody(CompoundTag tag);

    static LineageRemovalReason load(CompoundTag tag) {
        return switch (tag.getString(NBT_KIND)) {
            case "no_locations_remain" -> new NoLocationsRemain();
            case "absorbed_by" -> new AbsorbedBy(ResourceLocation.parse(tag.getString(NBT_ABSORBER)));
            case "civil_war" -> {
                var successors = new LinkedHashSet<ResourceLocation>();
                if (tag.contains(NBT_SUCCESSORS)) {
                    var listTag = tag.getList(NBT_SUCCESSORS, Tag.TAG_STRING);
                    for (var i = 0; i < listTag.size(); i++) {
                        successors.add(ResourceLocation.parse(listTag.getString(i)));
                    }
                }
                yield new CivilWar(successors);
            }
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

    record AbsorbedBy(ResourceLocation absorberLineageId) implements LineageRemovalReason {

        @Override
        public String typeKind() {
            return "absorbed_by";
        }

        @Override
        public void writeBody(CompoundTag tag) {
            tag.putString(NBT_ABSORBER, absorberLineageId.toString());
        }
    }

    record CivilWar(Set<ResourceLocation> successorLineageIds) implements LineageRemovalReason {

        @Override
        public String typeKind() {
            return "civil_war";
        }

        @Override
        public void writeBody(CompoundTag tag) {
            var listTag = new ListTag();
            for (var id : successorLineageIds) {
                listTag.add(StringTag.valueOf(id.toString()));
            }
            tag.put(NBT_SUCCESSORS, listTag);
        }
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

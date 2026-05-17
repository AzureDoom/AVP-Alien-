package com.alien.common.gameplay.hive.location;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

/**
 * The recorded cause of a hive location's removal. Used to gate downstream effects (advancement firing, biomass
 * refunds) — see HIVE_REDESIGN_03_LOCATIONS § 10.
 */
public sealed interface HiveLocationRemovalReason {

    String NBT_KIND = "Kind";

    String NBT_KILLER = "Killer";

    String NBT_DESTINATION = "Destination";

    String NBT_REASON = "Reason";

    String typeKind();

    void writeBody(CompoundTag tag);

    static HiveLocationRemovalReason load(CompoundTag tag) {
        return switch (tag.getString(NBT_KIND)) {
            case "natural_decay" -> new NaturalDecay();
            case "killed_by_player" -> new KilledByPlayer(tag.getUUID(NBT_KILLER));
            case "migrated" -> new Migrated(ResourceLocation.parse(tag.getString(NBT_DESTINATION)));
            case "contest_lost" -> new ContestLost();
            case "admin_removed" -> new AdminRemoved(tag.getString(NBT_REASON));
            default -> new NaturalDecay();
        };
    }

    static CompoundTag save(HiveLocationRemovalReason reason) {
        var tag = new CompoundTag();
        tag.putString(NBT_KIND, reason.typeKind());
        reason.writeBody(tag);
        return tag;
    }

    record NaturalDecay() implements HiveLocationRemovalReason {

        @Override
        public String typeKind() {
            return "natural_decay";
        }

        @Override
        public void writeBody(CompoundTag tag) {}
    }

    record KilledByPlayer(UUID killerId) implements HiveLocationRemovalReason {

        @Override
        public String typeKind() {
            return "killed_by_player";
        }

        @Override
        public void writeBody(CompoundTag tag) {
            tag.putUUID(NBT_KILLER, killerId);
        }
    }

    record Migrated(ResourceLocation destinationLocationId) implements HiveLocationRemovalReason {

        @Override
        public String typeKind() {
            return "migrated";
        }

        @Override
        public void writeBody(CompoundTag tag) {
            tag.putString(NBT_DESTINATION, destinationLocationId.toString());
        }
    }

    record ContestLost() implements HiveLocationRemovalReason {

        @Override
        public String typeKind() {
            return "contest_lost";
        }

        @Override
        public void writeBody(CompoundTag tag) {}
    }

    record AdminRemoved(String reason) implements HiveLocationRemovalReason {

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

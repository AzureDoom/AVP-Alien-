package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.HiveLocationIds;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Carries hive2 faction membership (lineage + location tiers) across an
 * {@link com.blib.api.common.entity.v1.EntityTransitionUtil#transitionInto entity transition}, which mints a fresh
 * UUID and would otherwise silently drop the molting alien from every faction it belonged to.
 * <p>
 * Two-phase: snapshot the old entity's faction ids <em>before</em> calling {@code transitionInto}, then apply to the
 * new entity after a {@code Success} result. The snapshot is needed because {@code transitionInto} discards the old
 * entity internally before returning, which BLib treats as a member removal — so by the time the caller sees the
 * {@code Success}, the old UUID is no longer in any faction.
 * <pre>{@code
 * var snapshot = FactionMembershipTransfer.snapshot(oldEntity);
 * var result = EntityTransitionUtil.transitionInto(oldEntity, newType);
 * if (result instanceof EntityTransitionResult.Success<?> success) {
 *     FactionMembershipTransfer.apply(snapshot, success.newEntity());
 * }
 * }</pre>
 */
public final class FactionMembershipTransfer {

    private FactionMembershipTransfer() {}

    /** Returns the set of lineage + location faction ids the entity is currently a member of. Order is insertion-stable. */
    public static Set<ResourceLocation> snapshot(Entity entity) {
        var snapshot = new LinkedHashSet<ResourceLocation>();
        for (var factionId : Alien.MOD.factions().getFactionIds(entity.getUUID())) {
            if (LineageIds.isLineageId(factionId) || HiveLocationIds.isHiveLocationId(factionId)) {
                snapshot.add(factionId);
            }
        }
        return snapshot;
    }

    /** Adds {@code newEntity} to every faction in the snapshot it isn't already a member of. */
    public static void apply(Set<ResourceLocation> snapshot, Entity newEntity) {
        var factions = Alien.MOD.factions();
        var member = FactionMember.entity(newEntity);
        for (var factionId : snapshot) {
            var faction = factions.get(factionId);
            if (faction != null && !faction.membership().hasMember(member)) {
                faction.membership().addEntity(newEntity);
            }
        }
    }
}

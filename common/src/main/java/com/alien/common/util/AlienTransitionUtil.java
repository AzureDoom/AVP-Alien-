package com.alien.common.util;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.hive2.faction.FactionMembershipTransfer;
import com.alien.common.gameplay.hive2.faction.LocationMembership;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.entity.v1.EntityTransitionUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

public class AlienTransitionUtil {

    public static <T extends Alien> AlienTransitionResult transitionIntoVariant(T alien, AlienVariant alienVariant) {
        var level = alien.level();

        if (level.isClientSide) {
            // Can't create entities client-side, so return.
            return AlienTransitionResult.ClientSide.INSTANCE;
        }

        if (alien.getVariant() == alienVariant) {
            // The alien is already the given variant, so return.
            return AlienTransitionResult.AlreadyDesiredVariant.INSTANCE;
        }

        @SuppressWarnings("unchecked")
        var variantType = (EntityType<T>) alien.getTypeForVariant(alienVariant);

        if (variantType == null) {
            // The alien has no corresponding type for the given variant, nothing we can do here, so return.
            return AlienTransitionResult.NoTypeForVariant.INSTANCE;
        }

        // Snapshot hive2 faction membership before transitionInto discards the old entity (UUID is in the default
        // blacklist). The Phase 9 invariant task will evict variant mismatches afterward if they don't fit the
        // lineage's variant — but for self-variant transitions this preserves location-tier membership correctly.
        var factionSnapshot = FactionMembershipTransfer.snapshot(alien);

        var result = EntityTransitionUtil.transitionInto(alien, variantType);

        if (result instanceof EntityTransitionUtil.EntityTransitionResult.Success<?> success
            && level instanceof ServerLevel serverLevel) {
            FactionMembershipTransfer.apply(factionSnapshot, success.newEntity());
            LocationMembership.autoJoinAtPosition(success.newEntity(), serverLevel);
        }

        return new AlienTransitionResult.Result(result);
    }

    public sealed interface AlienTransitionResult {

        enum ClientSide implements AlienTransitionResult {
            INSTANCE
        }

        enum AlreadyDesiredVariant implements AlienTransitionResult {
            INSTANCE
        }

        enum NoTypeForVariant implements AlienTransitionResult {
            INSTANCE
        }

        record Result(EntityTransitionUtil.EntityTransitionResult result) implements AlienTransitionResult {}
    }
}

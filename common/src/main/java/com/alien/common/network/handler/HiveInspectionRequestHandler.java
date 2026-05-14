package com.alien.common.network.handler;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.LocationFactionData;
import com.alien.common.gameplay.hive2.faction.VariantFactionData;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.inspection.HiveInspectionSnapshot;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.network.payload.C2SRequestHiveInspectionPayload;
import com.alien.common.network.payload.S2CHiveInspectionPayload;
import com.blib.api.common.faction.v1.Faction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Server-side handler for {@link C2SRequestHiveInspectionPayload}. Resolves the requested faction id, dispatches to the
 * matching snapshot builder (Location / Lineage / Variant), and replies with {@link S2CHiveInspectionPayload}. Op-gated
 * (permission level ≥ 2) since the panel is a debug surface.
 */
public final class HiveInspectionRequestHandler {

    private HiveInspectionRequestHandler() {}

    public static void handle(C2SRequestHiveInspectionPayload payload, Player player) {
        if (!(player instanceof ServerPlayer sp)) {
            return;
        }
        if (!sp.hasPermissions(2)) {
            return;
        }

        var factionId = payload.factionId();
        var faction = Alien.MOD.factions().get(factionId);
        if (faction == null) {
            return;
        }
        var data = faction.data();
        if (data instanceof LocationFactionData) {
            // Location faction id is exactly the HiveLocationId's ResourceLocation, so prefer a direct registry lookup
            // over LocationFactionData.locationId() — the latter is nullable on legacy / mid-load state and silently
            // skipping the reply makes the panel appear stuck.
            var location = HiveLocationRegistry.INSTANCE.get(HiveLocationId.of(factionId));
            if (location == null) {
                return;
            }
            var snapshot = HiveInspectionSnapshot.buildLocation(location, sp.server);
            Alien.MOD
                .networking()
                .sendToClient(
                    sp,
                    new S2CHiveInspectionPayload(HiveInspectionSnapshot.KIND_LOCATION, factionId, snapshot)
                );
        } else if (data instanceof LineageFactionData) {
            @SuppressWarnings("unchecked")
            var lineage = (Faction<LineageFactionData>) faction;
            var snapshot = HiveInspectionSnapshot.buildLineage(lineage, sp.server);
            Alien.MOD
                .networking()
                .sendToClient(
                    sp,
                    new S2CHiveInspectionPayload(HiveInspectionSnapshot.KIND_LINEAGE, factionId, snapshot)
                );
        } else if (data instanceof VariantFactionData) {
            @SuppressWarnings("unchecked")
            var variant = (Faction<VariantFactionData>) faction;
            var snapshot = HiveInspectionSnapshot.buildVariant(variant, sp.server);
            Alien.MOD
                .networking()
                .sendToClient(
                    sp,
                    new S2CHiveInspectionPayload(HiveInspectionSnapshot.KIND_VARIANT, factionId, snapshot)
                );
        }
    }
}

package com.alien.common.network.payload;

import com.alien.Alien;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.just.codec.stream.RecordStreamCodec;
import com.just.codec.stream.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Client → server: request a hive inspection snapshot for the currently-selected AVP faction. Fired by the inspector
 * sections (LocationFactionInspectorSection / LineageFactionInspectorSection / VariantFactionInspectorSection) on
 * selection change. Server replies with {@link S2CHiveInspectionPayload}.
 */
public record C2SRequestHiveInspectionPayload(ResourceLocation factionId) implements CustomPacketPayload {

    public static final ResourceLocation PAYLOAD_ID = Alien.MOD.resources().createLocation("request_hive_inspection");

    public static final Type<C2SRequestHiveInspectionPayload> TYPE = new Type<>(PAYLOAD_ID);

    public static final StreamCodec<C2SRequestHiveInspectionPayload> CODEC = RecordStreamCodec.of(
        BLibCodecs.Stream.RESOURCE_LOCATION,
        C2SRequestHiveInspectionPayload::factionId,
        C2SRequestHiveInspectionPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

package com.alien.common.network.payload;

import com.alien.Alien;
import com.alien.common.network.codec.CompoundTagStreamCodec;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.just.codec.stream.RecordStreamCodec;
import com.just.codec.stream.StreamCodec;
import com.just.codec.stream.impl.StreamCodecs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Server → client: hive inspection snapshot. The {@code kind} discriminator tags the payload as a Location, Lineage, or
 * Variant snapshot (one of {@link com.alien.common.gameplay.hive.inspection.HiveInspectionSnapshot#KIND_LOCATION} /
 * {@code KIND_LINEAGE} / {@code KIND_VARIANT}); the {@code data} tag holds the snapshot built by
 * {@link com.alien.common.gameplay.hive.inspection.HiveInspectionSnapshot}. Sent in reply to
 * {@link C2SRequestHiveInspectionPayload} and consumed by {@code ClientHiveInspectionCache}.
 */
public record S2CHiveInspectionPayload(
    String kind,
    ResourceLocation factionId,
    CompoundTag data
) implements CustomPacketPayload {

    public static final ResourceLocation PAYLOAD_ID = Alien.MOD.resources().createLocation("hive_inspection");

    public static final Type<S2CHiveInspectionPayload> TYPE = new Type<>(PAYLOAD_ID);

    public static final StreamCodec<S2CHiveInspectionPayload> CODEC = RecordStreamCodec.of(
        StreamCodecs.STRING_UTF8,
        S2CHiveInspectionPayload::kind,
        BLibCodecs.Stream.RESOURCE_LOCATION,
        S2CHiveInspectionPayload::factionId,
        CompoundTagStreamCodec.INSTANCE,
        S2CHiveInspectionPayload::data,
        S2CHiveInspectionPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

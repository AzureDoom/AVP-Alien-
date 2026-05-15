package com.alien.common.network.payload;

import com.alien.Alien;
import com.just.codec.stream.RecordStreamCodec;
import com.just.codec.stream.StreamCodec;
import com.just.codec.stream.impl.StreamCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record C2SUpdateHiveConfigPayload(String fieldName, String value) implements CustomPacketPayload {

    public static final ResourceLocation PAYLOAD_ID = Alien.MOD.resources().createLocation("update_hive_config");

    public static final Type<C2SUpdateHiveConfigPayload> TYPE = new Type<>(PAYLOAD_ID);

    public static final StreamCodec<C2SUpdateHiveConfigPayload> CODEC = RecordStreamCodec.of(
        StreamCodecs.STRING_UTF8,
        C2SUpdateHiveConfigPayload::fieldName,
        StreamCodecs.STRING_UTF8,
        C2SUpdateHiveConfigPayload::value,
        C2SUpdateHiveConfigPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

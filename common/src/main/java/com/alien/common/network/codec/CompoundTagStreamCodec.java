package com.alien.common.network.codec;

import com.just.codec.stream.StreamCodec;
import com.just.codec.stream.impl.StreamCodecs;
import com.just.codec.stream.schema.StreamCodecSchema;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Stream-codec for vanilla {@link CompoundTag}. Wire format is {@code VarInt(byteLength) + nbtBytes}, where the NBT
 * bytes are produced by {@link NbtIo#write(CompoundTag, DataOutput)}. Used by the hive-inspection payloads which carry
 * a snapshot tag whose schema would be cumbersome to express as a flat record-style stream codec.
 * <p>
 * Capped at 4MB on decode via {@link NbtAccounter} so a malformed packet can't drive an allocation explosion. Snapshots
 * are bounded by per-location data (single hive's chunks + caste counts), so 4MB is a comfortable ceiling.
 */
public final class CompoundTagStreamCodec implements StreamCodec<CompoundTag> {

    public static final CompoundTagStreamCodec INSTANCE = new CompoundTagStreamCodec();

    private static final long MAX_DECODED_BYTES = 4L * 1024L * 1024L;

    private CompoundTagStreamCodec() {}

    @Override
    public <T> void encode(@NotNull StreamCodecSchema<T> schema, @NotNull T input, @NotNull CompoundTag value) {
        var baos = new ByteArrayOutputStream();
        try (var dos = new DataOutputStream(baos)) {
            NbtIo.write(value, dos);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        var bytes = baos.toByteArray();
        StreamCodecs.VAR_INT.encode(schema, input, bytes.length);
        schema.writeBytes(input, bytes);
    }

    @Override
    public @NotNull <T> CompoundTag decode(@NotNull StreamCodecSchema<T> schema, @NotNull T input) {
        var length = StreamCodecs.VAR_INT.decode(schema, input);
        var bytes = schema.readBytes(input, length);
        try (var bis = new ByteArrayInputStream(bytes); var dis = new DataInputStream(bis)) {
            return NbtIo.read(dis, NbtAccounter.create(MAX_DECODED_BYTES));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

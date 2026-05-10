package com.alien.common.gameplay.hive2.migration;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Map;

/**
 * Writes the legacy-hive → new-(lineage, location) mapping to {@code data/avp_alien/hive2_migration.log} in the world
 * folder. One file per server start (overwrites previous).
 * <p>
 * Per {@code HIVE_REDESIGN_11_IMPLEMENTATION.md} § 6 step 19. Useful for support: an admin can grep the file for an old
 * hive id and find what it became.
 */
public final class MigrationLog {

    private static final String LOG_DIRECTORY = "avp_alien";

    private static final String LOG_FILE_NAME = "hive2_migration.log";

    private MigrationLog() {}

    public static void write(MinecraftServer server, Map<ResourceLocation, MigrationEntry> entries) {
        if (entries.isEmpty()) {
            return;
        }

        var worldPath = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT);
        var dataPath = worldPath.resolve("data").resolve(LOG_DIRECTORY);

        try {
            Files.createDirectories(dataPath);
        } catch (IOException e) {
            Alien.LOGGER.warn("Hive2 migrator: failed to create migration log directory {}: {}", dataPath, e.getMessage());
            return;
        }

        var logPath = dataPath.resolve(LOG_FILE_NAME);
        try (var writer = Files.newBufferedWriter(logPath)) {
            writeHeader(writer, entries.size());
            for (var entry : entries.entrySet()) {
                writeEntry(writer, entry.getKey(), entry.getValue());
            }
            Alien.LOGGER.info("Hive2 migrator: wrote {} migration entries to {}", entries.size(), logPath);
        } catch (IOException e) {
            Alien.LOGGER.warn("Hive2 migrator: failed to write migration log {}: {}", logPath, e.getMessage());
        }
    }

    private static void writeHeader(BufferedWriter writer, int entryCount) throws IOException {
        writer.write("# AVP-Alien hive2 migration log");
        writer.newLine();
        writer.write("# generated: ");
        writer.write(Instant.now().toString());
        writer.newLine();
        writer.write("# entries: ");
        writer.write(Integer.toString(entryCount));
        writer.newLine();
        writer.write("# format: <legacy_hive_id> -> lineage=<lineage_id> location=<location_id> center=<x,y,z> dim=<dim>");
        writer.newLine();
    }

    private static void writeEntry(BufferedWriter writer, ResourceLocation legacyId, MigrationEntry entry) throws IOException {
        writer.write(legacyId.toString());
        writer.write(" -> lineage=");
        writer.write(entry.lineageId.toString());
        writer.write(" location=");
        writer.write(entry.locationId.toString());
        writer.write(" center=");
        writer.write(entry.centerPos.getX() + "," + entry.centerPos.getY() + "," + entry.centerPos.getZ());
        writer.write(" dim=");
        writer.write(entry.dimension.toString());
        writer.newLine();
    }

    public record MigrationEntry(
        ResourceLocation legacyHiveId,
        ResourceLocation lineageId,
        HiveLocationId locationId,
        BlockPos centerPos,
        ResourceLocation dimension
    ) {}
}

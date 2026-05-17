package com.alien.common.gameplay.command.count;

import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

public class CountCommand {

    private static final String COMMAND_NAME = "count";

    private static final String ENTITY_ARGUMENT_NAME = "entity";

    private static final String HIVE_ARGUMENT_NAME = "hive";

    private static final String LINEAGE_ARGUMENT_NAME = "lineage";

    private static final String LOCATION_ARGUMENT_NAME = "location";

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal(COMMAND_NAME)
            .then(
                Commands.argument(ENTITY_ARGUMENT_NAME, EntityArgument.entities())
                    .executes(context -> {
                        var result = EntityArgument.getEntities(context, "entity");
                        var count = result.size();

                        context.getSource().sendSuccess(() -> {
                            var areOrIs = count == 1 ? "is" : "are";
                            var pluralEntity = count == 1 ? "that entity" : "those entities";
                            return Component.literal(
                                "There " + areOrIs + " " + count + " of " + pluralEntity + " in the world."
                            );
                        }, false);

                        return 1;
                    })
            )
            .then(
                // Backwards compat: `count hive` reports total hive2 locations.
                Commands.literal(HIVE_ARGUMENT_NAME)
                    .executes(CountCommand::countLocations)
            )
            .then(
                Commands.literal(LOCATION_ARGUMENT_NAME)
                    .executes(CountCommand::countLocations)
            )
            .then(
                Commands.literal(LINEAGE_ARGUMENT_NAME)
                    .executes(CountCommand::countLineages)
            );
    }

    private static int countLocations(com.mojang.brigadier.context.CommandContext<CommandSourceStack> context) {
        var count = HiveLocationRegistry.INSTANCE.locationCount();
        context.getSource().sendSuccess(() -> {
            var areOrIs = count == 1 ? "is" : "are";
            var pluralLocation = count == 1 ? "hive location" : "hive locations";
            return Component.literal("There " + areOrIs + " " + count + " " + pluralLocation + " in the world.");
        }, false);
        return 1;
    }

    private static int countLineages(com.mojang.brigadier.context.CommandContext<CommandSourceStack> context) {
        var count = HiveLocationRegistry.INSTANCE.lineageCount();
        context.getSource().sendSuccess(() -> {
            var areOrIs = count == 1 ? "is" : "are";
            var pluralLineage = count == 1 ? "lineage" : "lineages";
            return Component.literal("There " + areOrIs + " " + count + " " + pluralLineage + " in the world.");
        }, false);
        return 1;
    }
}

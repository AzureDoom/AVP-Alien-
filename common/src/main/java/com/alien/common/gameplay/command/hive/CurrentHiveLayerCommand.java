package com.alien.common.gameplay.command.hive;

import com.alien.common.gameplay.hive.HiveRegistry;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class CurrentHiveLayerCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("current")
            .requires(CommandSourceStack::isPlayer)
            .executes(context -> {
                var player = Objects.requireNonNull(context.getSource().getPlayer());
                var playerPos = player.blockPosition();
                var level = context.getSource().getLevel();

                var hive = HiveRegistry.INSTANCE.findNearestHive(playerPos, level.dimension());

                if (hive != null) {
                    var hiveLayer = hive.getSpaceManager().getHiveLayerOrNull(playerPos);

                    if (hiveLayer == null) {
                        context.getSource().sendSuccess(() -> Component.literal("No layer found."), false);
                    } else {
                        context.getSource()
                            .sendSuccess(
                                () -> Component.literal("Current hive layer: " + hiveLayer),
                                false
                            );
                    }
                } else {
                    context.getSource().sendSuccess(() -> Component.literal("No nearby hive found."), false);
                }

                return 1;
            });
    }
}

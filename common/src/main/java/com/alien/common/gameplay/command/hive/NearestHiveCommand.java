package com.alien.common.gameplay.command.hive;

import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class NearestHiveCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("nearest")
            .requires(CommandSourceStack::isPlayer)
            .executes(context -> {
                var player = Objects.requireNonNull(context.getSource().getPlayer());
                var level = context.getSource().getLevel();

                var location = HiveLocationRegistry.INSTANCE.findNearestInDim(level.dimension(), player.blockPosition());

                if (location != null) {
                    var pos = location.centerPos();

                    context.getSource()
                        .sendSuccess(
                            () -> Component.literal(
                                "Nearest hive location: " + location.id() + " at x " + pos.getX() + " y " + pos.getY() + " z " + pos.getZ()
                            ),
                            false
                        );
                } else {
                    context.getSource().sendSuccess(() -> Component.literal("No nearby hive location found."), false);
                }

                return 1;
            });
    }
}

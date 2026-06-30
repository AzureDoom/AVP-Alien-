package com.alien.common.gameplay.command.debug;

import com.alien.common.registry.init.item.AlienItems;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class DebugLimbStickCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("limb_stick")
            .requires(CommandSourceStack::isPlayer)
            .executes(context -> giveStick(context.getSource()));
    }

    private static int giveStick(CommandSourceStack source) throws CommandSyntaxException {
        var player = source.getPlayerOrException();
        var itemStack = new ItemStack(AlienItems.DEBUG_LIMB_STICK.get());

        if (!player.getInventory().add(itemStack)) {
            player.drop(itemStack, false);
        }

        source.sendSuccess(() -> Component.literal("Gave debug limb stick."), false);
        return 1;
    }

    private DebugLimbStickCommand() {
        throw new UnsupportedOperationException();
    }
}

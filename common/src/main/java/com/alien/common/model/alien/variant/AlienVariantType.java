package com.alien.common.model.alien.variant;

import com.alien.common.gameplay.block.resin.vein.ResinVeinBlock;
import com.alien.common.gameplay.block.resin.vent.ResinVentBlock;
import com.blib.api.common.registry.v1.BLibHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record AlienVariantType(
    AlienVariant variant,

    // Blocks
    Supplier<Block> resin,
    Supplier<Block> resinNode,
    Supplier<ResinVeinBlock> resinVein,
    Supplier<ResinVentBlock> resinVent,
    Supplier<Block> resinWeb,

    // Block Tags
    TagKey<Block> resinBlockTag,
    TagKey<Block> resinReplaceableTag,

    // Items
    Supplier<Item> chitin,
    Supplier<Item> platedChitin,
    Supplier<Item> resinBall,

    // Game Events
    BLibHolder<GameEvent> cryForHelpEvent,
    @Nullable BLibHolder<GameEvent> eggPickupRequestEvent,
    BLibHolder<GameEvent> resinSpreadEvent,

    // Particle Types
    Supplier<SimpleParticleType> acidParticleType,

    // Miscellaneous
    BossEvent.BossBarColor bossBarColor,
    ChatFormatting chatColor,
    boolean canReproduce
) {}

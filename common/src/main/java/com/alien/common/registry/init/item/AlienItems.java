package com.alien.common.registry.init.item;

import com.alien.Alien;
import com.alien.common.gameplay.item.PoisonJellyItem;
import com.alien.common.gameplay.item.RoyalJellyItem;
import com.alien.common.registry.key.AlienJukeboxSongKeys;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import com.blib.api.common.registry.v1.impl.BLibDecoratedPotPatternRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DiscFragmentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class AlienItems {

    public static final BLibRegistry<Item> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ITEM);

    private static final BLibDecoratedPotPatternRegistry DECORATED_POT_PATTERN_REGISTRY = Alien.MOD.registries()
        .createDecoratedPotPatternRegistry();

    public static final BLibHolder<Item> ABERRANT_CHITIN = create(
        "aberrant_chitin",
        new Item.Properties()
    );

    public static final BLibHolder<Item> ABERRANT_RESIN_BALL = create(
        "aberrant_resin_ball",
        new Item.Properties()
    );

    public static final BLibHolder<Item> ALIEN_MUSIC_DISC_1 = create(
        "alien_music_disc_1",
        new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AlienJukeboxSongKeys.ALIEN_MUSIC_1)
    );

    public static final BLibHolder<Item> ALIEN_MUSIC_DISC_1_FRAGMENT = create(
        "alien_music_disc_1_fragment",
        () -> new DiscFragmentItem(new Item.Properties())
    );

    public static final BLibHolder<Item> CHITIN = create("chitin");

    public static final BLibHolder<Item> IRRADIATED_CHITIN = create("irradiated_chitin");

    public static final BLibHolder<Item> IRRADIATED_RESIN_BALL = create("irradiated_resin_ball");

    public static final BLibHolder<Item> NETHER_CHITIN = create("nether_chitin", new Item.Properties().fireResistant());

    public static final BLibHolder<Item> NETHER_RESIN_BALL = create(
        "nether_resin_ball",
        new Item.Properties().fireResistant()
    );

    public static final BLibHolder<Item> OVOID_POTTERY_SHERD = create("ovoid_pottery_sherd");

    public static final BLibHolder<Item> PARASITE_POTTERY_SHERD = create("parasite_pottery_sherd");

    public static final BLibHolder<Item> PLATED_CHITIN = create("plated_chitin");

    public static final BLibHolder<Item> PLATED_ABERRANT_CHITIN = create(
        "plated_aberrant_chitin",
        new Item.Properties()
    );

    public static final BLibHolder<Item> PLATED_IRRADIATED_CHITIN = create("plated_irradiated_chitin");

    public static final BLibHolder<Item> PLATED_NETHER_CHITIN = create(
        "plated_nether_chitin",
        new Item.Properties().fireResistant()
    );

    public static final BLibHolder<Item> POISON_JELLY = create("poison_jelly", PoisonJellyItem::new);

    public static final BLibHolder<Item> RAW_ROYAL_JELLY = create("raw_royal_jelly", RoyalJellyItem::new);

    public static final BLibHolder<Item> RESIN_BALL = create("resin_ball");

    public static final BLibHolder<Item> ROYALTY_POTTERY_SHERD = create("royalty_pottery_sherd");

    public static final BLibHolder<Item> VECTOR_POTTERY_SHERD = create("vector_pottery_sherd");

    public static BLibHolder<Item> create(String name) {
        return create(name, new Item.Properties());
    }

    private static BLibHolder<Item> create(String name, Item.Properties properties) {
        return create(name, () -> new Item(properties));
    }

    private static <T extends Item> BLibHolder<T> create(String name, Supplier<T> itemSupplier) {
        return REGISTRY.createHolder(name, itemSupplier);
    }

    public static void initialize() {
        REGISTRY.registerAll();
        DECORATED_POT_PATTERN_REGISTRY.register("ovoid_pottery_pattern", OVOID_POTTERY_SHERD);
        DECORATED_POT_PATTERN_REGISTRY.register("parasite_pottery_pattern", PARASITE_POTTERY_SHERD);
        DECORATED_POT_PATTERN_REGISTRY.register("royalty_pottery_pattern", ROYALTY_POTTERY_SHERD);
        DECORATED_POT_PATTERN_REGISTRY.register("vector_pottery_pattern", VECTOR_POTTERY_SHERD);
    }
}

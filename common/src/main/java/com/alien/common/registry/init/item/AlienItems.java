package com.alien.common.registry.init.item;

import com.alien.AlienResources;
import com.alien.common.gameplay.item.PoisonJellyItem;
import com.alien.common.gameplay.item.RoyalJellyItem;
import com.alien.common.registry.key.AlienJukeboxSongKeys;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DiscFragmentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AlienItems {

    private static final List<AVPDeferredHolder<? extends Item>> HOLDERS = new ArrayList<>();

    public static List<AVPDeferredHolder<? extends Item>> getAll() {
        return Collections.unmodifiableList(HOLDERS);
    }

    public static final AVPDeferredHolder<Item> ABERRANT_CHITIN = register(
        "aberrant_chitin",
        new Item.Properties().fireResistant()
    );

    public static final AVPDeferredHolder<Item> ABERRANT_RESIN_BALL = register(
        "aberrant_resin_ball",
        new Item.Properties().fireResistant()
    );

    public static final AVPDeferredHolder<Item> ALIEN_MUSIC_DISC_1 = register(
        "alien_music_disc_1",
        new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AlienJukeboxSongKeys.ALIEN_MUSIC_1)
    );

    public static final AVPDeferredHolder<Item> ALIEN_MUSIC_DISC_1_FRAGMENT = register(
        "alien_music_disc_1_fragment",
        () -> new DiscFragmentItem(new Item.Properties())
    );

    public static final AVPDeferredHolder<Item> CHITIN = register("chitin");

    public static final AVPDeferredHolder<Item> IRRADIATED_CHITIN = register("irradiated_chitin");

    public static final AVPDeferredHolder<Item> IRRADIATED_RESIN_BALL = register("irradiated_resin_ball");

    public static final AVPDeferredHolder<Item> NETHER_CHITIN = register("nether_chitin", new Item.Properties().fireResistant());

    public static final AVPDeferredHolder<Item> NETHER_RESIN_BALL = register(
        "nether_resin_ball",
        new Item.Properties().fireResistant()
    );

    public static final AVPDeferredHolder<Item> OVOID_POTTERY_SHERD = register("ovoid_pottery_sherd");

    public static final AVPDeferredHolder<Item> PARASITE_POTTERY_SHERD = register("parasite_pottery_sherd");

    public static final AVPDeferredHolder<Item> PLATED_CHITIN = register("plated_chitin");

    public static final AVPDeferredHolder<Item> PLATED_ABERRANT_CHITIN = register(
        "plated_aberrant_chitin",
        new Item.Properties().fireResistant()
    );

    public static final AVPDeferredHolder<Item> PLATED_IRRADIATED_CHITIN = register("plated_irradiated_chitin");

    public static final AVPDeferredHolder<Item> PLATED_NETHER_CHITIN = register(
        "plated_nether_chitin",
        new Item.Properties().fireResistant()
    );

    public static final AVPDeferredHolder<Item> POISON_JELLY = register("poison_jelly", PoisonJellyItem::new);

    public static final AVPDeferredHolder<Item> RAW_ROYAL_JELLY = register("raw_royal_jelly", RoyalJellyItem::new);

    public static final AVPDeferredHolder<Item> RESIN_BALL = register("resin_ball");

    public static final AVPDeferredHolder<Item> ROYALTY_POTTERY_SHERD = register("royalty_pottery_sherd");

    public static final AVPDeferredHolder<Item> VECTOR_POTTERY_SHERD = register("vector_pottery_sherd");

    public static AVPDeferredHolder<Item> register(String name) {
        return register(name, new Item.Properties());
    }

    public static AVPDeferredHolder<Item> register(String name, Item.Properties properties) {
        return register(name, () -> new Item(properties));
    }

    public static <T extends Item> AVPDeferredHolder<T> register(String name, Supplier<T> itemSupplier) {
        var holder = Services.REGISTRY.register(BuiltInRegistries.ITEM, AlienResources.location(name), itemSupplier);
        HOLDERS.add(holder);
        return holder;
    }

    public static void initialize() {}
}

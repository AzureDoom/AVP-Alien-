package com.alien.common.registry.init.item;

import com.alien.AlienResources;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AlienItems {

    private static final List<AVPDeferredHolder<? extends Item>> HOLDERS = new ArrayList<>();

    public static List<AVPDeferredHolder<? extends Item>> getAll() {
        return Collections.unmodifiableList(HOLDERS);
    }

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

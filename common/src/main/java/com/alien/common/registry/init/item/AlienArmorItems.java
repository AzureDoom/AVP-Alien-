package com.alien.common.registry.init.item;

import com.avp.common.registry.AVPDeferredHolder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class AlienArmorItems {

    public static AVPDeferredHolder<ArmorItem> register(
        String id,
        Supplier<Holder<ArmorMaterial>> holderSupplier,
        ArmorItem.Type type,
        int durabilityMultiplier
    ) {
        return register(id, holderSupplier, type, durabilityMultiplier, new Item.Properties());
    }

    public static AVPDeferredHolder<ArmorItem> register(
        String id,
        Supplier<Holder<ArmorMaterial>> holderSupplier,
        ArmorItem.Type type,
        int durabilityMultiplier,
        Item.Properties properties
    ) {
        return register(id, () -> createArmorItem(holderSupplier.get(), type, durabilityMultiplier, properties));
    }

    public static <T extends Item> AVPDeferredHolder<T> register(String id, Supplier<T> itemSupplier) {
        return AlienItems.register(id, itemSupplier);
    }

    public static ArmorItem createArmorItem(
        Holder<ArmorMaterial> holder,
        ArmorItem.Type type,
        int durabilityMultiplier,
        Item.Properties properties
    ) {
        var durability = type.getDurability(durabilityMultiplier);
        properties = properties.durability(durability);
        return new ArmorItem(holder, type, properties);
    }

    public static void initialize() {}
}

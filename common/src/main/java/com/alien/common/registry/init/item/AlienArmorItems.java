package com.alien.common.registry.init.item;

import com.alien.common.gameplay.item.NetherChitinArmorItem;
import com.alien.common.gameplay.item.PlatedNetherChitinArmorItem;
import com.alien.common.registry.init.AlienArmorMaterials;
import com.avp.common.registry.AVPDeferredHolder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class AlienArmorItems {

    public static final int CHITIN_DURABILITY_MULTIPLIER = 21;

    public static final int PLATED_CHITIN_DURABILITY_MULTIPLIER = 27;

    public static final AVPDeferredHolder<ArmorItem> ABERRANT_CHITIN_BOOTS = register(
        "aberrant_chitin_boots",
        AlienArmorMaterials.ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.BOOTS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> ABERRANT_CHITIN_CHESTPLATE = register(
        "aberrant_chitin_chestplate",
        AlienArmorMaterials.ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.CHESTPLATE,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> ABERRANT_CHITIN_HELMET = register(
        "aberrant_chitin_helmet",
        AlienArmorMaterials.ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.HELMET,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> ABERRANT_CHITIN_LEGGINGS = register(
        "aberrant_chitin_leggings",
        AlienArmorMaterials.ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.LEGGINGS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> CHITIN_BOOTS = register(
        "chitin_boots",
        AlienArmorMaterials.CHITIN::getHolder,
        ArmorItem.Type.BOOTS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> CHITIN_CHESTPLATE = register(
        "chitin_chestplate",
        AlienArmorMaterials.CHITIN::getHolder,
        ArmorItem.Type.CHESTPLATE,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> CHITIN_HELMET = register(
        "chitin_helmet",
        AlienArmorMaterials.CHITIN::getHolder,
        ArmorItem.Type.HELMET,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> CHITIN_LEGGINGS = register(
        "chitin_leggings",
        AlienArmorMaterials.CHITIN::getHolder,
        ArmorItem.Type.LEGGINGS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> IRRADIATED_CHITIN_BOOTS = register(
        "irradiated_chitin_boots",
        AlienArmorMaterials.IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.BOOTS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> IRRADIATED_CHITIN_CHESTPLATE = register(
        "irradiated_chitin_chestplate",
        AlienArmorMaterials.IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.CHESTPLATE,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> IRRADIATED_CHITIN_HELMET = register(
        "irradiated_chitin_helmet",
        AlienArmorMaterials.IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.HELMET,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> IRRADIATED_CHITIN_LEGGINGS = register(
        "irradiated_chitin_leggings",
        AlienArmorMaterials.IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.LEGGINGS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> NETHER_CHITIN_BOOTS = register(
        "nether_chitin_boots",
        () -> new NetherChitinArmorItem(ArmorItem.Type.BOOTS)
    );

    public static final AVPDeferredHolder<ArmorItem> NETHER_CHITIN_CHESTPLATE = register(
        "nether_chitin_chestplate",
        () -> new NetherChitinArmorItem(ArmorItem.Type.CHESTPLATE)
    );

    public static final AVPDeferredHolder<ArmorItem> NETHER_CHITIN_HELMET = register(
        "nether_chitin_helmet",
        () -> new NetherChitinArmorItem(ArmorItem.Type.HELMET)
    );

    public static final AVPDeferredHolder<ArmorItem> NETHER_CHITIN_LEGGINGS = register(
        "nether_chitin_leggings",
        () -> new NetherChitinArmorItem(ArmorItem.Type.LEGGINGS)
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_ABERRANT_CHITIN_BOOTS = register(
        "plated_aberrant_chitin_boots",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.BOOTS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_ABERRANT_CHITIN_CHESTPLATE = register(
        "plated_aberrant_chitin_chestplate",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.CHESTPLATE,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_ABERRANT_CHITIN_HELMET = register(
        "plated_aberrant_chitin_helmet",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.HELMET,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_ABERRANT_CHITIN_LEGGINGS = register(
        "plated_aberrant_chitin_leggings",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN::getHolder,
        ArmorItem.Type.LEGGINGS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_CHITIN_BOOTS = register(
        "plated_chitin_boots",
        AlienArmorMaterials.PLATED_CHITIN::getHolder,
        ArmorItem.Type.BOOTS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_CHITIN_CHESTPLATE = register(
        "plated_chitin_chestplate",
        AlienArmorMaterials.PLATED_CHITIN::getHolder,
        ArmorItem.Type.CHESTPLATE,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_CHITIN_HELMET = register(
        "plated_chitin_helmet",
        AlienArmorMaterials.PLATED_CHITIN::getHolder,
        ArmorItem.Type.HELMET,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_CHITIN_LEGGINGS = register(
        "plated_chitin_leggings",
        AlienArmorMaterials.PLATED_CHITIN::getHolder,
        ArmorItem.Type.LEGGINGS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_BOOTS = register(
        "plated_irradiated_chitin_boots",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.BOOTS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_CHESTPLATE = register(
        "plated_irradiated_chitin_chestplate",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.CHESTPLATE,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_HELMET = register(
        "plated_irradiated_chitin_helmet",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.HELMET,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_LEGGINGS = register(
        "plated_irradiated_chitin_leggings",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN::getHolder,
        ArmorItem.Type.LEGGINGS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_NETHER_CHITIN_BOOTS = register(
        "plated_nether_chitin_boots",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.BOOTS)
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_NETHER_CHITIN_CHESTPLATE = register(
        "plated_nether_chitin_chestplate",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.CHESTPLATE)
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_NETHER_CHITIN_HELMET = register(
        "plated_nether_chitin_helmet",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.HELMET)
    );

    public static final AVPDeferredHolder<ArmorItem> PLATED_NETHER_CHITIN_LEGGINGS = register(
        "plated_nether_chitin_leggings",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.LEGGINGS)
    );

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

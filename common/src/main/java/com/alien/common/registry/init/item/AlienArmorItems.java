package com.alien.common.registry.init.item;

import com.alien.Alien;
import com.alien.common.gameplay.item.NetherChitinArmorItem;
import com.alien.common.gameplay.item.PlatedNetherChitinArmorItem;
import com.alien.common.registry.init.AlienArmorMaterials;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class AlienArmorItems {

    private static final BLibRegistry<Item> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ITEM);

    public static final int CHITIN_DURABILITY_MULTIPLIER = 21;

    public static final int PLATED_CHITIN_DURABILITY_MULTIPLIER = 27;

    public static final BLibHolder<ArmorItem> ABERRANT_CHITIN_BOOTS = create(
        "aberrant_chitin_boots",
        AlienArmorMaterials.ABERRANT_CHITIN,
        ArmorItem.Type.BOOTS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> ABERRANT_CHITIN_CHESTPLATE = create(
        "aberrant_chitin_chestplate",
        AlienArmorMaterials.ABERRANT_CHITIN,
        ArmorItem.Type.CHESTPLATE,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> ABERRANT_CHITIN_HELMET = create(
        "aberrant_chitin_helmet",
        AlienArmorMaterials.ABERRANT_CHITIN,
        ArmorItem.Type.HELMET,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> ABERRANT_CHITIN_LEGGINGS = create(
        "aberrant_chitin_leggings",
        AlienArmorMaterials.ABERRANT_CHITIN,
        ArmorItem.Type.LEGGINGS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> CHITIN_BOOTS = create(
        "chitin_boots",
        AlienArmorMaterials.CHITIN,
        ArmorItem.Type.BOOTS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> CHITIN_CHESTPLATE = create(
        "chitin_chestplate",
        AlienArmorMaterials.CHITIN,
        ArmorItem.Type.CHESTPLATE,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> CHITIN_HELMET = create(
        "chitin_helmet",
        AlienArmorMaterials.CHITIN,
        ArmorItem.Type.HELMET,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> CHITIN_LEGGINGS = create(
        "chitin_leggings",
        AlienArmorMaterials.CHITIN,
        ArmorItem.Type.LEGGINGS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> IRRADIATED_CHITIN_BOOTS = create(
        "irradiated_chitin_boots",
        AlienArmorMaterials.IRRADIATED_CHITIN,
        ArmorItem.Type.BOOTS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> IRRADIATED_CHITIN_CHESTPLATE = create(
        "irradiated_chitin_chestplate",
        AlienArmorMaterials.IRRADIATED_CHITIN,
        ArmorItem.Type.CHESTPLATE,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> IRRADIATED_CHITIN_HELMET = create(
        "irradiated_chitin_helmet",
        AlienArmorMaterials.IRRADIATED_CHITIN,
        ArmorItem.Type.HELMET,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> IRRADIATED_CHITIN_LEGGINGS = create(
        "irradiated_chitin_leggings",
        AlienArmorMaterials.IRRADIATED_CHITIN,
        ArmorItem.Type.LEGGINGS,
        CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> NETHER_CHITIN_BOOTS = create(
        "nether_chitin_boots",
        () -> new NetherChitinArmorItem(ArmorItem.Type.BOOTS)
    );

    public static final BLibHolder<ArmorItem> NETHER_CHITIN_CHESTPLATE = create(
        "nether_chitin_chestplate",
        () -> new NetherChitinArmorItem(ArmorItem.Type.CHESTPLATE)
    );

    public static final BLibHolder<ArmorItem> NETHER_CHITIN_HELMET = create(
        "nether_chitin_helmet",
        () -> new NetherChitinArmorItem(ArmorItem.Type.HELMET)
    );

    public static final BLibHolder<ArmorItem> NETHER_CHITIN_LEGGINGS = create(
        "nether_chitin_leggings",
        () -> new NetherChitinArmorItem(ArmorItem.Type.LEGGINGS)
    );

    public static final BLibHolder<ArmorItem> PLATED_ABERRANT_CHITIN_BOOTS = create(
        "plated_aberrant_chitin_boots",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN,
        ArmorItem.Type.BOOTS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_ABERRANT_CHITIN_CHESTPLATE = create(
        "plated_aberrant_chitin_chestplate",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN,
        ArmorItem.Type.CHESTPLATE,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_ABERRANT_CHITIN_HELMET = create(
        "plated_aberrant_chitin_helmet",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN,
        ArmorItem.Type.HELMET,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_ABERRANT_CHITIN_LEGGINGS = create(
        "plated_aberrant_chitin_leggings",
        AlienArmorMaterials.PLATED_ABERRANT_CHITIN,
        ArmorItem.Type.LEGGINGS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_CHITIN_BOOTS = create(
        "plated_chitin_boots",
        AlienArmorMaterials.PLATED_CHITIN,
        ArmorItem.Type.BOOTS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_CHITIN_CHESTPLATE = create(
        "plated_chitin_chestplate",
        AlienArmorMaterials.PLATED_CHITIN,
        ArmorItem.Type.CHESTPLATE,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_CHITIN_HELMET = create(
        "plated_chitin_helmet",
        AlienArmorMaterials.PLATED_CHITIN,
        ArmorItem.Type.HELMET,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_CHITIN_LEGGINGS = create(
        "plated_chitin_leggings",
        AlienArmorMaterials.PLATED_CHITIN,
        ArmorItem.Type.LEGGINGS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_BOOTS = create(
        "plated_irradiated_chitin_boots",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN,
        ArmorItem.Type.BOOTS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_CHESTPLATE = create(
        "plated_irradiated_chitin_chestplate",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN,
        ArmorItem.Type.CHESTPLATE,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_HELMET = create(
        "plated_irradiated_chitin_helmet",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN,
        ArmorItem.Type.HELMET,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_IRRADIATED_CHITIN_LEGGINGS = create(
        "plated_irradiated_chitin_leggings",
        AlienArmorMaterials.PLATED_IRRADIATED_CHITIN,
        ArmorItem.Type.LEGGINGS,
        PLATED_CHITIN_DURABILITY_MULTIPLIER
    );

    public static final BLibHolder<ArmorItem> PLATED_NETHER_CHITIN_BOOTS = create(
        "plated_nether_chitin_boots",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.BOOTS)
    );

    public static final BLibHolder<ArmorItem> PLATED_NETHER_CHITIN_CHESTPLATE = create(
        "plated_nether_chitin_chestplate",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.CHESTPLATE)
    );

    public static final BLibHolder<ArmorItem> PLATED_NETHER_CHITIN_HELMET = create(
        "plated_nether_chitin_helmet",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.HELMET)
    );

    public static final BLibHolder<ArmorItem> PLATED_NETHER_CHITIN_LEGGINGS = create(
        "plated_nether_chitin_leggings",
        () -> new PlatedNetherChitinArmorItem(ArmorItem.Type.LEGGINGS)
    );

    private static BLibHolder<ArmorItem> create(
        String path,
        BLibHolder<ArmorMaterial> holder,
        ArmorItem.Type type,
        int durabilityMultiplier
    ) {
        return create(path, holder, type, durabilityMultiplier, new Item.Properties());
    }

    private static BLibHolder<ArmorItem> create(
        String path,
        BLibHolder<ArmorMaterial> holder,
        ArmorItem.Type type,
        int durabilityMultiplier,
        Item.Properties properties
    ) {
        return create(path, () -> createArmorItem(holder, type, durabilityMultiplier, properties));
    }

    private static <T extends ArmorItem> BLibHolder<T> create(String path, Supplier<T> itemSupplier) {
        return REGISTRY.createHolder(path, itemSupplier);
    }

    private static ArmorItem createArmorItem(
        BLibHolder<ArmorMaterial> holder,
        ArmorItem.Type type,
        int durabilityMultiplier,
        Item.Properties properties
    ) {
        var durability = type.getDurability(durabilityMultiplier);
        properties = properties.durability(durability);
        return new ArmorItem(holder, type, properties);
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

package com.alien.common.registry.init;

import com.alien.AlienResources;
import com.alien.common.registry.init.item.AlienItems;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AlienArmorMaterials {

    // Should be slightly stronger than iron.
    public static final AVPDeferredHolder<ArmorMaterial> ABERRANT_CHITIN = register(
        "aberrant_chitin",
        relativeDefense(
            ArmorMaterials.IRON,
            Map.ofEntries(
                Map.entry(ArmorItem.Type.CHESTPLATE, 1),
                Map.entry(ArmorItem.Type.BOOTS, 1)
            )
        ),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.ABERRANT_CHITIN.get()),
        0,
        0,
        false
    );

    // Should be slightly stronger than iron.
    public static final AVPDeferredHolder<ArmorMaterial> CHITIN = register(
        "chitin",
        relativeDefense(
            ArmorMaterials.IRON,
            Map.ofEntries(
                Map.entry(ArmorItem.Type.CHESTPLATE, 1),
                Map.entry(ArmorItem.Type.BOOTS, 1)
            )
        ),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.CHITIN.get()),
        0,
        0,
        false
    );

    // Should be slightly stronger than iron.
    public static final AVPDeferredHolder<ArmorMaterial> IRRADIATED_CHITIN = register(
        "irradiated_chitin",
        relativeDefense(
            ArmorMaterials.IRON,
            Map.ofEntries(
                Map.entry(ArmorItem.Type.CHESTPLATE, 1),
                Map.entry(ArmorItem.Type.BOOTS, 1)
            )
        ),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.IRRADIATED_CHITIN.get()),
        0,
        0,
        false
    );

    public static final AVPDeferredHolder<ArmorMaterial> NETHER_CHITIN = register(
        "nether_chitin",
        relativeDefense(
            ArmorMaterials.IRON,
            Map.ofEntries(
                Map.entry(ArmorItem.Type.CHESTPLATE, 1),
                Map.entry(ArmorItem.Type.BOOTS, 1)
            )
        ),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.NETHER_CHITIN.get()),
        0,
        0,
        false
    );

    public static final AVPDeferredHolder<ArmorMaterial> PLATED_ABERRANT_CHITIN = register(
        "plated_aberrant_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_ABERRANT_CHITIN.get()),
        1,
        0,
        false
    );

    public static final AVPDeferredHolder<ArmorMaterial> PLATED_CHITIN = register(
        "plated_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_CHITIN.get()),
        1,
        0,
        false
    );

    public static final AVPDeferredHolder<ArmorMaterial> PLATED_IRRADIATED_CHITIN = register(
        "plated_irradiated_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_IRRADIATED_CHITIN.get()),
        1,
        0,
        false
    );

    public static final AVPDeferredHolder<ArmorMaterial> PLATED_NETHER_CHITIN = register(
        "plated_nether_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_NETHER_CHITIN.get()),
        1,
        0,
        false
    );

    public static AVPDeferredHolder<ArmorMaterial> register(
        String id,
        Map<ArmorItem.Type, Integer> defensePoints,
        int enchantability,
        Supplier<Holder<SoundEvent>> equipSoundHolderSupplier,
        Supplier<Ingredient> repairIngredientSupplier,
        float toughness,
        float knockbackResistance,
        boolean dyeable
    ) {
        var resourceLocation = AlienResources.location(id);

        List<ArmorMaterial.Layer> layers = List.of(
            new ArmorMaterial.Layer(resourceLocation, "", dyeable)
        );

        return Services.REGISTRY.register(
            BuiltInRegistries.ARMOR_MATERIAL,
            AlienResources.location(id),
            () -> new ArmorMaterial(
                defensePoints,
                enchantability,
                equipSoundHolderSupplier.get(),
                repairIngredientSupplier,
                layers,
                toughness,
                knockbackResistance
            )
        );
    }

    public static Map<ArmorItem.Type, Integer> relativeDefense(
        Holder<ArmorMaterial> armorMaterialHolder,
        Map<ArmorItem.Type, Integer> additiveDefense
    ) {
        var armorMaterial = armorMaterialHolder.value();

        return Map.ofEntries(
            compute(ArmorItem.Type.HELMET, additiveDefense, armorMaterial),
            compute(ArmorItem.Type.CHESTPLATE, additiveDefense, armorMaterial),
            compute(ArmorItem.Type.LEGGINGS, additiveDefense, armorMaterial),
            compute(ArmorItem.Type.BOOTS, additiveDefense, armorMaterial)
        );
    }

    private static @NotNull Map.Entry<ArmorItem.Type, Integer> compute(
        ArmorItem.Type type,
        Map<ArmorItem.Type, Integer> additiveDefense,
        ArmorMaterial armorMaterial
    ) {
        return Map.entry(type, armorMaterial.getDefense(type) + additiveDefense.getOrDefault(type, 0));
    }

    public static void initialize() {}
}

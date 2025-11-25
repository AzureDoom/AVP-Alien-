package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.AlienResources;
import com.alien.common.registry.init.item.AlienItems;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
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

    private static final BLibRegistry<ArmorMaterial> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.ARMOR_MATERIAL);

    // Should be slightly stronger than iron.
    public static final BLibHolder<ArmorMaterial> ABERRANT_CHITIN = create(
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
    public static final BLibHolder<ArmorMaterial> CHITIN = create(
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
    public static final BLibHolder<ArmorMaterial> IRRADIATED_CHITIN = create(
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

    public static final BLibHolder<ArmorMaterial> NETHER_CHITIN = create(
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

    public static final BLibHolder<ArmorMaterial> PLATED_ABERRANT_CHITIN = create(
        "plated_aberrant_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_ABERRANT_CHITIN.get()),
        1,
        0,
        false
    );

    public static final BLibHolder<ArmorMaterial> PLATED_CHITIN = create(
        "plated_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_CHITIN.get()),
        1,
        0,
        false
    );

    public static final BLibHolder<ArmorMaterial> PLATED_IRRADIATED_CHITIN = create(
        "plated_irradiated_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_IRRADIATED_CHITIN.get()),
        1,
        0,
        false
    );

    public static final BLibHolder<ArmorMaterial> PLATED_NETHER_CHITIN = create(
        "plated_nether_chitin",
        relativeDefense(ArmorMaterials.DIAMOND, Map.of()),
        7,
        AlienSoundEvents.ITEM_ARMOR_EQUIP_CHITIN::getHolder,
        () -> Ingredient.of(AlienItems.PLATED_NETHER_CHITIN.get()),
        1,
        0,
        false
    );

    public static BLibHolder<ArmorMaterial> create(
        String path,
        Map<ArmorItem.Type, Integer> defensePoints,
        int enchantability,
        Supplier<Holder<SoundEvent>> equipSoundHolderSupplier,
        Supplier<Ingredient> repairIngredientSupplier,
        float toughness,
        float knockbackResistance,
        boolean dyeable
    ) {
        var resourceLocation = AlienResources.location(path);

        List<ArmorMaterial.Layer> layers = List.of(
            new ArmorMaterial.Layer(resourceLocation, "", dyeable)
        );

        return REGISTRY.createHolder(
            path,
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

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

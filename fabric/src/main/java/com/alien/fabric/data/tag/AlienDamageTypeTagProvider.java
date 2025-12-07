package com.alien.fabric.data.tag;

import com.alien.common.registry.key.AlienDamageTypeKeys;
import com.avp.common.registry.tag.AVPDamageTypesTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

public class AlienDamageTypeTagProvider extends FabricTagProvider<DamageType> {

    public AlienDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
            .add(
                AlienDamageTypeKeys.CHESTBURSTING
            );

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
            .add(
                AlienDamageTypeKeys.CHESTBURSTING
            );

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_INVULNERABILITY)
            .add(
                AlienDamageTypeKeys.CHESTBURSTING
            );

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_RESISTANCE)
            .add(
                AlienDamageTypeKeys.CHESTBURSTING
            );

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_SHIELD)
            .add(
                AlienDamageTypeKeys.CHESTBURSTING
            );

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_WOLF_ARMOR)
            .add(
                AlienDamageTypeKeys.CHESTBURSTING
            );

        getOrCreateTagBuilder(DamageTypeTags.NO_KNOCKBACK)
            .add(
                AlienDamageTypeKeys.ACID,
                AlienDamageTypeKeys.CHESTBURSTING,
                AlienDamageTypeKeys.SMOTHERING
            );

        getOrCreateTagBuilder(AVPDamageTypesTags.DOES_NOT_HURT_ALIENS)
            .add(
                AlienDamageTypeKeys.ACID,
                DamageTypes.DROWN,
                DamageTypes.FREEZE,
                DamageTypes.IN_WALL
            );
    }
}

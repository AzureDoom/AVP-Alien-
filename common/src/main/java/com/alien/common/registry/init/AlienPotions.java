package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.common.registry.init.item.AlienItems;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class AlienPotions {

    private static final BLibRegistry<Potion> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.POTION);

    private static final int THIRTY_SECONDS_IN_TICKS = 20 * 30;

    private static final int ONE_MINUTE_IN_TICKS = 20 * 60;

    private static final int THREE_MINUTES_IN_TICKS = 20 * 60 * 3;

    public static final BLibHolder<Potion> BLOOD_LOSS = REGISTRY.createHolder(
        "blood_loss",
        () -> new Potion(
            "blood_loss",
            new MobEffectInstance(AlienMobEffects.getBloodLossHolder(), ONE_MINUTE_IN_TICKS)
        )
    );

    public static final BLibHolder<Potion> LONG_BLOOD_LOSS = REGISTRY.createHolder(
        "long_blood_loss",
        () -> new Potion(
            "blood_loss",
            new MobEffectInstance(AlienMobEffects.getBloodLossHolder(), THREE_MINUTES_IN_TICKS)
        )
    );

    public static final BLibHolder<Potion> STRONG_BLOOD_LOSS = REGISTRY.createHolder(
        "strong_blood_loss",
        () -> new Potion(
            "blood_loss",
            new MobEffectInstance(AlienMobEffects.getBloodLossHolder(), THIRTY_SECONDS_IN_TICKS, 1)
        )
    );

    public static final BLibHolder<Potion> METAMORPHOSIS = REGISTRY.createHolder(
        "metamorphosis",
        () -> new Potion(
            "metamorphosis",
            new MobEffectInstance(AlienMobEffects.getMetamorphosisHolder(), ONE_MINUTE_IN_TICKS)
        )
    );

    public static final BLibHolder<Potion> LONG_METAMORPHOSIS = REGISTRY.createHolder(
        "long_metamorphosis",
        () -> new Potion(
            "metamorphosis",
            new MobEffectInstance(AlienMobEffects.getMetamorphosisHolder(), THREE_MINUTES_IN_TICKS)
        )
    );

    public static final BLibHolder<Potion> STRONG_METAMORPHOSIS = REGISTRY.createHolder(
        "strong_metamorphosis",
        () -> new Potion(
            "metamorphosis",
            new MobEffectInstance(AlienMobEffects.getMetamorphosisHolder(), THIRTY_SECONDS_IN_TICKS, 1)
        )
    );

    public static final BLibHolder<Potion> SCOURGE = REGISTRY.createHolder(
        "scourge",
        () -> new Potion(
            "scourge",
            new MobEffectInstance(AlienMobEffects.getScourgeHolder(), ONE_MINUTE_IN_TICKS)
        )
    );

    public static final BLibHolder<Potion> LONG_SCOURGE = REGISTRY.createHolder(
        "long_scourge",
        () -> new Potion(
            "scourge",
            new MobEffectInstance(AlienMobEffects.getScourgeHolder(), THREE_MINUTES_IN_TICKS)
        )
    );

    public static final BLibHolder<Potion> STRONG_SCOURGE = REGISTRY.createHolder(
        "strong_scourge",
        () -> new Potion(
            "scourge",
            new MobEffectInstance(AlienMobEffects.getScourgeHolder(), THIRTY_SECONDS_IN_TICKS, 1)
        )
    );

    private static void registerBrewingRecipes() {
        var brewingRegistry = Alien.MOD.registries().createBrewingRegistry();

        // Awkward + Chitin -> Blood Loss
        brewingRegistry.registerMix(
            Potions.AWKWARD,
            AlienItems.CHITIN,
            BLOOD_LOSS
        );

        // Blood Loss + Redstone -> Long Blood Loss
        brewingRegistry.registerMix(
            BLOOD_LOSS,
            () -> Items.REDSTONE,
            LONG_BLOOD_LOSS
        );

        // Blood Loss + Glowstone -> Strong Blood Loss
        brewingRegistry.registerMix(
            BLOOD_LOSS,
            () -> Items.GLOWSTONE_DUST,
            STRONG_BLOOD_LOSS
        );

        // Awkward + Raw Royal Jelly -> Metamorphosis
        brewingRegistry.registerMix(
            Potions.AWKWARD,
            AlienItems.RAW_ROYAL_JELLY,
            METAMORPHOSIS
        );

        // Metamorphosis + Redstone -> Long Metamorphosis
        brewingRegistry.registerMix(
            METAMORPHOSIS,
            () -> Items.REDSTONE,
            LONG_METAMORPHOSIS
        );

        // Metamorphosis + Glowstone -> Strong Metamorphosis
        brewingRegistry.registerMix(
            METAMORPHOSIS,
            () -> Items.GLOWSTONE_DUST,
            STRONG_METAMORPHOSIS
        );

        // Awkward + Raw Scourge Jelly -> Scourge
        brewingRegistry.registerMix(
            Potions.AWKWARD,
            AlienItems.RAW_SCOURGE_JELLY,
            SCOURGE
        );

        // Scourge + Redstone -> Long Scourge
        brewingRegistry.registerMix(
            SCOURGE,
            () -> Items.REDSTONE,
            LONG_SCOURGE
        );

        // Scourge + Glowstone -> Strong Scourge
        brewingRegistry.registerMix(
            SCOURGE,
            () -> Items.GLOWSTONE_DUST,
            STRONG_SCOURGE
        );

        // Awkward + Nether Chitin -> Fire Resistance
        brewingRegistry.registerMix(
            Potions.AWKWARD,
            AlienItems.NETHER_CHITIN,
            Potions.FIRE_RESISTANCE
        );

        // Fire Resistance + Nether Chitin -> Long Fire Resistance
        brewingRegistry.registerMix(
            Potions.FIRE_RESISTANCE,
            AlienItems.NETHER_CHITIN,
            Potions.LONG_FIRE_RESISTANCE
        );

        // Awkward + Nether Resin Ball -> Fire Resistance
        brewingRegistry.registerMix(
            Potions.AWKWARD,
            AlienItems.NETHER_RESIN_BALL,
            Potions.FIRE_RESISTANCE
        );

        // Fire Resistance + Nether Resin Ball -> Long Fire Resistance
        brewingRegistry.registerMix(
            Potions.FIRE_RESISTANCE,
            AlienItems.NETHER_RESIN_BALL,
            Potions.LONG_FIRE_RESISTANCE
        );
    }

    public static void initialize() {
        REGISTRY.registerAll();
        registerBrewingRecipes();
    }
}

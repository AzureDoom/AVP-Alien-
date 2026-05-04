package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.dismemberment.v1.Dismemberable;
import com.blib.api.common.dismemberment.v1.LimbDefinitionRegistry;
import com.blib.api.common.dismemberment.v1.LimbDismemberer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

/**
 * Server-side glue for the Ravager's one-shot charge attack.
 * <p>
 * Resolves the right model/texture for whichever Dismemberable target was just killed and asks BLib to detach the head
 * limb if one is registered. Targets that aren't Dismemberable, or that have no head limb defined, silently no-op.
 */
public final class RavagerHeadDismemberment {

    private RavagerHeadDismemberment() {}

    public static void tryDismemberHead(LivingEntity target) {
        if (target.level().isClientSide) {
            return;
        }

        if (!(target instanceof Dismemberable)) {
            return;
        }

        var resourcesOpt = resolveLimbResources(target);

        if (resourcesOpt == null) {
            return;
        }

        // TEMP: detach every registered limb so we can eyeball each one in-game. Revert to
        // detachFirstOfCategory(HEAD, ...) once the per-limb visuals are dialed in.
        for (var definition : LimbDefinitionRegistry.getDefinitions(target.getType())) {
            LimbDismemberer.detach(
                target,
                definition.id(),
                resourcesOpt.modelLocation(),
                resourcesOpt.textureLocation(),
                null
            );
        }
    }

    private static LimbResources resolveLimbResources(LivingEntity target) {
        if (!(target instanceof Alien alien)) {
            return null;
        }

        var baseName = baseNameForType(target);

        if (baseName == null) {
            return null;
        }

        var variant = alien.getVariant();
        var modelLocation = AlienResources.entityGeoModelLocation(baseName);
        var textureLocation = AlienResources.entityTextureLocation(prefixForVariant(variant) + baseName);

        return new LimbResources(modelLocation, textureLocation);
    }

    private static String baseNameForType(LivingEntity target) {
        var typeId = target.getType().builtInRegistryHolder().key().location().getPath();

        // Strip variant prefixes so we land on the canonical model name.
        for (var prefix : VARIANT_PREFIXES) {
            if (typeId.startsWith(prefix)) {
                return typeId.substring(prefix.length());
            }
        }

        return typeId;
    }

    private static String prefixForVariant(AlienVariant variant) {
        return switch (variant) {
            case NORMAL -> "";
            case NETHER -> "nether_";
            case ABERRANT -> "aberrant_";
            case IRRADIATED -> "irradiated_";
        };
    }

    private static final String[] VARIANT_PREFIXES = {
        "aberrant_",
        "irradiated_",
        "nether_",
        "royal_aberrant_",
        "royal_nether_",
        "royal_"
    };

    private record LimbResources(
        ResourceLocation modelLocation,
        ResourceLocation textureLocation
    ) {}
}

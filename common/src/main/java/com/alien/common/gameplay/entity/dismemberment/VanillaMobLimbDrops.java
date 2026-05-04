package com.alien.common.gameplay.entity.dismemberment;

import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbInteractionRegistry;
import com.blib.api.common.dismemberment.v1.entity.DismemberedLimbEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

/**
 * Right-click drops for HEAD limbs of vanilla mobs we register limb defs for. Maps each mob to the closest
 * silhouette-matching vanilla skull/head item — mobs without a corresponding head item (illagers, villagers, livestock)
 * fall through and produce no drop.
 */
public final class VanillaMobLimbDrops {

    private VanillaMobLimbDrops() {}

    public static void initialize() {
        LimbInteractionRegistry.register(VanillaMobLimbDrops::isVanillaMobHeadLimb, VanillaMobLimbDrops::headForLimb);
    }

    private static boolean isVanillaMobHeadLimb(DismemberedLimbEntity limb) {
        var sourceType = limb.getSourceEntityType();

        if (sourceType == null || vanillaHeadItemFor(sourceType) == null) {
            return false;
        }

        var definition = limb.resolveLimbDefinition();
        return definition != null && definition.category().equals(LimbCategories.HEAD);
    }

    private static ItemStack headForLimb(DismemberedLimbEntity limb) {
        var item = vanillaHeadItemFor(limb.getSourceEntityType());
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    private static @Nullable Item vanillaHeadItemFor(@Nullable EntityType<?> sourceType) {
        if (sourceType == null) {
            return null;
        }

        // Zombie family — all share the zombie head silhouette.
        if (
            sourceType == EntityType.ZOMBIE
                || sourceType == EntityType.HUSK
                || sourceType == EntityType.DROWNED
                || sourceType == EntityType.ZOMBIE_VILLAGER
        ) {
            return Items.ZOMBIE_HEAD;
        }

        // Skeletons share the skeleton skull.
        if (sourceType == EntityType.SKELETON || sourceType == EntityType.STRAY) {
            return Items.SKELETON_SKULL;
        }

        if (sourceType == EntityType.WITHER_SKELETON) {
            return Items.WITHER_SKELETON_SKULL;
        }

        if (sourceType == EntityType.CREEPER) {
            return Items.CREEPER_HEAD;
        }

        // Piglins (incl. brutes and zombified) share the piglin head silhouette.
        if (
            sourceType == EntityType.PIGLIN
                || sourceType == EntityType.PIGLIN_BRUTE
                || sourceType == EntityType.ZOMBIFIED_PIGLIN
        ) {
            return Items.PIGLIN_HEAD;
        }

        // Illagers, villagers, livestock — no matching skull item, fall through.
        return null;
    }
}

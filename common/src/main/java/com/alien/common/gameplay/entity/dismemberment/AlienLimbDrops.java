package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.item.AlienItems;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbInteractionRegistry;
import com.blib.api.common.dismemberment.v1.entity.DismemberedLimbEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Right-click drops for xenomorph limb fragments. Crusher and queen HEAD limbs drop their matching trophy head items.
 * Royal-class xenomorphs without a trophy head drop <em>plated</em> chitin from their HEAD limbs to reflect the heavier
 * armor on those forms; everything else (and other limb categories on royals) drops the regular chitin variant matching
 * the source's affliction (irradiated/nether/aberrant/normal).
 */
public final class AlienLimbDrops {

    private static final ResourceLocation CRUSHER_HEAD_LIMB = AlienResources.location("crusher_head");

    private static final ResourceLocation QUEEN_HEAD_LIMB = AlienResources.location("queen_head");

    private AlienLimbDrops() {}

    public static void initialize() {
        // Specific trophy-head rules first so these limbs do not fall through to chitin.
        LimbInteractionRegistry.register(AlienLimbDrops::isTrophyHeadLimb, AlienLimbDrops::trophyHeadForLimb);
        // Specific rule first: royal-class HEAD limbs upgrade to plated chitin.
        LimbInteractionRegistry.register(AlienLimbDrops::isRoyalHeadLimb, AlienLimbDrops::platedChitinForLimb);
        // Fallback: any xenomorph limb drops the matching regular chitin.
        LimbInteractionRegistry.register(AlienLimbDrops::isXenomorphLimb, AlienLimbDrops::chitinForLimb);
    }

    private static boolean isXenomorphLimb(DismemberedLimbEntity limb) {
        var sourceType = limb.getSourceEntityType();
        return sourceType != null && sourceType.is(AlienEntityTypeTags.XENOMORPHS);
    }

    private static boolean isTrophyHeadLimb(DismemberedLimbEntity limb) {
        return trophyHeadItemFor(limb) != null;
    }

    private static boolean isRoyalHeadLimb(DismemberedLimbEntity limb) {
        var sourceType = limb.getSourceEntityType();

        if (sourceType == null || !dropsPlatedChitin(sourceType)) {
            return false;
        }

        var definition = limb.resolveLimbDefinition();
        return definition != null && definition.category().equals(LimbCategories.HEAD);
    }

    private static boolean dropsPlatedChitin(EntityType<?> sourceType) {
        return sourceType.is(AlienEntityTypeTags.PRAETORIANS)
            || sourceType.is(AlienEntityTypeTags.QUEENS)
            || sourceType.is(AlienEntityTypeTags.EMPRESSES)
            || sourceType.is(AlienEntityTypeTags.HARBINGERS);
    }

    private static ItemStack chitinForLimb(DismemberedLimbEntity limb) {
        var item = chitinItemFor(limb.getSourceEntityType());
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    private static ItemStack platedChitinForLimb(DismemberedLimbEntity limb) {
        var item = platedChitinItemFor(limb.getSourceEntityType());
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    private static ItemStack trophyHeadForLimb(DismemberedLimbEntity limb) {
        var item = trophyHeadItemFor(limb);
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    private static @Nullable Item trophyHeadItemFor(DismemberedLimbEntity limb) {
        var sourceType = limb.getSourceEntityType();
        var limbId = limb.getLimbId();

        if (sourceType == null || limbId == null) {
            return null;
        }

        if (limbId.equals(CRUSHER_HEAD_LIMB)) {
            return crusherHeadItemFor(sourceType);
        }

        if (limbId.equals(QUEEN_HEAD_LIMB)) {
            return queenHeadItemFor(sourceType);
        }

        return null;
    }

    private static @Nullable Item crusherHeadItemFor(EntityType<?> sourceType) {
        if (sourceType == AlienEntityTypes.IRRADIATED_CRUSHER.get()) {
            return AlienItems.IRRADIATED_CRUSHER_HEAD.get();
        }

        if (sourceType == AlienEntityTypes.NETHER_CRUSHER.get()) {
            return AlienItems.NETHER_CRUSHER_HEAD.get();
        }

        if (sourceType == AlienEntityTypes.ABERRANT_CRUSHER.get()) {
            return AlienItems.ABERRANT_CRUSHER_HEAD.get();
        }

        if (sourceType == AlienEntityTypes.CRUSHER.get()) {
            return AlienItems.CRUSHER_HEAD.get();
        }

        return null;
    }

    private static @Nullable Item queenHeadItemFor(EntityType<?> sourceType) {
        if (sourceType == AlienEntityTypes.IRRADIATED_QUEEN.get()) {
            return AlienItems.IRRADIATED_QUEEN_HEAD.get();
        }

        if (sourceType == AlienEntityTypes.NETHER_QUEEN.get()) {
            return AlienItems.NETHER_QUEEN_HEAD.get();
        }

        if (sourceType == AlienEntityTypes.ABERRANT_QUEEN.get()) {
            return AlienItems.ABERRANT_QUEEN_HEAD.get();
        }

        if (sourceType == AlienEntityTypes.QUEEN.get()) {
            return AlienItems.QUEEN_HEAD.get();
        }

        return null;
    }

    private static @Nullable Item chitinItemFor(@Nullable EntityType<?> sourceType) {
        if (sourceType == null) {
            return null;
        }

        if (sourceType.is(AlienEntityTypeTags.IRRADIATED_ALIENS)) {
            return AlienItems.IRRADIATED_CHITIN.get();
        }

        if (sourceType.is(AlienEntityTypeTags.NETHER_ALIENS)) {
            return AlienItems.NETHER_CHITIN.get();
        }

        if (sourceType.is(AlienEntityTypeTags.ABERRANT_ALIENS)) {
            return AlienItems.ABERRANT_CHITIN.get();
        }

        return AlienItems.CHITIN.get();
    }

    private static @Nullable Item platedChitinItemFor(@Nullable EntityType<?> sourceType) {
        if (sourceType == null) {
            return null;
        }

        if (sourceType.is(AlienEntityTypeTags.IRRADIATED_ALIENS)) {
            return AlienItems.PLATED_IRRADIATED_CHITIN.get();
        }

        if (sourceType.is(AlienEntityTypeTags.NETHER_ALIENS)) {
            return AlienItems.PLATED_NETHER_CHITIN.get();
        }

        if (sourceType.is(AlienEntityTypeTags.ABERRANT_ALIENS)) {
            return AlienItems.PLATED_ABERRANT_CHITIN.get();
        }

        return AlienItems.PLATED_CHITIN.get();
    }
}

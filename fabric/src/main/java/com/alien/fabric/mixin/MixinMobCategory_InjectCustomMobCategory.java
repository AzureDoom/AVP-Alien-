package com.alien.fabric.mixin;

import com.alien.common.registry.init.AlienMobCategoryData;
import com.avp.fabric.service.FabricBridgeService;
import com.avp.service.Services;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

// TODO: Rename this.
@Mixin(MobCategory.class)
public abstract class MixinMobCategory_InjectCustomMobCategory {

    @Unique
    private static final int OPCODE_PUTSTATIC = 179;

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static MobCategory newMobCategory(
        String internalName,
        int internalId,
        String name,
        final int j,
        final boolean bl,
        final boolean bl2,
        final int k
    ) {
        throw new AssertionError();
    }

    private static MobCategory newMobCategory(
        String internalName,
        int internalId,
        AlienMobCategoryData.Data data
    ) {
        return newMobCategory(
            internalName,
            internalId,
            data.name(),
            data.max(),
            data.isFriendly(),
            data.isPersistent(),
            data.despawnDistance()
        );
    }

    @Shadow
    private static @Final @Mutable MobCategory[] $VALUES;

    @Inject(
        method = "<clinit>", at = @At(
            value = "FIELD", opcode = OPCODE_PUTSTATIC,
            target = "net/minecraft/world/entity/MobCategory.$VALUES:[Lnet/minecraft/world/entity/MobCategory;", shift = At.Shift.AFTER
        )
    )
    private static void addCustomMobCategory(CallbackInfo ci) {
        var categories = new ArrayList<>(Arrays.asList($VALUES));
        var last = categories.get(categories.size() - 1);
        var nextOrdinal = last.ordinal() + 1;

        var alien = newMobCategory("AVP_ALIEN", nextOrdinal++, AlienMobCategoryData.ALIEN);

        var fabricBridgeService = (FabricBridgeService) Services.BRIDGE;

        fabricBridgeService.setAlienMobCategory(alien);

        categories.add(alien);

        $VALUES = categories.toArray(new MobCategory[0]);
    }
}

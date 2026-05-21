package com.alien.fabric.data.dismemberment;

import com.alien.AlienResources;
import com.alien.common.registry.init.AlienEntityTypes;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.datagen.LimbDefinitionDataProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;

public final class AlienLimbDefinitionDataProvider extends LimbDefinitionDataProvider {

    private static final ResourceLocation DRONE_TEMPLATE = AlienResources.location("drone_template");

    public AlienLimbDefinitionDataProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
        template(DRONE_TEMPLATE)
            .fatalLimb(limb("drone_head"), LimbCategories.HEAD)
            .limb(limb("drone_left_arm"), LimbCategories.ARM)
            .limb(limb("drone_right_arm"), LimbCategories.ARM)
            .limb(limb("drone_left_leg"), LimbCategories.LEG)
            .limb(limb("drone_right_leg"), LimbCategories.LEG)
            .limb(limb("drone_tail"), LimbCategories.TAIL);

        entity(AlienEntityTypes.DRONE).parent(DRONE_TEMPLATE);
        entity(AlienEntityTypes.ABERRANT_DRONE).parent(DRONE_TEMPLATE);
        entity(AlienEntityTypes.IRRADIATED_DRONE).parent(DRONE_TEMPLATE);
        entity(AlienEntityTypes.NETHER_DRONE).parent(DRONE_TEMPLATE);
    }

    private static ResourceLocation limb(String path) {
        return AlienResources.location(path);
    }
}

package com.alien.fabric.data.dismemberment;

import com.alien.AlienResources;
import com.alien.common.registry.init.AlienEntityTypes;
import com.blib.api.common.dismemberment.v1.datagen.LimbVisualsDataProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;

public final class AlienLimbVisualsDataProvider extends LimbVisualsDataProvider {

    private static final ResourceLocation DRONE_TEMPLATE = AlienResources.location("drone_template");

    public AlienLimbVisualsDataProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
        template(DRONE_TEMPLATE)
            .visual(limb("drone_head"), "gHead", visuals -> visuals.renderOffset(0.0, 0.125, 0.0))
            .visual(
                limb("drone_left_arm"),
                "gLeftShoulder",
                visuals -> visuals.renderOffset(0.0, 0.25, 0.0).renderRotation(-135.0, 0.0, 0.0)
            )
            .visual(
                limb("drone_right_arm"),
                "gRightShoulder",
                visuals -> visuals.renderOffset(0.0, 0.25, 0.0).renderRotation(-135.0, 0.0, 0.0)
            )
            .visual(
                limb("drone_left_leg"),
                "gLeftLeg",
                visuals -> visuals.renderOffset(0.0, 0.5, 0.0).renderRotation(-90.0, 0.0, 0.0)
            )
            .visual(
                limb("drone_right_leg"),
                "gRightLeg",
                visuals -> visuals.renderOffset(0.0, 0.5, 0.0).renderRotation(-90.0, 0.0, 0.0)
            )
            .visual(limb("drone_tail"), "gTail1");

        entity(AlienEntityTypes.DRONE).parent(DRONE_TEMPLATE);
        entity(AlienEntityTypes.ABERRANT_DRONE).parent(DRONE_TEMPLATE);
        entity(AlienEntityTypes.IRRADIATED_DRONE).parent(DRONE_TEMPLATE);
        entity(AlienEntityTypes.NETHER_DRONE).parent(DRONE_TEMPLATE);
    }

    private static ResourceLocation limb(String path) {
        return AlienResources.location(path);
    }
}

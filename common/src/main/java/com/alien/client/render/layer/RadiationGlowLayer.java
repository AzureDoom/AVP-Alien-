package com.alien.client.render.layer;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.blib.azurelib.common.render.AzRendererPipelineContext;
import com.blib.azurelib.common.render.layer.AzAutoGlowingLayer;

import java.util.UUID;

public class RadiationGlowLayer<T> extends AzAutoGlowingLayer<UUID, T> {

    @Override
    public void render(AzRendererPipelineContext<UUID, T> context) {
        var animatable = context.animatable();
        if (animatable instanceof Alien alien && alien.isIrradiated()) {
            super.render(context);
        }
    }
}

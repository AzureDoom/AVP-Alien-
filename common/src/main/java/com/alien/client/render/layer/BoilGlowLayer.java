package com.alien.client.render.layer;

import com.blib.azurelib.common.render.AzRendererPipelineContext;
import com.blib.azurelib.common.render.layer.AzAutoGlowingLayer;

import java.util.UUID;

public class BoilGlowLayer<T> extends AzAutoGlowingLayer<UUID, T> {

    @Override
    public void render(AzRendererPipelineContext<UUID, T> context) {
        super.render(context);
    }
}

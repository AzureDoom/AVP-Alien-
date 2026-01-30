package com.alien.client.render.layer;

import com.blib.api.client.render.v1.AzRendererPipelineContext;
import com.blib.api.client.render.v1.layer.AzAutoGlowingLayer;

import java.util.UUID;

public class SpitGlandGlowLayer<T> extends AzAutoGlowingLayer<UUID, T> {

    @Override
    public void render(AzRendererPipelineContext<UUID, T> context) {
        super.render(context);
    }
}

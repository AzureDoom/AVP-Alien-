package com.alien.common.gameplay.block.crusher;

/**
 * Identifies which crusher-texture variant a crusher-head block / block-entity uses for rendering. Matches the
 * {@code textures/entity/<entityName>.png} stem the existing {@link com.alien.client.render.item.CrusherHeadRenderers}
 * already references for items, so the block renderer can reuse the same texture resolution path.
 */
public enum CrusherHeadVariant {

    CRUSHER("crusher"),
    ABERRANT("aberrant_crusher"),
    IRRADIATED("irradiated_crusher"),
    NETHER("nether_crusher");

    private final String entityTextureName;

    CrusherHeadVariant(String entityTextureName) {
        this.entityTextureName = entityTextureName;
    }

    public String entityTextureName() {
        return entityTextureName;
    }
}

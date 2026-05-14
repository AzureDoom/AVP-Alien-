package com.alien.common.gameplay.block.crusher;

/**
 * Identifies which crusher-texture variant a crusher-head block / block-entity uses for rendering. The
 * {@link #entityTextureName} maps to {@code textures/entity/<entityName>.png} — the same stem the asset-driven item
 * renderer configs use under {@code assets/avp_alien/blib/item_renderers/}, so the block renderer can reuse the same
 * texture resolution path.
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

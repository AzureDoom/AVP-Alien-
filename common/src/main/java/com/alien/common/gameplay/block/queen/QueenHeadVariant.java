package com.alien.common.gameplay.block.queen;

/**
 * Identifies which queen-texture variant a queen-head block / block-entity uses for rendering. Matches the
 * {@code textures/entity/<entityName>.png} stem the existing {@link com.alien.client.render.item.QueenHeadRenderers}
 * already references for items, so the block renderer can reuse the same texture resolution path.
 */
public enum QueenHeadVariant {

    QUEEN("queen"),
    ABERRANT("aberrant_queen"),
    IRRADIATED("irradiated_queen"),
    NETHER("nether_queen");

    private final String entityTextureName;

    QueenHeadVariant(String entityTextureName) {
        this.entityTextureName = entityTextureName;
    }

    public String entityTextureName() {
        return entityTextureName;
    }
}

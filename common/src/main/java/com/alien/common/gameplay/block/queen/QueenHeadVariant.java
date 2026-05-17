package com.alien.common.gameplay.block.queen;

/**
 * Identifies which queen-texture variant a queen-head block / block-entity uses for rendering. The
 * {@link #entityTextureName} maps to {@code textures/entity/<entityName>.png} — the same stem the asset-driven item
 * renderer configs use under {@code assets/avp_alien/blib/item_renderers/}, so the block renderer can reuse the same
 * texture resolution path.
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

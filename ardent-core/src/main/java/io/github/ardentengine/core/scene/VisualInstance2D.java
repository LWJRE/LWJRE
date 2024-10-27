package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Material;

/**
 * Base class for all visual 2D nodes.
 */
public class VisualInstance2D extends Node2D {

    /**
     * True if this visual instance is visible.
     * The visual instance won't be rendered if {@code visible} is set to false.
     */
    private boolean visible = true;

    // TODO: Don't render the children if visible is set to false

    /**
     * The material used by this visual instance.
     * Can be used to customize how visual instances are rendered.
     * Can be set to null to tell the rendering api to use the default material.
     */
    private Material material;

    // TODO: Add an "inherit material" option to allow visual instances to use their ancestor's material

    // TODO: Add modulate color

    /**
     * Getter method for {@link VisualInstance2D#visible}.
     *
     * @return True if this visual instance is visible, otherwise false.
     */
    public final boolean visible() {
        return this.visible;
    }

    /**
     * Setter method for {@link VisualInstance2D#visible}.
     *
     * @param visible True if this visual instance is visible, otherwise false.
     */
    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Getter method for {@link VisualInstance2D#material}.
     *
     * @return The material used by this visual instance. Can be null.
     */
    public final Material material() {
        return this.material;
    }

    /**
     * Setter method for {@link VisualInstance2D#material}.
     *
     * @param material The material used by this visual instance. Can be set to null to tell the rendering api to use the default material.
     */
    public final void setMaterial(Material material) {
        this.material = material;
    }
}
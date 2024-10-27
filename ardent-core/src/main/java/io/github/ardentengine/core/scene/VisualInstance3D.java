package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Material;

/**
 * Base class for all visual 3D nodes.
 */
public class VisualInstance3D extends Node3D {

    /**
     * True if this visual instance is visible.
     * The visual instance won't be rendered if {@code visible} is set to false.
     */
    private boolean visible = true;

    // TODO: Setting visible to false should also make all children invisible

    /**
     * Material override for the whole geometry.
     * If a material is assigned to this property, it will be used instead of the material set in any mesh.
     */
    private Material materialOverride = null;

    // TODO: Visibility range to cull objects far from the camera

    // TODO: Cull layers to only render objects in the same layer as the camera

    /**
     * Getter method for {@link VisualInstance3D#visible}.
     *
     * @return True if this visual instance is visible, otherwise false.
     */
    public final boolean visible() {
        return this.visible;
    }

    /**
     * Setter method for {@link VisualInstance3D#visible}.
     *
     * @param visible True if this visual instance is visible, otherwise false.
     */
    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Getter method for {@link VisualInstance3D#materialOverride}.
     *
     * @return The material override for this object. Can be null.
     */
    public final Material materialOverride() {
        return this.materialOverride;
    }

    /**
     * Setter method for {@link VisualInstance3D#materialOverride}.
     *
     * @param materialOverride Material override for the whole geometry. Can be set to null to use the material set in the mesh.
     */
    public final void setMaterialOverride(Material materialOverride) {
        this.materialOverride = materialOverride;
    }
}
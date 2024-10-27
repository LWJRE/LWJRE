package io.github.ardentengine.core.rendering;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for materials.
 * Materials are used to apply visual properties to an object, such as color, roughness, and reflectivity.
 * <p>
 *     Every material contains a {@link Shader} used for rendering and a set of properties.
 * </p>
 */
public sealed class Material permits Material3D, ShaderMaterial {

    /**
     * Map of material parameters.
     * These parameters are loaded into the shader as uniform variables.
     */
    protected final HashMap<String, Object> parameters = new HashMap<>();

    /**
     * Returns a map containing all the material's properties.
     * <p>
     *     Modifying the returned map does not affect the material.
     * </p>
     *
     * @return A map containing all the material's properties.
     */
    public final Map<String, Object> getParameters() {
        return new HashMap<>(this.parameters);
    }
}
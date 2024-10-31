package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.math.Color;
import io.github.ardentengine.core.resources.YamlMappingDeserializer;

import java.util.Map;

/**
 * A material for 3D objects that uses the Phong lighting model.
 * <p>
 *     Uses the {@code default_shader_3d} shader.
 * </p>
 */
public final class Material3D extends Material {

    /**
     * Constructs a Material with the default properties.
     */
    public Material3D() {
        this.parameters.put("ambient", new Color(0.2f, 0.2f, 0.2f, 1.0f));
        this.parameters.put("diffuse", Color.WHITE);
        this.parameters.put("specular", Color.WHITE);
        this.parameters.put("shininess", 32.0f);
    }

    /**
     * Returns the material's ambient color.
     * Defines what color a surface reflects under ambient lighting.
     *
     * @return The material's ambient color.
     */
    public Color ambient() {
        return (Color) this.parameters.getOrDefault("ambient", Color.BLACK);
    }

    /**
     * Sets the material's ambient color.
     * Defines what color a surface reflects under ambient lighting.
     *
     * @param ambient The material's ambient color.
     */
    public void setAmbient(Color ambient) {
        this.parameters.put("ambient", ambient);
    }

    /**
     * Returns the material's diffuse color.
     * Defines the color of the surface under diffuse lighting.
     *
     * @return The material's diffuse color.
     */
    public Color diffuse() {
        return (Color) this.parameters.getOrDefault("diffuse", Color.BLACK);
    }

    /**
     * Sets the material's diffuse color.
     * Defines the color of the surface under diffuse lighting.
     *
     * @param diffuse The material's diffuse color.
     */
    public void setDiffuse(Color diffuse) {
        this.parameters.put("diffuse", diffuse);
    }

    /**
     * Returns the material's specular color.
     * Defines the color of the specular highlight on the surface.
     *
     * @return The material's specular color.
     */
    public Color specular() {
        return (Color) this.parameters.getOrDefault("specular", Color.BLACK);
    }

    /**
     * Sets the material's specular color.
     * Defines the color of the specular highlight on the surface.
     *
     * @param specular The material's specular color.
     */
    public void setSpecular(Color specular) {
        this.parameters.put("specular", specular);
    }

    /**
     * Returns the material's shininess.
     * Impacts the scattering/radius of the specular highlight.
     *
     * @return The material's shininess.
     */
    public float shininess() {
        return (float) this.parameters.getOrDefault("shininess", 0.0f);
    }

    /**
     * Sets the material's shininess.
     * Impacts the scattering/radius of the specular highlight.
     *
     * @param shininess The material's shininess.
     */
    public void setShininess(float shininess) {
        this.parameters.put("shininess", shininess);
    }

    /**
     * Deserializer class for {@link Material3D} used to ensure the correct material parameters are used.
     */
    public static class Deserializer implements YamlMappingDeserializer {

        @Override
        public Object deserialize(Map<Object, Object> map) {
            var material = new Material3D();
            material.setAmbient((Color) map.getOrDefault("ambient", material.ambient()));
            material.setDiffuse((Color) map.getOrDefault("diffuse", material.diffuse()));
            material.setSpecular((Color) map.getOrDefault("specular", material.specular()));
            material.setShininess((float) map.getOrDefault("shininess", material.shininess()));
            return material;
        }

        @Override
        public Class<?> getTag() {
            return Material3D.class;
        }
    }
}
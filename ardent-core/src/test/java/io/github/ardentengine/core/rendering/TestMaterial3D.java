package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.math.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMaterial3D {

    @Test
    public void testAmbientColor() {
        var material = new Material3D();
        var color = new Color(0.9f, 0.8f, 0.7f);
        material.setAmbient(color);
        Assertions.assertEquals(color, material.ambient());
    }

    @Test
    public void testDiffuseColor() {
        var material = new Material3D();
        var color = new Color(0.9f, 0.8f, 0.7f);
        material.setDiffuse(color);
        Assertions.assertEquals(color, material.diffuse());
    }

    @Test
    public void testSpecularColor() {
        var material = new Material3D();
        var color = new Color(0.9f, 0.8f, 0.7f);
        material.setSpecular(color);
        Assertions.assertEquals(color, material.specular());
    }

    @Test
    public void testShininess() {
        var material = new Material3D();
        material.setShininess(0.75f);
        Assertions.assertEquals(0.75f, material.shininess());
    }
}
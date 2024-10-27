package io.github.ardentengine.core.rendering;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestShaderMaterial {

    @Test
    public void testFloatParameter() {
        var material = new ShaderMaterial();
        material.setParameter("name", 0.75f);
        Assertions.assertEquals(0.75f, material.getParameter("name"));
    }

    // TODO: Finish tests
}
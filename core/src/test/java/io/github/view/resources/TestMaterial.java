package io.github.view.resources;

import io.github.view.math.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMaterial {

	@Test
	public void testLoadMaterial() {
		Material material = Material.getOrLoad("/materials/test_material.yaml");
		Assertions.assertEquals(material.getAmbient(), new Color(1.0f, 0.5f, 0.25f));
		Assertions.assertEquals(material.getDiffuse(), new Color(0.25f, 1.0f, 0.5f));
		Assertions.assertEquals(material.getSpecular(), new Color(0.5f, 0.25f, 1.0f));
		Assertions.assertEquals(material.getShininess(), 0.42f);
	}
}

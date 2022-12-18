package io.github.view.resources;

import io.github.view.math.Color;
import io.github.view.utils.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.HashMap;

public class Material {

	private static final HashMap<String, Material> MATERIALS = new HashMap<>();

	public static Material getOrLoad(String file) {
		if(MATERIALS.containsKey(file)) {
			return MATERIALS.get(file);
		} else {
			Material material = FileUtils.readFile(file, inputStream -> new Yaml(new Constructor(Material.class)).load(inputStream), exception -> {
				System.err.println("Error reading material file " + file);
				exception.printStackTrace();
				return new Material();
			});
			MATERIALS.put(file, material);
			return material;
		}
	}

	private Color ambient = Color.WHITE;
	private Color diffuse = Color.WHITE;
	private Color specular = Color.BLACK;
	private float shininess = 0.0f;

	public void setAmbient(Color ambient) {
		if(ambient == null) this.ambient = Color.BLACK;
		else this.ambient = ambient;
	}

	public Color getAmbient() {
		return this.ambient;
	}

	public void setDiffuse(Color diffuse) {
		if(diffuse == null) this.diffuse = Color.BLACK;
		else this.diffuse = diffuse;
	}

	public Color getDiffuse() {
		return this.diffuse;
	}

	public void setSpecular(Color specular) {
		if(specular == null) this.specular = Color.BLACK;
		else this.specular = specular;
	}

	public Color getSpecular() {
		return this.specular;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	public float getShininess() {
		return this.shininess;
	}

	@Override
	public String toString() {
		return "Material{ambient=" + this.ambient + ", diffuse=" + this.diffuse + ", specular=" + this.specular + ", shininess=" + this.shininess + "}";
	}
}

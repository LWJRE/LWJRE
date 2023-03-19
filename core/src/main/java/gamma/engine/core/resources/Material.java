package gamma.engine.core.resources;

import vecmatlib.color.Color;

public class Material {

	public Color ambient;
	public Color diffuse;
	public Color specular;
	public float shininess;

	public Material(Color ambient, Color diffuse, Color specular, float shininess) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.shininess = shininess;
	}

	public Material() {
		this(Color.White(), Color.White(), Color.White(), 0.0f);
	}
}

package gamma.engine.core.resources;

import io.github.hexagonnico.vecmatlib.color.Color4f;

public class Material {

	public Color4f ambient;
	public Color4f diffuse;
	public Color4f specular;
	public float shininess;

	public Material(Color4f ambient, Color4f diffuse, Color4f specular, float shininess) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.shininess = shininess;
	}

	public Material() {
		this(Color4f.White(), Color4f.White(), Color4f.White(), 0.0f);
	}
}

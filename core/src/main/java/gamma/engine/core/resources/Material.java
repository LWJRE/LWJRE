package gamma.engine.core.resources;

import gamma.engine.core.utils.Color;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;

public class Material {

	private static final HashMap<String, Material> MATERIALS = new HashMap<>();

	public static Material getOrLoad(String file) {
		if(MATERIALS.containsKey(file)) {
			return MATERIALS.get(file);
		} else {
			Material material = new Yaml().loadAs(Material.class.getResourceAsStream(file), Material.class);
			MATERIALS.put(file, material);
			return material;
		}
	}

	// TODO: Finish material

	public Color ambient = Color.WHITE;
	public Color diffuse = Color.WHITE;
	public Color specular = Color.BLACK;
	public float shininess = 0.0f;

	@Override
	public String toString() {
		return "Material{ambient=" + this.ambient + ", diffuse=" + this.diffuse + ", specular=" + this.specular + ", shininess=" + this.shininess + "}";
	}
}

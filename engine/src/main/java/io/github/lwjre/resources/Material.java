package io.github.lwjre.resources;

import io.github.hexagonnico.vecmatlib.color.Color4f;

import java.util.HashMap;
import java.util.Objects;

public class Material {

	public static final String NAME = "name";
	public static final String TWOSIDED = "twosided";
	public static final String SHADING_MODEL = "shadingm";
	public static final String ENABLE_WIREFRAME = "wireframe";
	public static final String BLEND_FUNC = "blend";
	public static final String OPACITY = "opacity";
	public static final String TRANSPARENCYFACTOR = "transparencyfactor";
	public static final String BUMPSCALING = "bumpscaling";
	public static final String SHININESS = "shininess";
	public static final String REFLECTIVITY = "reflectivity";
	public static final String SHININESS_STRENGTH = "shinpercent";
	public static final String REFRACTI = "refracti";
	public static final String COLOR_DIFFUSE = "diffuse";
	public static final String COLOR_AMBIENT = "ambient";
	public static final String COLOR_SPECULAR = "specular";
	public static final String COLOR_EMISSIVE = "emissive";
	public static final String COLOR_TRANSPARENT = "transparent";
	public static final String COLOR_REFLECTIVE = "reflective";
	public static final String USE_COLOR_MAP = "useColorMap";
	public static final String BASE_COLOR = "base";
	public static final String USE_METALLIC_MAP = "useMetallicMap";
	public static final String METALLIC_FACTOR = "metallicFactor";
	public static final String USE_ROUGHNESS_MAP = "useRoughnessMap";
	public static final String ROUGHNESS_FACTOR = "roughnessFactor";
	public static final String ANISOTROPY_FACTOR = "anisotropyFactor";
	public static final String SPECULAR_FACTOR = "specularFactor";
	public static final String GLOSSINESS_FACTOR = "glossinessFactor";
	public static final String SHEEN_COLOR_FACTOR = "sheen.factor";
	public static final String SHEEN_ROUGHNESS_FACTOR = "sheen.roughnessFactor";
	public static final String CLEARCOAT_FACTOR = "clearcoat.factor";
	public static final String CLEARCOAT_ROUGHNESS_FACTOR = "clearcoat.roughnessFactor";
	public static final String TRANSMISSION_FACTOR = "transmission.factor";
	public static final String VOLUME_THICKNESS_FACTOR = "volume.thicknessFactor";
	public static final String VOLUME_ATTENUATION_DISTANCE = "volume.attenuationDistance";
	public static final String VOLUME_ATTENUATION_COLOR = "volume.attenuationColor";
	public static final String USE_EMISSIVE_MAP = "useEmissiveMap";
	public static final String EMISSIVE_INTENSITY = "emissiveIntensity";
	public static final String USE_AO_MAP = "useAOMap";

	private final HashMap<String, Object> params = new HashMap<>();

	public Object setParam(String key, Object param) {
		return this.params.put(key, param);
	}

	public float getFloat(String key) {
		Object param = this.params.get(key);
		if(param instanceof Float) {
			return (Float) param;
		}
		return 0.0f;
	}

	public Color4f getColor(String key) {
		Object param = this.params.get(key);
		if(param instanceof Color4f) {
			return (Color4f) param;
		}
		return Color4f.Black();
	}

	public String getString(String key) {
		Object param = this.params.get(key);
		if(param instanceof String) {
			return (String) param;
		}
		return "";
	}

	public String name() {
		return this.getString(NAME);
	}

	public Color4f ambientColor() {
		return this.getColor(COLOR_AMBIENT);
	}

	public Color4f diffuseColor() {
		return this.getColor(COLOR_DIFFUSE);
	}

	public Color4f specularColor() {
		return this.getColor(COLOR_SPECULAR);
	}

	public float shininess() {
		return this.getFloat(SHININESS);
	}

	@Override
	public String toString() {
		return "Material" + this.params;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o == null || getClass() != o.getClass()) {
			return false;
		}
		return Objects.equals(this.params, ((Material) o).params);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.params);
	}
}

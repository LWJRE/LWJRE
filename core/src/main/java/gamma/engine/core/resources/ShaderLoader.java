package gamma.engine.core.resources;

import gamma.engine.core.utils.FileUtils;
import gamma.engine.core.utils.YamlParser;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

import java.util.HashMap;

/**
 * Utility class for loading shaders.
 * Shaders are loaded with {@link ShaderLoader#getOrLoad(String)}.
 *
 * @author Nico
 */
public final class ShaderLoader extends AbstractConstruct {

	/** Map of all loaded shader programs */
	private static final HashMap<String, Shader> SHADER_PROGRAMS = new HashMap<>();

	/** Map of all loaded vertex shaders */
	private static final HashMap<String, Integer> VERTEX_SHADERS = new HashMap<>();
	/** Map of all loaded fragment shaders */
	private static final HashMap<String, Integer> FRAGMENT_SHADERS = new HashMap<>();

	/**
	 * Loads a shader program or gets the same instance if it was already loaded.
	 *
	 * @param file Path to the shader program's yaml file
	 * @return The requested shader program
	 */
	public static Shader getOrLoad(String file) {
		if(SHADER_PROGRAMS.containsKey(file)) {
			return SHADER_PROGRAMS.get(file);
		} else {
			Shader shader = load(file);
			SHADER_PROGRAMS.put(file, shader);
			return shader;
		}
	}

	/**
	 * Loads a shader program.
	 *
	 * @param file Path to the shader program's yaml file
	 * @return The requested shader program
	 */
	private static Shader load(String file) {
		return YamlParser.loadAs(file, Shader.class);
	}

	/** Shader type */
	private final int type;

	/**
	 * Constructor used in {@link YamlParser} for loading shaders inside yaml files.
	 *
	 * @param type Shader type
	 */
	public ShaderLoader(int type) {
		this.type = type;
	}

	@Override
	public Object construct(Node node) {
		String value = ((ScalarNode) node).getValue();
		return switch(this.type) {
			case GL20.GL_VERTEX_SHADER -> getOrLoad(value, this.type, VERTEX_SHADERS);
			case GL20.GL_FRAGMENT_SHADER -> getOrLoad(value, this.type, FRAGMENT_SHADERS);
			default -> throw new IllegalStateException("Unexpected value: " + this.type);
		};
	}

	/**
	 * Gets or loads a shader.
	 *
	 * @param file Shader file
	 * @param type Shader type
	 * @param map Map of shaders
	 * @return The requested shader
	 */
	private static int getOrLoad(String file, int type, HashMap<String, Integer> map) {
		if(map.containsKey(file)) {
			return map.get(file);
		} else {
			int shader = load(file, type);
			map.put(file, shader);
			return shader;
		}
	}

	/**
	 * Loads a shader.
	 *
	 * @param file Shader file
	 * @param type Shader type
	 * @return The requested shader
	 */
	private static int load(String file, int type) {
		String shaderCode = FileUtils.readAsString(file);
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, shaderCode);
		GL20.glCompileShader(shader);
		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Could not compile shader");
			System.out.println(GL20.glGetShaderInfoLog(shader));
			GL20.glDeleteShader(shader);
			// TODO: Give better compilation feedback
			throw new RuntimeException("Shader compilation exception");
		}
		return shader;
	}
}

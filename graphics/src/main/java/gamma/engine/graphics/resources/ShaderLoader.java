package gamma.engine.graphics.resources;

import gamma.engine.core.resources.FileUtils;
import gamma.engine.core.resources.ResourceLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of a {@link ResourceLoader} to load shader files.
 *
 * @author Nico
 */
public final class ShaderLoader implements ResourceLoader {

	/** The main fragment shader's code */
	private static final String MAIN_FRAGMENT = FileUtils.readResourceAsString("gamma/engine/shaders/main_fragment.glsl");
	/** The main vertex shader's code */
	private static final String MAIN_VERTEX = FileUtils.readResourceAsString("gamma/engine/shaders/main_vertex.glsl");

	@Override
	public Object load(String path) {
		try {
			String shaderCode = FileUtils.readResourceAsString(path);
			Matcher vertexRegex = Pattern.compile("((#define\\s+VERTEX)(.|\\s)+?(#undef\\s+VERTEX|\\z))").matcher(shaderCode);
			Matcher fragmentRegex = Pattern.compile("((#define\\s+FRAGMENT)(.|\\s)+?(#undef\\s+FRAGMENT|\\z))").matcher(shaderCode);
			if(vertexRegex.find() && fragmentRegex.find()) {
				String vertexCode = MAIN_VERTEX.replace("void vertex_shader();", vertexRegex.group(1));
				String fragmentCode = MAIN_FRAGMENT.replace("vec4 fragment_shader();", fragmentRegex.group(1));
				// TODO: Get version from settings
				vertexCode = vertexCode.replaceAll("#version \\d+", "#version 450");
				fragmentCode = fragmentCode.replaceAll("#version \\d+", "#version 450");
				return new Shader(vertexCode, fragmentCode);
			}
			throw new RuntimeException("Incorrect format in shader " + path);
		} catch (ShaderCompilationException e) {
			throw new RuntimeException("Could not compile shader " + path, e);
		}
	}

	@Override
	public String[] getExtensions() {
		return new String[] {".glsl"};
	}
}

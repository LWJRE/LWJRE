package io.github.lwjre.resources;

import io.github.lwjre.ApplicationProperties;
import io.github.lwjre.utils.FileUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of a {@link ResourceLoader} to load shader files.
 * Can load {@code .glsl}, {@code .vert}, and {@code .frag} files.
 * The file extension is only used to determine the type of shader and can be used in place of {@code #define VERTEX} or {@code #define FRAGMENT}.
 *
 * @author Nico
 */
public final class ShaderLoader implements ResourceLoader {

	/** The main fragment shader's code */
	private static final String MAIN_FRAGMENT = FileUtils.readResourceAsString("io/github/lwjre/shaders/main_fragment.glsl");
	/** The main vertex shader's code */
	private static final String MAIN_VERTEX = FileUtils.readResourceAsString("io/github/lwjre/shaders/main_vertex.glsl");

	@Override
	public Object load(String path) {
		try {
			String shaderCode = FileUtils.readResourceAsString(path);
			String vertexCode = MAIN_VERTEX.replaceAll("#version \\d+", ApplicationProperties.get("rendering.shaders.version", "#version 450"));
			String fragmentCode = MAIN_FRAGMENT.replaceAll("#version \\d+", ApplicationProperties.get("rendering.shaders.version", "#version 450"));
			fragmentCode = fragmentCode.replaceAll("#define MAX_POINT_LIGHTS \\d+", "#define MAX_POINT_LIGHTS " + ApplicationProperties.get("rendering.shaders.lights", "4"));
			if(path.endsWith(".vert")) {
				vertexCode = vertexCode.replace("void vertex_shader();", shaderCode);
				fragmentCode = fragmentCode.replace("vec4 fragment_shader();", "vec4 fragment_shader() {return vec4(0.0, 0.0, 0.0, 0.0);}");
			} else if(path.endsWith(".frag")) {
				vertexCode = vertexCode.replace("void vertex_shader();", "void vertex_shader() {}");
				fragmentCode = fragmentCode.replace("vec4 fragment_shader();", shaderCode);
			} else {
				Matcher vertexRegex = Pattern.compile("((#define\\s+VERTEX)(.|\\s)+?(#undef\\s+VERTEX|\\z))").matcher(shaderCode);
				Matcher fragmentRegex = Pattern.compile("((#define\\s+FRAGMENT)(.|\\s)+?(#undef\\s+FRAGMENT|\\z))").matcher(shaderCode);
				if(vertexRegex.find()) {
					vertexCode = vertexCode.replace("void vertex_shader();", vertexRegex.group(1));
				} else {
					vertexCode = vertexCode.replace("void vertex_shader();", "void vertex_shader() {}");
				}
				if(fragmentRegex.find()) {
					fragmentCode = fragmentCode.replace("vec4 fragment_shader();", fragmentRegex.group(1));
				} else {
					fragmentCode = fragmentCode.replace("vec4 fragment_shader();", "vec4 fragment_shader() {return vec4(0.0, 0.0, 0.0, 0.0);}");
				}
			}
			return new Shader(vertexCode, fragmentCode);
		} catch (ShaderCompilationException e) {
			throw new RuntimeException("Could not compile shader " + path, e);
		}
	}

	@Override
	public String[] getExtensions() {
		return new String[] {".glsl", ".frag", ".vert"};
	}
}

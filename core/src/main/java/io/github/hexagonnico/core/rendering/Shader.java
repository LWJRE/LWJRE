package io.github.hexagonnico.core.rendering;

import io.github.scalamath.colorlib.Color;
import io.github.scalamath.vecmatlib.*;

import java.io.*;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * A Shader used for rendering.
 */
public final class Shader {

    /**
     * Map containing the code for built-in shaders.
     */
    private static final HashMap<String, String> BUILTIN_SHADER_CODE = new HashMap<>();
    /**
     * Map containing instances of built-in shaders.
     */
    private static final HashMap<String, Shader> BUILTIN_SHADERS = new HashMap<>();

    /**
     * Returns the code for a built-in shader of the given type.
     * The given string must end with {@code .vert} or {@code .frag}.
     *
     * @param shader The shader type.
     * @return The code for the requested built-in shader.
     */
    public static String getBuiltinShaderCode(String shader) {
        // TODO: Create a FileUtils class
        return BUILTIN_SHADER_CODE.computeIfAbsent(shader, shaderType -> {
            try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("io/github/hexagonnico/core/shaders/" + shaderType)) {
                if(inputStream == null) {
                    throw new FileNotFoundException();
                }
                return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            } catch(IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        // TODO: Builtin shaders may not exist for types different from vertex and fragment
    }

    public static Shader getBuiltinShader(String type) {
        return BUILTIN_SHADERS.computeIfAbsent(type, shaderType -> {
            var shader = new Shader();
            shader.shaderData.compile(getBuiltinShaderCode(shaderType + ".vert"), getBuiltinShaderCode(shaderType + ".frag"));
            return shader;
        });
    }

    /**
     * Shader data used internally to abstract the representation of a shaders across different rendering APIs.
     */
    private final ShaderData shaderData = RenderingServer.createShader(this);

    public void compile(String code) {
        var processor = new ShaderProcessor(code);
        this.shaderData.compile(processor.getVertexCode(), processor.getFragmentCode());
    }

    public void set(String variable, float value) {
        this.shaderData.set(variable, value);
    }

    public void set(String variable, float x, float y) {
        this.shaderData.set(variable, x, y);
    }

    public void set(String variable, Vec2f vector) {
        this.set(variable, vector.x(), vector.y());
    }

    public void set(String variable, float x, float y, float z) {
        this.shaderData.set(variable, x, y, z);
    }

    public void set(String variable, Vec3f vector) {
        this.set(variable, vector.x(), vector.y(), vector.z());
    }

    public void set(String variable, float x, float y, float z, float w) {
        this.shaderData.set(variable, x, y, z, w);
    }

    public void set(String variable, Vec4f vector) {
        this.set(variable, vector.x(), vector.y(), vector.z(), vector.w());
    }

    public void set(String variable, int value) {
        this.shaderData.set(variable, value);
    }

    public void set(String variable, int x, int y) {
        this.shaderData.set(variable, x, y);
    }

    public void set(String variable, Vec2i vector) {
        this.set(variable, vector.x(), vector.y());
    }

    public void set(String variable, int x, int y, int z) {
        this.shaderData.set(variable, x, y, z);
    }

    public void set(String variable, Vec3i vector) {
        this.set(variable, vector.x(), vector.y(), vector.z());
    }

    public void set(String variable, int x, int y, int z, int w) {
        this.shaderData.set(variable, x, y, z, w);
    }

    public void set(String variable, Vec4i vector) {
        this.set(variable, vector.x(), vector.y(), vector.z(), vector.w());
    }

    public void set(String variable, Mat2f matrix) {
        this.shaderData.set(variable, matrix);
    }

    public void set(String variable, Mat2x3f matrix) {
        this.shaderData.set(variable, matrix);
    }

    public void set(String variable, Mat3f matrix) {
        this.shaderData.set(variable, matrix);
    }

    public void set(String variable, Mat3x4f matrix) {
        this.shaderData.set(variable, matrix);
    }

    public void set(String variable, Mat4f matrix) {
        this.shaderData.set(variable, matrix);
    }

    public void set(String variable, Texture texture) {
        if(texture != null) {
            texture.updateTexture();
        }
        this.shaderData.set(variable, texture);
    }

    // TODO: Differentiate between Col4f and Col3f
    public void set(String variable, Color color) {
        this.shaderData.set(variable, color.r(), color.g(), color.b(), color.a());
    }
}

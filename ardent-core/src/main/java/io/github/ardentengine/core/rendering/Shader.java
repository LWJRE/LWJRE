package io.github.ardentengine.core.rendering;

import java.util.Objects;

public final class Shader {

    private String vertexCode = "";
    private String fragmentCode = "";

    // TODO: Shaders should be immutable because the shader code is processed at build time and it's pasted into the builtin shader when the shader is loaded

    public String vertexCode() {
        return this.vertexCode;
    }

    public void setVertexCode(String vertexCode) {
        this.vertexCode = Objects.requireNonNullElse(vertexCode, "");
    }

    public String fragmentCode() {
        return this.fragmentCode;
    }

    public void setFragmentCode(String fragmentCode) {
        this.fragmentCode = Objects.requireNonNullElse(fragmentCode, "");
    }
}
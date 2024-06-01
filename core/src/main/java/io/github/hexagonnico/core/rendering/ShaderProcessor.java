package io.github.hexagonnico.core.rendering;

import java.util.regex.Pattern;

public class ShaderProcessor {

    private final String code;
    private String type;

    private String vertexCode;
    private String fragmentCode;

    public ShaderProcessor(String code) {
        this.code = code;
    }

    public String getShaderType() {
        if(this.type == null || this.type.isEmpty()) {
            var regex = Pattern.compile("#define\\s+SHADER_TYPE\\s+(\\w+)").matcher(this.code);
            if(regex.find()) {
                this.type = regex.group(1);
            } else {
                // TODO: Log an error here
                this.type = "";
            }
        }
        return this.type;
    }

    // TODO: Better regex that does not require #define and #undef
    // Match the vertex_shader function ^void\s*vertex_shader\(\s*\)\s*{[\s\S]*?}\s*$

    public String getVertexCode() {
        if(this.vertexCode == null || this.vertexCode.isEmpty()) {
            this.vertexCode = Shader.getBuiltinShaderCode(this.getShaderType() + ".vert");
            var regex = Pattern.compile("(#define\\s+VERTEX_SHADER(.|\\n)+?)(#undef\\s+VERTEX_SHADER|\\z)").matcher(this.code);
            if(regex.find()) {
                this.vertexCode = this.vertexCode.replace("void main()", regex.group(1) + "\nvoid main()");
            }
        }
        return this.vertexCode;
    }

    public String getFragmentCode() {
        if(this.fragmentCode == null || this.fragmentCode.isEmpty()) {
            this.fragmentCode = Shader.getBuiltinShaderCode(this.getShaderType() + ".frag");
            var regex = Pattern.compile("(#define\\s+FRAGMENT_SHADER(.|\\n)+?)(#undef\\s+FRAGMENT_SHADER|\\z)").matcher(this.code);
            if(regex.find()) {
                this.fragmentCode = this.fragmentCode.replace("void main()", regex.group(1) + "\nvoid main()");
            }
        }
        return this.fragmentCode;
    }
}

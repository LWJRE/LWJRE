#define SHADER_TYPE default_shader_3d

void fragment_shader() {
    frag_color = vec4(vertex + 0.5, 1.0);
}
#define SHADER_TYPE mesh_shader

void fragment_shader() {
    frag_color = vec4(vertex + 0.5, 1.0);
}
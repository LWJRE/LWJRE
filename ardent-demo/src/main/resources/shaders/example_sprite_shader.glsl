#define SHADER_TYPE sprite_shader

// This is an example shader

void vertex_shader() {
    uv = uv.yx;
}

void fragment_shader() {
    frag_color = vec4(1.0 - frag_color.rgb, 1.0);
}
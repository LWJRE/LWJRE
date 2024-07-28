#define SHADER_TYPE sprite_2d

// This is an example shader

void vertex_shader() {
    float v = uv.x;
    uv.x = uv.y;
    uv.y = v;
}

void fragment_shader() {
    frag_color = vec4(1.0 - frag_color.rgb, 1.0);
}
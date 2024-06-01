#define SHADER_TYPE sprite_shader

#define VERTEX_SHADER

void vertex_shader() {
    vertex += vec2(0.2, 0.1);
    uv.x = 1.0 - uv.x;
    uv.y = 1.0 - uv.y;
}

#undef VERTEX_SHADER
#define FRAGMENT_SHADER

void fragment_shader() {
    frag_color = vec4(0.0, uv, 1.0);
}

#undef FRAGMENT_SHADER
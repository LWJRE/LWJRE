#define SHADER_TYPE sprite_shader

void vertex_shader() {
    float v = uv.x;
    uv.x = uv.y;
    uv.y = v;
}

void fragment_shader() {
    frag_color = vec4(vec3(1.0) - texture(sprite_texture, uv).rgb, 1.0);
}

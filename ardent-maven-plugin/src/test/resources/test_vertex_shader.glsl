#define SHADER_TYPE sprite_2d

void vertex_shader() {
    float v = uv.x;
    uv.x = uv.y;
    uv.y = v;
}

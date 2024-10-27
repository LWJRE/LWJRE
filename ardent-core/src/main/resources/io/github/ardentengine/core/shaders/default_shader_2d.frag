#version 450

in vec2 vertex;
in vec2 uv;

out vec4 frag_color;

uniform ivec2 texture_size;
// TODO: Implement modulate color
uniform sampler2D color_texture;
// TODO: Implement normal map

void main() {
    frag_color = texture(color_texture, uv);
#ifdef SHADER_TYPE
    fragment_shader();
#endif
}
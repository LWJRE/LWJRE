#version 450

in vec2 vertex;
in vec2 uv;

out vec4 frag_color;

// TODO: Implement modulate color
uniform sampler2D sprite_texture;

void main() {
    frag_color = texture(sprite_texture, uv);
#ifdef SHADER_TYPE
    fragment_shader();
#endif
}
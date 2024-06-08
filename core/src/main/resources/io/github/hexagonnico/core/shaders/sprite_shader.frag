#version 450

in vec2 vertex;
in vec2 uv;

out vec4 frag_color;

uniform sampler2D sprite_texture;
uniform vec4 modulate;

void main() {
#ifdef FRAGMENT_SHADER
    fragment_shader();
#else
    frag_color = modulate * texture(sprite_texture, uv);
#endif
}
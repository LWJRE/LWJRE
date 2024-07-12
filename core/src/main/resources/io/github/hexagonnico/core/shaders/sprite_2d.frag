#version 450

in vec2 vertex;
in vec2 uv;

out vec4 frag_color;

uniform sampler2D sprite_texture;
uniform vec4 modulate = vec4(1.0);

void fragment_shader() {
    frag_color = modulate * texture(sprite_texture, uv);
}

void main() {
    fragment_shader();
}
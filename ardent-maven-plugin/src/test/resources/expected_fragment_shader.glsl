#version 450

in vec2 vertex;
in vec2 uv;

out vec4 frag_color;

uniform sampler2D sprite_texture;
uniform vec4 modulate = vec4(1.0);

#define SHADER_TYPE sprite_2d

void fragment_shader() {
    frag_color = vec4(vec3(1.0) - texture(sprite_texture, uv).rgb, 1.0);
}

void main() {
    fragment_shader();
}
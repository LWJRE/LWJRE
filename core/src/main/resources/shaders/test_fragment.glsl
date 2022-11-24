#version 450
#define FRAGMENT

in vec2 texture_coords;

out vec4 out_color;

uniform sampler2D texture_sampler;

void main() {
    out_color = texture(texture_sampler, texture_coords);
}
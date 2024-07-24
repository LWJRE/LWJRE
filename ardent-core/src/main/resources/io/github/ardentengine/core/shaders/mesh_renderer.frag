#version 450

in vec3 vertex;
in vec2 uv;
in vec3 normal;

out vec4 frag_color;

uniform vec4 modulate = vec4(1.0);

void fragment_shader() {
    frag_color = modulate;
}

void main() {
    fragment_shader();
}
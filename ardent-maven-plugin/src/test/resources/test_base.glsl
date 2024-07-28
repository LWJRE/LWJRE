#version 450

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec3 normal;

out vec4 frag_color;

uniform mat4x3 transformation_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

void vertex_shader() {
    vec3 world_position = transformation_matrix * vec4(vertex, 1.0);
    gl_Position = projection_matrix * view_matrix * vec4(world_position, 1.0);
}

void fragment_shader() {
    frag_color = vec4(1.0);
}
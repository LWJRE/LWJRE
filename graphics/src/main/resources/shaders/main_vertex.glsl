#version 450

in layout(location = 0) vec3 vertex;
in layout(location = 1) vec2 texture_coords;
in layout(location = 2) vec3 normal;

out vec3 world_position;
out vec3 surface_normal;

uniform mat4 transformation_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

void vertex_shader();

void main() {
    gl_Position = projection_matrix * view_matrix * transformation_matrix * vec4(vertex, 1.0);
    world_position = vec3(transformation_matrix * vec4(vertex, 1.0));
    surface_normal = normalize(vec3(transformation_matrix * vec4(normal, 1.0)));
    vertex_shader();
}
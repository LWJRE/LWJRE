#version 450

// TODO: Bind attributes instead of using location
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
    world_position = (transformation_matrix * vec4(vertex, 1.0)).xyz;
    surface_normal = normalize(transformation_matrix * vec4(normal, 0.0)).xyz;
    vertex_shader();
}
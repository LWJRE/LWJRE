#version 450

in layout(location = 0) vec3 vertex;
in layout(location = 1) vec2 texture_coords;
in layout(location = 2) vec3 normal;

out vec2 uv;
out vec3 surface_normal;
out vec4 world_position;
out vec3 view_direction;

uniform mat4 transformation_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

#ifdef VERTEX
void vertex_shader();
#endif

void main() {
    world_position = transformation_matrix * vec4(vertex, 1.0);
    surface_normal = (transformation_matrix * vec4(normal, 1.0)).xyz;
    gl_Position = projection_matrix * view_matrix * world_position;
    // TODO: Find a more efficient way of computing the vector to the camera
    view_direction = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - world_position.xyz;
    uv = texture_coords;
#ifdef VERTEX
    vertex_shader();
#endif
}

#version 450

in layout(location = 0) vec3 vertex;
in layout(location = 1) vec2 texture_coords;
in layout(location = 2) vec3 normal;

out vec2 uv;

uniform mat4 transformation_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

#ifdef VERTEX
void vertex_shader();
#endif

void main() {
    gl_Position = projection_matrix * view_matrix * transformation_matrix * vec4(vertex, 1.0);
    uv = texture_coords;
#ifdef VERTEX
    vertex_shader();
#endif
}

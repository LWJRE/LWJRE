#version 450

layout(location = 0) in vec2 in_vertex;
layout(location = 1) in vec2 in_uv;

out vec2 vertex;
out vec2 uv;

uniform mat3 transformation_matrix;
uniform mat3 projection_matrix;
uniform mat3 view_matrix;

void main() {
    vertex = in_vertex;
    uv = in_uv;
#ifdef VERTEX_SHADER
    vertex_shader();
#endif
//    vec3 position = projection_matrix * view_matrix * transformation_matrix * vec3(vertex, 0.0);
//    gl_Position = vec4(position, 1.0);
    gl_Position = vec4(vertex, 0.0, 1.0);
}
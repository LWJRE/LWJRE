#version 450

layout(location = 0) in vec2 in_vertex;
layout(location = 1) in vec2 in_uv;

out vec2 vertex;
out vec2 uv;

uniform mat3 transformation_matrix;
uniform mat3 view_matrix;
uniform mat4 projection_matrix;

uniform ivec2 texture_size;

void main() {
    vertex = in_vertex * texture_size;
    uv = in_uv;
#ifdef VERTEX_SHADER
    vertex_shader();
#endif
    vec3 world_position = transformation_matrix * vec3(vertex, 1.0);
    gl_Position = projection_matrix * vec4(view_matrix * world_position, 1.0);
}
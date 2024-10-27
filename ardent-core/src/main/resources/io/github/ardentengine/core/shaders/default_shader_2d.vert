#version 450

layout(location = 0) in vec2 in_vertex;
layout(location = 1) in vec2 in_uv;

out vec2 vertex;
out vec2 uv;

uniform mat3x2 transformation_matrix;

layout(std140) uniform Camera2D {
    mat3 view_matrix;
    mat4 projection_matrix;
};

uniform ivec2 texture_size;

void compute_position();

void main() {
    vertex = in_vertex;
    uv = in_uv;
#ifdef SHADER_TYPE
    vertex_shader();
#endif
    compute_position();
}

uniform vec2 vertex_scale;
uniform vec2 vertex_offset;
uniform vec2 uv_scale;
uniform vec2 uv_offset;

// FIXME: The way z index is implemented is weird
uniform int z_index;

void compute_position() {
    vertex = vertex * texture_size * vertex_scale + vertex_offset;
    uv = uv * uv_scale + uv_offset;
    vec2 world_position = transformation_matrix * vec3(vertex, 1.0);
    gl_Position = projection_matrix * vec4(view_matrix * vec3(world_position, 1.0), 1.0);
    gl_Position.z = -z_index / 4096.0; // TODO: Hardcoded number
}
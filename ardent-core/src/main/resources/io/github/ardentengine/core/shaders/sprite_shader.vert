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

uniform int z_index;

uniform ivec2 texture_size;
uniform vec2 offset;
uniform ivec2 flip;
uniform ivec2 frames;
uniform int frame;

void main() {
    vertex = in_vertex;
    uv = in_uv;
#ifdef SHADER_TYPE
    vertex_shader();
#endif
    vec2 uv_scale = flip / frames;
    vec2 uv_offset = vec2(frame % frames.x, frame / frames.x) / frames;
    vertex = vertex * texture_size / frames + offset;
    uv = uv * uv_scale + uv_offset;
    vec2 world_position = transformation_matrix * vec3(vertex, 1.0);
    gl_Position = projection_matrix * vec4(view_matrix * vec3(world_position, 1.0), 1.0);
    gl_Position.z = -z_index / 4096.0; // TODO: Hardcoded number
}
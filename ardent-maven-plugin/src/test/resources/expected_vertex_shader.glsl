#version 450

layout(location = 0) in vec2 in_vertex;
layout(location = 1) in vec2 in_uv;

out vec2 vertex;
out vec2 uv;

uniform mat3x2 transformation_matrix;
uniform mat3 view_matrix;
uniform mat4 projection_matrix;

uniform int z_index;
uniform ivec2 texture_size;
uniform vec2 offset;
uniform int flip_h;
uniform int flip_v;
uniform int h_frames;
uniform int v_frames;
uniform int frame;

#define SHADER_TYPE sprite_2d

void vertex_shader() {
    float v = uv.x;
    uv.x = uv.y;
    uv.y = v;
}

void main() {
    vertex = in_vertex;
    uv = in_uv;
    vertex_shader();
    vec2 frames = vec2(h_frames, v_frames);
    vec2 uv_scale = vec2(flip_h, flip_v) / frames;
    vec2 uv_offset = vec2(frame % h_frames, frame / h_frames) / frames;
    vertex = vertex * texture_size / frames + offset;
    uv = uv * uv_scale + uv_offset;
    vec2 world_position = transformation_matrix * vec3(vertex, 1.0);
    gl_Position = projection_matrix * vec4(view_matrix * vec3(world_position, 1.0 + z_index), 1.0);
}
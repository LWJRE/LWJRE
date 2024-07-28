#version 450
layout(location = 0) in vec3 in_vertex;
out vec3 vertex;
layout(location = 1) in vec2 in_uv;
out vec2 uv;
layout(location = 2) in vec3 in_normal;
out vec3 normal;
uniform mat4x3 transformation_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;
void main(){
vertex=in_vertex;
uv=in_uv;
normal=in_normal;
vec3 world_position = transformation_matrix * vec4(vertex, 1.0);
gl_Position = projection_matrix * view_matrix * vec4(world_position, 1.0);
}
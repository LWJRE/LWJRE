#version 450
in vec3 vertex;
in vec2 uv;
in vec3 normal;
out vec4 frag_color;
uniform mat4x3 transformation_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;
void main(){
frag_color = vec4(1.0);
}
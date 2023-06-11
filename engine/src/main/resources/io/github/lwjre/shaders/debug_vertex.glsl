#version 450

// TODO: Bind attributes instead of using location
in layout(location = 0) vec3 vertex;

uniform mat4 transformation_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

void main() {
    gl_Position = projection_matrix * view_matrix * transformation_matrix * vec4(vertex, 1.0);
}
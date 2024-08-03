#version 450

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec3 normal;

out vec4 frag_color;

uniform mat4x3 transformation_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec4 modulate = vec4(1.0);

varying vec3 world_position;
varying vec3 surface_normal;

struct Light {
    vec3 position;
    vec3 color;
};

layout (std140) uniform Lights {
    Light lights[128];
    int lights_count;
};

void vertex_shader() {
    world_position = transformation_matrix * vec4(vertex, 1.0);
    surface_normal = normalize(transformation_matrix * vec4(normal, 1.0)).xyz;
    gl_Position = projection_matrix * view_matrix * vec4(world_position, 1.0);
}

// TODO: #if RENDER_MODE == unshaded
void fragment_shader() {
    // TODO: Directional light
    vec3 light_result = vec3(0.0);
    for(int i = 0; i < lights_count; i++) {
        vec3 light_direction = normalize(lights[i].position - world_position);
        float diffuse = max(dot(surface_normal, light_direction), 0.0);
        // TODO: Specular and attenuation
        light_result += lights[i].color * diffuse;
    }
    frag_color = modulate * vec4(light_result, 1.0);
}
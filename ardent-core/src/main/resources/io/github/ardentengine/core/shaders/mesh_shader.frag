#version 450

in vec3 vertex;
in vec2 uv;
in vec3 normal;

in vec3 world_position;
in vec3 surface_normal;

out vec4 frag_color;

// TODO: Implement texture, modulate color, and vertex color

void compute_lighting();

void main() {
    frag_color = vec4(1.0, 1.0, 1.0, 1.0);
#ifdef SHADER_TYPE
    fragment_shader();
#endif
    compute_lighting();
}

// TODO: This struct can be renamed by the shader processor if the user defines a struct with the same name (the uniform block can't though...)
struct Light {
    vec3 position;
    vec3 color;
};

layout(std140) uniform LightData {
    Light lights[128];
    int lights_count;
};

// TODO: This function must be renamed by the shader processor if the user defines a function with the same name
void compute_lighting() {
    // TODO: Ambient light
    vec3 light_result = vec3(0.0);
    // TODO: Directional light
    for(int i = 0; i < lights_count; i++) {
        vec3 light_direction = normalize(lights[i].position - world_position);
        float diffuse = max(dot(surface_normal, light_direction), 0.0);
        // TODO: Specular and attenuation
        light_result += lights[i].color * diffuse;
    }
    frag_color *= vec4(light_result, 1.0);
    // TODO: Add different light modes (phong, cell shaded, unshaded)
}
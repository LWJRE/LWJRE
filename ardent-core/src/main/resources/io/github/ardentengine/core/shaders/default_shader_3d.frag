#version 450

in vec3 vertex;
in vec2 uv;
in vec3 normal;

in vec3 world_position;
in vec3 surface_normal;

out vec4 frag_color;

// TODO: Implement vertex color

#ifdef SHADER_TYPE
vec3 ambient = vec3(0.2);
vec3 diffuse = vec3(1.0);
vec3 specular = vec3(1.0);
float shininess = 32.0;
#else
// TODO: Implement textures here
uniform vec3 ambient;
uniform vec3 diffuse;
uniform vec3 specular;
uniform float shininess;
#endif

void compute_lighting();

void main() {
    frag_color = vec4(1.0);
#ifdef SHADER_TYPE
    fragment_shader();
#endif
    // TODO: Allow for "unshaded" mode
    compute_lighting();
}

struct Light {
    vec3 position;
    vec3 color;
};

layout(std140) uniform LightData {
    Light lights[128];
    int lights_count;
};

uniform vec3 camera_position;

// TODO: Add cell shading option

void compute_lighting() {
    // TODO: Ambient light
    vec3 light_result = vec3(0.0);
    // TODO: Directional light
    vec3 view_direction = normalize(camera_position - world_position);
    for(int i = 0; i < lights_count; i++) {
        // Ambient shading
        vec3 ambient = lights[i].color * ambient;
        // Diffuse shading
        vec3 light_direction = normalize(lights[i].position - world_position);
        vec3 diffuse = lights[i].color * diffuse * max(dot(surface_normal, light_direction), 0.0);
        // Specular shading
        vec3 reflection = reflect(-light_direction, normal);
        vec3 specular = lights[i].color * specular * pow(max(dot(view_direction, reflection), 0.0), shininess);
        // TODO: Attenuation
        light_result += ambient + diffuse + specular;
    }
    frag_color *= vec4(light_result, 1.0);
}
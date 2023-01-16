#version 450

in vec3 world_position;
in vec3 surface_normal;

out vec4 frag_color;

uniform vec3 camera_position;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct PointLight {
    vec3 position;
    vec3 attenuation;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

#define MAX_POINT_LIGHTS 4

uniform Material material;

uniform int lights_count;
uniform PointLight point_lights[MAX_POINT_LIGHTS];

vec4 fragment_shader(vec4 frag_color);

vec3 point_light(PointLight light, vec3 view_direction) {
    vec3 light_direction = normalize(light.position - world_position);
    // diffuse shading
    float diffuse_value = max(dot(surface_normal, light_direction), 0.0); // TODO: Give a min higher than 0.0
    // specular shading
    vec3 reflection = reflect(-light_direction, surface_normal);
    float specular_value = pow(max(dot(view_direction, reflection), 0.0), material.shininess);
    // attenuation
    float distance = length(light.position - world_position);
    float attenuation = 1.0 / (light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * (distance * distance));
    // result
    vec3 ambient = light.ambient * material.ambient;
    vec3 diffuse = light.diffuse * diffuse_value * material.diffuse;
    vec3 specular = light.specular * specular_value * material.specular;
    return (ambient + diffuse + specular) * attenuation;
}

void main() {
    vec3 view_direction = normalize(camera_position - world_position);
    // point light
    vec3 color = vec3(0.0, 0.0, 0.0);
    for(int i = 0; i < lights_count; i++) {
        color += point_light(point_lights[i], view_direction);
    }

    frag_color = vec4(color, 1.0);
    frag_color = fragment_shader(frag_color);
}

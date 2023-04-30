#version 450

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
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

in vec3 world_position;
in vec3 surface_normal;

out vec4 frag_color;

uniform vec3 camera_position;

uniform Material material;

uniform int lights_count;
uniform PointLight point_lights[MAX_POINT_LIGHTS];

vec3 point_light(PointLight light, vec3 view_direction) {
    // ambient lighting
    vec3 ambient = light.ambient * material.ambient.rgb;
    // diffuse lighting
    vec3 light_direction = normalize(light.position - world_position);
    float diffuse_value = max(dot(surface_normal, light_direction), 0.0);
    vec3 diffuse = light.diffuse * diffuse_value * material.diffuse.rgb;
    // specular lighting
    vec3 reflection = reflect(-light_direction, surface_normal);
    float specular_value = 0.0;
    if(material.shininess > 0.0) {
        specular_value = pow(max(dot(view_direction, reflection), 0.0), material.shininess);
    }
    vec3 specular = light.specular * specular_value * material.specular.rgb;
    // attenuation
    float distance = length(light.position - world_position);
    float attenuation = 1.0 / (light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * (distance * distance));
    return (ambient + diffuse + specular) /* attenuation*/;
}

vec4 fragment_shader();

void main() {
    vec3 view_direction = normalize(camera_position - world_position);
    frag_color = fragment_shader();
    // lighting calculation
    for(int i = 0; i < lights_count; i++) {
        frag_color += vec4(point_light(point_lights[i], view_direction), 0.0);
    }
}

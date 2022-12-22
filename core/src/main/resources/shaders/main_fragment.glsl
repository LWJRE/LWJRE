#version 450

in vec3 world_position;
in vec3 surface_normal;

out vec4 frag_color;

uniform vec3 light_position;
uniform vec3 light_color;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

uniform Material material;

#ifdef FRAGMENT
vec4 fragment_shader(vec4 frag_color);
#endif

void main() {
    // ambient
    vec3 ambient = light_color * material.ambient;

    // diffuse
    vec3 light_direction = normalize(light_position - world_position);
    float brightness = max(dot(surface_normal, light_direction), 0.0);
    vec3 diffuse = light_color * (brightness * material.diffuse);

    // TODO: specular
    vec3 specular = vec3(0.0, 0.0, 0.0);

    frag_color = vec4(ambient + diffuse + specular, 1.0);
#ifdef FRAGMENT
    frag_color = fragment_shader(frag_color);
#endif
}

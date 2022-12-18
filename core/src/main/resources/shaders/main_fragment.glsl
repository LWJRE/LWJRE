#version 450

in vec2 uv;
in vec3 surface_normal;
in vec4 world_position;
in vec3 view_direction;

out vec4 frag_color;

struct PointLight {
    vec3 position;
    vec3 color;
    // TODO: Specular light
};

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

// TODO: Material texture
//uniform sampler2D texture_sampler;

// TODO: Multiple lights
uniform PointLight light;

#ifdef FRAGMENT
vec4 fragment_shader(vec4 frag_color);
#endif

vec3 point_light(PointLight light) {
    float diffuse_light = 0.2;
    vec3 to_light_vector = normalize(light.position - world_position.xyz);
    float brightness = max(dot(surface_normal, to_light_vector), diffuse_light);
    return brightness * light.color;
}

void main() {
    //frag_color = texture(texture_sampler, uv);
    vec3 light_color = point_light(light);
    frag_color = vec4(light_color, 1.0);
#ifdef FRAGMENT
    frag_color = fragment_shader(frag_color);
#endif
}

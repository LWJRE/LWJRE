#version 450

in vec2 uv;

out vec4 frag_color;

uniform sampler2D texture_sampler;

#ifdef FRAGMENT
vec4 fragment_shader(vec4 frag_color);
#endif

void main() {
    frag_color = texture(texture_sampler, uv);
#ifdef FRAGMENT
    frag_color = fragment_shader(frag_color);
#endif
}

#define SHADER_TYPE sprite_2d

// Fragment shader function
void fragment_shader() {
    frag_color = vec4(vec3(1.0) - texture(sprite_texture, uv).rgb, 1.0); // Comment
    /*
     * Multiline
     * comment
     */
}

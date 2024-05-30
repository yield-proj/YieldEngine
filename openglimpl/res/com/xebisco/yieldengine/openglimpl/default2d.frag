#version 330

in vec2 outTextCoord;

out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 color;
uniform int ignoreTexture;

void main() {
    if(ignoreTexture == 0) {
        vec4 textureColor = texture(texture_sampler, outTextCoord);
        fragColor = color * textureColor;
    } else {
        fragColor = color;
    }
}
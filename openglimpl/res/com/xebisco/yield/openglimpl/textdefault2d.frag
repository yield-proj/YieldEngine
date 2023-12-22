#version 330

in vec2 outTextCoord;

out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 color;

void main() {
    vec4 textureColor = texture(texture_sampler, outTextCoord);
    fragColor = color * vec4(textureColor.r, textureColor.r, textureColor.r, textureColor.r);
}
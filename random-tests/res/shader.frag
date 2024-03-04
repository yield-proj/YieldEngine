#version 330

out vec4 fragColor;

in vec2 fTexCoord;

uniform sampler2D texture_sampler;
uniform float a;

void main() {
    fragColor = texture(texture_sampler, vec2(fTexCoord.x - a, fTexCoord.y));
    if(fragColor.a < 1) {
        fragColor.r = 1;
        fragColor.b = 1;
        fragColor.a = 1;
        fragColor.g = abs(sin(a));
    }
}
#version 330

in vec2 position;
in vec2 texCoord;

out vec2 fTexCoord;

void main() {
    gl_Position = vec4(position, 0, 1);
    fTexCoord = texCoord;
}
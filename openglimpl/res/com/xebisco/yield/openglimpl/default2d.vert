#version 330

in vec3 position;
in vec2 texCoord;
out vec2 outTextCoord;

uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
    mat4 modelViewMatrix = viewMatrix * transformationMatrix;
    vec4 mvPosition =  modelViewMatrix * vec4(position, 1.0);
    gl_Position   = mvPosition;
    outTextCoord  = texCoord;
}
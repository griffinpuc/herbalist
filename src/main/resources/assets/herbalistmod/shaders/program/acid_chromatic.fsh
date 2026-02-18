#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform float Time;
uniform float Intensity;

uniform float Aberration;
uniform float AberrationSpeed;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    float t = Time * AberrationSpeed;

    vec2 dir = normalize(vec2(sin(t), cos(t)));
    vec2 px = 1.0 / InSize;

    float a = Aberration * Intensity;
    vec2 off = dir * a;

    vec4 cR = texture(DiffuseSampler, texCoord + off + px * 0.5 * sin(t * 0.7));
    vec4 cG = texture(DiffuseSampler, texCoord);
    vec4 cB = texture(DiffuseSampler, texCoord - off + px * 0.5 * cos(t * 0.6));

    fragColor = vec4(cR.r, cG.g, cB.b, 1.0);
}

#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PrevSampler;
uniform float TrailStrength;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 cur = texture(DiffuseSampler, texCoord).rgb;
    vec3 prev = texture(PrevSampler, texCoord).rgb;

    // feedback blend: higher TrailStrength = more persistence
    vec3 outc = mix(cur, prev, clamp(TrailStrength, 0.0, 0.98));
    fragColor = vec4(outc, 1.0);
}

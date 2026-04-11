package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.client.trip.primitive.Tags;

import java.util.List;

public final class PrimitiveDef {
    public String id;
    public String type;          // "fog" | "fov" | "particles" | "entity_particles" | "audio"
    public Tags tag;
    public boolean major = false;
    public float weight = 1.0f;

    // optional conflicts by tag name, e.g. ["POSTFX"]
    public List<Tags> conflicts;

    // shared params
    public float minStrength = 0.25f;
    public float maxStrength = 1.0f;
    public float minSpeed = 0.2f;
    public float maxSpeed = 1.2f;

    // ---- fog params ----
    public String fogMode;       // "tint" | "pulse" | "crush" | "grayscale"
    public float[] colorA;       // [r,g,b]
    public float[] colorB;       // [r,g,b]
    public float grayMix = 0.0f; // 0..1 extra gray pull

    // ---- fov params ----
    public String fovMode;       // "sine" | "dual" | "jitter" | "snap"
    public float fovAmp = 3.5f;
    public float fovAmp2 = 1.5f;
    public int snapEveryTicks = 80;

    // ---- particles params ----
    public String particle;      // "minecraft:ash" etc (subset supported in mapper)
    public String pattern;       // "cloud" | "ring" | "horizon_blob" | "upward_snow"
    public int minCount = 2;
    public int maxCount = 25;
    public float radius = 10f;
    public float yMin = 0.0f;
    public float yMax = 6.0f;

    // ---- entity particle params ----
    public String entityPattern; // "ring" | "trail"
    public int entityMax = 12;
    public float entityRadius = 1.2f;

    // ---- audio params ----
    public String sound;         // "minecraft:entity.enderman.ambient" etc
    public String audioMode;     // "scatter"
    public int minDelayTicks = 140;
    public int maxDelayTicks = 420;
    public float volMin = 0.4f;
    public float volMax = 1.0f;
    public float pitchMin = 0.8f;
    public float pitchMax = 1.2f;
    public float soundRadius = 90f;


    // ---- postfx params ----
    // shader post chain json, e.g. "herbalistmod:shaders/post/mirage.json"
    public String shader;
}

package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.client.trip.primitive.Tags;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class TripProfile {

    public final String id;

    // Multiplier applied to all primitives of a given tag
    public final EnumMap<Tags, Float> tagWeightMult = new EnumMap<>(Tags.class);

    // Multiplier applied to specific primitive ids
    public final Map<String, Float> primitiveWeightMult = new HashMap<>();

    // Global tuning knobs
    public float strengthMult = 1.0f;
    public float speedMult = 1.0f;

    // Phase bias multipliers (early -> peak -> late)
    public float earlyBias = 1.0f;
    public float peakBias = 1.0f;
    public float lateBias = 1.0f;

    // 0..1 random jitter applied to weights (helps avoid repetition)
    public float chaos = 0.15f;

    public TripProfile(String id) {
        this.id = id;

        // Default all tags to 1.0
        for (Tags t : Tags.values()) {
            tagWeightMult.put(t, 1.0f);
        }
    }

    public float multFor(Tags tag) {
        return tagWeightMult.getOrDefault(tag, 1.0f);
    }

    public float multFor(String primitiveId) {
        return primitiveWeightMult.getOrDefault(primitiveId, 1.0f);
    }
}

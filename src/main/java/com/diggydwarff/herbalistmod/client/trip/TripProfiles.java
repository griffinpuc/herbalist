package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.client.trip.primitive.Tags;

public final class TripProfiles {

    private TripProfiles() {}

    public static TripProfile introspectionI() {
        TripProfile p = new TripProfile("introspection_i");

        p.tagWeightMult.put(Tags.WORLD, 1.25f);
        p.tagWeightMult.put(Tags.POSTFX, 1.35f);
        p.tagWeightMult.put(Tags.ATMOSPHERE, 1.15f);
        p.tagWeightMult.put(Tags.AUDIO, 1.10f);

        p.primitiveWeightMult.put("postfx.mirage", 1.35f);
        p.primitiveWeightMult.put("atmo.context_tint", 1.25f);

        p.strengthMult = 0.50f;
        p.speedMult = 0.95f;

        p.earlyBias = 0.25f;
        p.peakBias = 1.25f;
        p.lateBias = 0.95f;
        p.chaos = 0.22f;

        return p;
    }

    public static TripProfile introspectionII() {
        TripProfile p = new TripProfile("introspection_ii");

        p.tagWeightMult.put(Tags.WORLD, 1.45f);
        p.tagWeightMult.put(Tags.POSTFX, 1.60f);
        p.tagWeightMult.put(Tags.CAMERA, 1.25f);
        p.tagWeightMult.put(Tags.ATMOSPHERE, 1.20f);

        p.primitiveWeightMult.put("postfx.mirage", 1.60f);
        p.primitiveWeightMult.put("fov.pulse", 1.25f);

        p.strengthMult = 1.20f;
        p.speedMult = 1.05f;

        p.earlyBias = 0.40f;
        p.peakBias = 1.45f;
        p.lateBias = 0.90f;
        p.chaos = 0.35f;

        return p;
    }
}
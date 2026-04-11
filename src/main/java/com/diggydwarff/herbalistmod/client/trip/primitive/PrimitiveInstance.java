package com.diggydwarff.herbalistmod.client.trip.primitive;

import java.util.HashMap;
import java.util.Map;

public final class PrimitiveInstance {
    public final Primitive primitive;
    public final PrimitiveSpec spec;
    public final long seed;

    // quick params map (replace later with typed params)
    private final Map<String, Float> f = new HashMap<>();

    public PrimitiveInstance(Primitive primitive, PrimitiveSpec spec, long seed) {
        this.primitive = primitive;
        this.spec = spec;
        this.seed = seed;
    }

    public void set(String key, float value) { f.put(key, value); }
    public float get(String key, float def) { return f.getOrDefault(key, def); }
}
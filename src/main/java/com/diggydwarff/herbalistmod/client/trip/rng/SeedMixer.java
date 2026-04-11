package com.diggydwarff.herbalistmod.client.trip.rng;

public final class SeedMixer {
    private SeedMixer() {}

    // SplitMix64 finalizer
    public static long mix64(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return z ^ (z >>> 33);
    }

    public static long sceneSeed(long baseSeed, int sceneIndex) {
        long x = baseSeed ^ (sceneIndex * 0x9E3779B97F4A7C15L);
        return mix64(x);
    }
}
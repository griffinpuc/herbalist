package com.diggydwarff.herbalistmod.client.trip.rng;

import java.util.SplittableRandom;

public final class SeededRng {
    private final SplittableRandom rng;

    public SeededRng(long seed) {
        this.rng = new SplittableRandom(seed);
    }

    public int nextInt(int minInclusive, int maxExclusive) {
        if (maxExclusive <= minInclusive) return minInclusive;
        return minInclusive + rng.nextInt(maxExclusive - minInclusive);
    }

    public float nextFloat(float minInclusive, float maxInclusive) {
        float t = (float) rng.nextDouble();
        return minInclusive + t * (maxInclusive - minInclusive);
    }

    public boolean chance(float p01) {
        return rng.nextDouble() < p01;
    }

    public long nextLong() {
        return rng.nextLong();
    }
}
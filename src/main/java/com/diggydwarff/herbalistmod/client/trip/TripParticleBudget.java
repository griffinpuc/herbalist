package com.diggydwarff.herbalistmod.client.trip;

public final class TripParticleBudget {
    private TripParticleBudget() {}

    private static int remaining = 0;

    public static void beginTick(int maxParticlesThisTick) {
        remaining = Math.max(0, maxParticlesThisTick);
    }

    // returns how many you are allowed to spawn now (0..requested)
    public static int request(int requested) {
        if (requested <= 0 || remaining <= 0) return 0;
        int grant = Math.min(requested, remaining);
        remaining -= grant;
        return grant;
    }
}
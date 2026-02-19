package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.client.trip.rng.SeedMixer;
import net.minecraft.client.Minecraft;

public final class TripState {
    public boolean active = true;     // TEMP: always true
    public int ticks = 0;

    public long tripSessionId = 0L;
    public long baseSeed = 0L;

    public float intensity = 0f; // 0..1

    public void ensureSession(Minecraft mc) {
        if (tripSessionId != 0L) return;

        // Session id: new each run (you can set this when consuming item/effect)
        tripSessionId = System.nanoTime();

        // baseSeed: deterministic-ish; world seed may be unavailable on servers, so don’t rely on it.
        long a = mc.player != null ? mc.player.getUUID().getLeastSignificantBits() : 1234L;
        long b = mc.player != null ? mc.player.getUUID().getMostSignificantBits() : 5678L;
        baseSeed = SeedMixer.mix64(a ^ SeedMixer.mix64(b ^ tripSessionId));
    }

    public void tick(Minecraft mc) {
        ticks++;

        // TEMP intensity curve (ramps up, holds, ramps down, loops)
        int cycle = 20 * 60; // 60s cycle
        int t = ticks % cycle;
        float x = t / (float) cycle;

        // triangle-ish
        float tri = x < 0.5f ? (x * 2f) : (2f - x * 2f);
        intensity = clamp01(tri);

        // Later: tie active/intensity to your status effect duration/amplifier.
    }

    private static float clamp01(float v) {
        return v < 0f ? 0f : Math.min(v, 1f);
    }
}
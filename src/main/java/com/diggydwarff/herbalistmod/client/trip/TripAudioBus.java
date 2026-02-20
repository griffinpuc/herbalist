package com.diggydwarff.herbalistmod.client.trip;

import net.minecraft.util.Mth;

public final class TripAudioBus {
    private TripAudioBus() {}

    // 0..1-ish
    private static float energy = 0f;

    public static float energy() {
        return energy;
    }

    public static void addImpulse(float amount) {
        energy = Mth.clamp(energy + amount, 0f, 1f);
    }

    // call once per client tick
    public static void tick() {
        // fast decay so it feels reactive
        energy *= 0.90f;
        energy -= 0.005f;
        if (energy < 0f) energy = 0f;
    }
}
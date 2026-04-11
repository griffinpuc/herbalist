package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.client.trip.rng.SeedMixer;
import net.minecraft.client.Minecraft;

import java.util.ArrayDeque;
import java.util.Deque;

public final class TripState {

    public boolean active = false;

    // Global tick counter while active
    public int ticks = 0;

    public long tripSessionId = 0L;
    public long baseSeed = 0L;

    // Duration control (from the triggering effect)
    public int totalTicks = 0;
    public int elapsedTicks = 0;

    // Phases (1 per minute, rounded up)
    public int phaseCount = 1;
    public int phaseIndex = 0;
    public int ticksPerPhase = 20 * 60;

    // Progress values
    public float progress = 0f;       // 0..1 across whole trip
    public float phaseProgress = 0f;  // 0..1 within current phase

    // Overall intensity (slow start -> peak -> come down)
    public float intensity = 0f;      // 0..1

    public TripProfile profile = new TripProfile("default");

    private final Deque<String> recentPrimitiveIds = new ArrayDeque<>();

    public void startTrip(Minecraft mc, long seed, TripProfile profile, int totalTicks) {
        this.active = true;
        this.ticks = 0;

        this.elapsedTicks = 0;
        this.totalTicks = Math.max(20, totalTicks);

        this.tripSessionId = System.nanoTime();
        this.profile = (profile != null) ? profile : new TripProfile("default");

        // 1 phase per minute (rounded up), minimum 1
        int minute = 20 * 60;
        this.phaseCount = Math.max(1, (this.totalTicks + (minute - 1)) / minute);
        this.ticksPerPhase = Math.max(20, this.totalTicks / this.phaseCount);
        this.phaseIndex = 0;

        this.progress = 0f;
        this.phaseProgress = 0f;
        this.intensity = 0f;

        long a = mc.player != null ? mc.player.getUUID().getLeastSignificantBits() : 1234L;
        long b = mc.player != null ? mc.player.getUUID().getMostSignificantBits() : 5678L;

        this.baseSeed = SeedMixer.mix64(
                seed
                        ^ SeedMixer.mix64(a)
                        ^ SeedMixer.mix64(b ^ tripSessionId)
        );

        recentPrimitiveIds.clear();
    }

    public void stopTrip() {
        this.active = false;
        this.ticks = 0;
        this.elapsedTicks = 0;
        this.totalTicks = 0;

        this.phaseCount = 1;
        this.phaseIndex = 0;
        this.ticksPerPhase = 20 * 60;

        this.progress = 0f;
        this.phaseProgress = 0f;
        this.intensity = 0f;

        recentPrimitiveIds.clear();
    }

    public void tick(Minecraft mc) {
        if (!active) return;

        ticks++;
        elapsedTicks++;

        if (totalTicks <= 0) totalTicks = 20 * 60;

        progress = clamp01(elapsedTicks / (float) totalTicks);

        int newPhase = Math.min(phaseCount - 1, elapsedTicks / ticksPerPhase);
        phaseIndex = newPhase;

        int phaseStart = phaseIndex * ticksPerPhase;
        phaseProgress = clamp01((elapsedTicks - phaseStart) / (float) ticksPerPhase);

        intensity = bellCurve(progress);
    }

    public void notePrimitive(String id) {
        if (id == null) return;
        recentPrimitiveIds.addLast(id);
        if (recentPrimitiveIds.size() > 8) recentPrimitiveIds.removeFirst();
    }

    public boolean recentlyUsed(String id) {
        return recentPrimitiveIds.contains(id);
    }

    // Compatibility aliases for older code
    public void remember(String id) { notePrimitive(id); }
    public boolean seenRecently(String id) { return recentlyUsed(id); }

    private static float bellCurve(float p) {
        // sin(pi * p): 0 at start/end, 1 in middle
        return (float) Math.sin(Math.PI * clamp01(p));
    }

    private static float clamp01(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}
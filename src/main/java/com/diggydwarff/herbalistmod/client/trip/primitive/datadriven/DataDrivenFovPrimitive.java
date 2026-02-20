package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraftforge.client.event.ViewportEvent;

public final class DataDrivenFovPrimitive implements Primitive, FovHook {
    private final PrimitiveDef def;

    public DataDrivenFovPrimitive(PrimitiveDef def) { this.def = def; }

    @Override public String id() { return def.id; }
    @Override public Tags tag() { return def.tag; }

    @Override
    public void onFov(TripState state, ViewportEvent.ComputeFov event, PrimitiveInstance inst) {
        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);
        if (strength < 0.02f) return;

        float speed = Math.max(0.01f, inst.get("speed", 0.6f));
        float t = state.ticks * 0.02f * speed;

        String mode = def.fovMode == null ? "sine" : def.fovMode;

        float delta;
        switch (mode) {
            case "dual" -> {
                float n = (float)Math.sin(t) * 0.6f + (float)Math.sin(t * 0.37f + 1.7f) * 0.4f;
                delta = n * def.fovAmp * strength;
            }
            case "jitter" -> {
                // deterministic-ish micro jitter from tick
                float n = (float)Math.sin(t * 9.7f + inst.seed * 0.000001f);
                delta = n * (def.fovAmp * 0.6f) * strength;
            }
            case "snap" -> {
                int every = Math.max(10, def.snapEveryTicks);
                boolean snap = (state.ticks % every) == 0;
                delta = snap ? def.fovAmp * strength : 0f;
            }
            default -> {
                delta = (float)Math.sin(t) * def.fovAmp * strength;
            }
        }

        event.setFOV(event.getFOV() + delta);
    }

    private static float clamp01(float v) { return v < 0 ? 0 : Math.min(v, 1); }
}
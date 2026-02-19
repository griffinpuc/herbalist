package com.diggydwarff.herbalistmod.client.trip.primitive.atmosphere;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraftforge.client.event.ViewportEvent;

public final class ContextTintPrimitive implements Primitive {

    @Override public String id() { return "atmo.context_tint"; }
    @Override public Tags tag() { return Tags.ATMOSPHERE; }

    @Override
    public void onFogColor(TripState state, ViewportEvent.ComputeFogColor e, PrimitiveInstance inst) {
        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);
        if (strength < 0.03f) return;

        // Choose target tint based on context at runtime
        float tr, tg, tb;

        if (state.intensity > 0.7f) {
            // deep “cosmic”
            tr = 0.15f; tg = 0.00f; tb = 0.35f;
        } else if (inst.get("speed", 0.6f) > 0.8f) {
            // neon-y
            tr = 0.35f; tg = 0.05f; tb = 0.55f;
        } else {
            // soft pastel
            tr = 0.55f; tg = 0.25f; tb = 0.70f;
        }

        e.setRed(lerp(e.getRed(), tr, strength));
        e.setGreen(lerp(e.getGreen(), tg, strength));
        e.setBlue(lerp(e.getBlue(), tb, strength));
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
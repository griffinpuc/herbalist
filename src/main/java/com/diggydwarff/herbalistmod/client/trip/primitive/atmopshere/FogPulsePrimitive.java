package com.diggydwarff.herbalistmod.client.trip.primitive.atmosphere;

import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraftforge.client.event.ViewportEvent;

public final class FogPulsePrimitive implements Primitive {

    @Override public String id() { return "atmo.fog_pulse"; }
    @Override public Tags tag() { return Tags.ATMOSPHERE; }

    @Override
    public void onFogColor(TripState state, ViewportEvent.ComputeFogColor e, PrimitiveInstance inst) {
        float strength = clamp01(inst.get("strength", 0.7f)) * clamp01(state.intensity);
        float speed = Math.max(0.01f, inst.get("speed", 0.6f));

        float t = state.ticks * 0.03f * speed;
        float pulse = 0.5f + 0.5f * (float)Math.sin(t); // 0..1

        // target swings between two weird colors
        float tr = lerp(0.10f, 0.60f, pulse);
        float tg = lerp(0.00f, 0.20f, pulse);
        float tb = lerp(0.25f, 0.95f, pulse);

        e.setRed(lerp(e.getRed(), tr, strength));
        e.setGreen(lerp(e.getGreen(), tg, strength * 0.8f));
        e.setBlue(lerp(e.getBlue(), tb, strength));
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
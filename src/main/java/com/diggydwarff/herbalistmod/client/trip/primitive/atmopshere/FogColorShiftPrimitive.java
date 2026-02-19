package com.diggydwarff.herbalistmod.client.trip.primitive.atmosphere;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraftforge.client.event.ViewportEvent;

public final class FogColorShiftPrimitive implements Primitive {

    @Override public String id() { return "atmo.fog_color_shift"; }
    @Override public Tags tag() { return Tags.ATMOSPHERE; }

    @Override
    public void onFogColor(TripState state, ViewportEvent.ComputeFogColor e, PrimitiveInstance inst) {
        float sceneAlpha = 1f; // you can pass scene alpha later; keep simple now
        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity) * sceneAlpha;

        // shift fog toward purple/green-ish using simple blend (cheap, not HSV)
        float r = e.getRed();
        float g = e.getGreen();
        float b = e.getBlue();

        float tr = 0.75f;  // target
        float tg = 0.25f;
        float tb = 0.90f;

        e.setRed(lerp(r, tr, strength));
        e.setGreen(lerp(g, tg, strength * 0.8f));
        e.setBlue(lerp(b, tb, strength));
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
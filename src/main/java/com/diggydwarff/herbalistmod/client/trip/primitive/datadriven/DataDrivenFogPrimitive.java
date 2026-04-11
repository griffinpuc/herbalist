package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.client.trip.TripAudioBus;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.Primitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveInstance;
import com.diggydwarff.herbalistmod.client.trip.primitive.Tags;
import net.minecraftforge.client.event.ViewportEvent;

public final class DataDrivenFogPrimitive implements Primitive {
    private final PrimitiveDef def;

    public DataDrivenFogPrimitive(PrimitiveDef def) { this.def = def; }

    @Override public String id() { return def.id; }
    @Override public Tags tag() { return def.tag; }

    @Override
    public void onFogColor(TripState state, ViewportEvent.ComputeFogColor e, PrimitiveInstance inst) {
        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);

        float audio = TripAudioBus.energy(); // 0..1
        float audioBoost = inst.get("audioBoost", 0.0f); // per primitive control
        strength *= (1.0f + audio * audioBoost);

        float speed = Math.max(0.01f, inst.get("speed", 0.6f));
        float t = state.ticks * 0.03f * speed;

        float r = e.getRed(), g = e.getGreen(), b = e.getBlue();

        String mode = def.fogMode == null ? "tint" : def.fogMode;
        float[] a = color(def.colorA, 0.55f, 0.15f, 0.75f);
        float[] c;

        switch (mode) {
            case "pulse" -> {
                float[] bb = color(def.colorB, 0.10f, 0.00f, 0.25f);
                float pulse = 0.5f + 0.5f * (float)Math.sin(t);
                c = new float[] { lerp(a[0], bb[0], pulse), lerp(a[1], bb[1], pulse), lerp(a[2], bb[2], pulse) };
            }
            case "grayscale" -> {
                float gray = (r + g + b) / 3f;
                float gm = clamp01(def.grayMix) * strength;
                r = lerp(r, gray, gm);
                g = lerp(g, gray, gm);
                b = lerp(b, gray, gm);
                c = a;
            }
            case "crush" -> {
                float gray = (r + g + b) / 3f;
                r = lerp(r, gray, 0.45f * strength);
                g = lerp(g, gray, 0.45f * strength);
                b = lerp(b, gray, 0.45f * strength);
                c = a;
            }
            default -> c = a;
        }

        e.setRed(lerp(r, c[0], strength));
        e.setGreen(lerp(g, c[1], strength));
        e.setBlue(lerp(b, c[2], strength));
    }

    private static float[] color(float[] arr, float dr, float dg, float db) {
        if (arr == null || arr.length < 3) return new float[]{dr, dg, db};
        return new float[]{arr[0], arr[1], arr[2]};
    }
    private static float lerp(float x, float y, float t) { return x + (y - x) * t; }
    private static float clamp01(float v) { return v < 0 ? 0 : Math.min(v, 1); }
}
package com.diggydwarff.herbalistmod.client.trip.primitive.camera;

import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraftforge.client.event.ViewportEvent;

public final class FovDriftPrimitive implements Primitive, FovHook {

    @Override public String id() { return "camera.fov_drift"; }
    @Override public Tags tag() { return Tags.CAMERA; }

    @Override
    public void onFov(TripState state, ViewportEvent.ComputeFov event, PrimitiveInstance inst) {
        float strength = clamp01(inst.get("strength", 0.5f)) * clamp01(state.intensity);
        if (strength < 0.03f) return;

        float speed = Math.max(0.01f, inst.get("speed", 0.6f));

        // cheap "noise": sum of two sines with different rates (good enough)
        float t = state.ticks * 0.02f * speed;
        float n = (float)Math.sin(t) * 0.6f + (float)Math.sin(t * 0.37f + 1.7f) * 0.4f;

        float delta = n * 3.0f * strength; // keep small
        event.setFOV(event.getFOV() + delta);
    }

    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
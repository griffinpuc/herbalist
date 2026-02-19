package com.diggydwarff.herbalistmod.client.trip.primitive.camera;

import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraftforge.client.event.ViewportEvent;

public final class FovPulsePrimitive implements Primitive, FovHook {

    @Override public String id() { return "camera.fov_pulse"; }
    @Override public Tags tag() { return Tags.CAMERA; }

    @Override
    public void onFov(TripState state, ViewportEvent.ComputeFov event, PrimitiveInstance inst) {

        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);
        float speed = Math.max(0.01f, inst.get("speed", 0.6f));

        float t = state.ticks * 0.05f * speed;
        float pulse = (float) Math.sin(t) * 15f * strength;

        event.setFOV(event.getFOV() + pulse);
    }

    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
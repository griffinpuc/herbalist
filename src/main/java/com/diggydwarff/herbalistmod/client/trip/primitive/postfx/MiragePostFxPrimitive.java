package com.diggydwarff.herbalistmod.client.trip.primitive.postfx;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraft.client.Minecraft;

public final class MiragePostFxPrimitive implements Primitive {

    @Override public String id() { return "postfx.mirage"; }
    @Override public Tags tag() { return Tags.POSTFX; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        float strength = clamp01(inst.get("strength", 0.8f)) * clamp01(state.intensity);

        // TODO: replace these with your real post-fx controller calls
        // Example pattern:
        // MirageEffectRenderer.setEnabled(strength > 0.01f);
        // MirageEffectRenderer.setStrength(strength);

        PostFxBridge.setMirageStrength(strength);
    }

    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }

    // Minimal stub. Replace with your actual renderer integration.
    static final class PostFxBridge {
        static void setMirageStrength(float strength) {
            // no-op until wired
        }
    }
}
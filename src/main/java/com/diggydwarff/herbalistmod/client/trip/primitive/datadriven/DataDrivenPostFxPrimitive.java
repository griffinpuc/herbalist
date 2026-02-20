package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripPostFxManager;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.Primitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveInstance;
import com.diggydwarff.herbalistmod.client.trip.primitive.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public final class DataDrivenPostFxPrimitive implements Primitive {

    private final PrimitiveDef def;
    private final ResourceLocation shader;

    public DataDrivenPostFxPrimitive(PrimitiveDef def) {
        this.def = def;
        this.shader = def.shader == null ? null : ResourceLocation.tryParse(def.shader);
    }

    @Override public String id() { return def.id; }
    @Override public Tags tag() { return def.tag; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc == null || mc.level == null || mc.player == null) return;
        if (shader == null) return;

        float strength = clamp01(inst.get("strength", 0.65f)) * clamp01(state.intensity);
        if (strength < 0.02f) return;

        float speed = clamp01(inst.get("speed", 0.6f));

        // Load / take ownership
        TripPostFxManager.ensure(mc, state.tripSessionId, shader);

        // Per-instance time
        float t = inst.get("time", 0f);
        t += 0.05f * (0.25f + 1.75f * speed);
        inst.set("time", t);

        // Common uniforms (silently ignored if shader doesn't have them)
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Time", t);
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Intensity", strength);

        // Provide a few common knobs for different shaders
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Strength", 0.0025f + 0.0075f * strength);
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Scale", 1.0f + 1.5f * strength);
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Chromatic", 0.0008f + 0.0022f * strength);
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Fade", Mth.clamp(strength, 0f, 1f));

        // Phase/progress uniforms for shaders that want it
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Progress", clamp01(state.progress));
        TripPostFxManager.setUniformsIfOwned(mc, state.tripSessionId, "Phase", state.phaseProgress);
    }

    private static float clamp01(float v) { return v < 0 ? 0 : Math.min(v, 1); }
}
package com.diggydwarff.herbalistmod.client.trip.primitive.entity;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;

public final class SwarmOrbitParticlesPrimitive implements Primitive {

    @Override public String id() { return "entity.swarm_orbit_particles"; }
    @Override public Tags tag() { return Tags.ENTITY; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc.level == null || mc.player == null) return;

        float strength = clamp01(inst.get("strength", 0.8f)) * clamp01(state.intensity);
        if (strength < 0.05f) return;

        int count = 6 + (int)(strength * 14);
        double radius = 2.5 + strength * 5.0;

        double base = state.ticks * 0.05 * inst.get("speed", 0.6f);

        double px = mc.player.getX();
        double py = mc.player.getY() + 1.0;
        double pz = mc.player.getZ();

        for (int i = 0; i < count; i++) {
            double ang = base + (Math.PI * 2.0 * i / count);

            double x = px + Math.cos(ang) * radius;
            double z = pz + Math.sin(ang) * radius;
            double y = py + Math.sin(ang * 2.0) * 0.6;

            mc.level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0.01, 0);
        }
    }

    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
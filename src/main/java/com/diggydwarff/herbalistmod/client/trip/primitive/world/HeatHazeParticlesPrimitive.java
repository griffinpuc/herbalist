package com.diggydwarff.herbalistmod.client.trip.primitive.world;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public final class HeatHazeParticlesPrimitive implements Primitive {

    @Override public String id() { return "world.heat_haze_particles"; }
    @Override public Tags tag() { return Tags.WORLD; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc.level == null || mc.player == null) return;

        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);
        if (strength < 0.05f) return;

        Level level = mc.level;
        int count = Mth.clamp((int)(strength * 14), 2, 18);

        double px = mc.player.getX();
        double py = mc.player.getY() + 0.2;
        double pz = mc.player.getZ();

        for (int i = 0; i < count; i++) {
            double ox = (level.random.nextDouble() - 0.5) * 10.0;
            double oz = (level.random.nextDouble() - 0.5) * 10.0;
            double oy = level.random.nextDouble() * 1.8;

            // use small puffs; reads like shimmer/dust
            double vx = (level.random.nextDouble() - 0.5) * 0.01;
            double vy = level.random.nextDouble() * 0.01;
            double vz = (level.random.nextDouble() - 0.5) * 0.01;

            level.addParticle(ParticleTypes.ASH, px + ox, py + oy, pz + oz, vx, vy, vz);
        }
    }

    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
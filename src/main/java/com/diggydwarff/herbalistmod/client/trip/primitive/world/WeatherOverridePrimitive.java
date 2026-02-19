package com.diggydwarff.herbalistmod.client.trip.primitive.world;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public final class WeatherOverridePrimitive implements Primitive {

    @Override public String id() { return "world.weather_override"; }
    @Override public Tags tag() { return Tags.WORLD; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc.level == null || mc.player == null) return;

        float strength = clamp01(inst.get("strength", 0.7f)) * clamp01(state.intensity);
        if (strength <= 0.01f) return;

        Level level = mc.level;

        int count = Mth.clamp((int)(strength * 18), 2, 25);
        double px = mc.player.getX();
        double py = mc.player.getY() + 2.0;
        double pz = mc.player.getZ();

        // Pick particle type by context
        var particle = ctx.indoors ? ParticleTypes.SPORE_BLOSSOM_AIR : (ctx.nearWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.SNOWFLAKE);

        for (int i = 0; i < count; i++) {
            double ox = (level.random.nextDouble() - 0.5) * 14.0;
            double oy = (level.random.nextDouble()) * 6.0;
            double oz = (level.random.nextDouble() - 0.5) * 14.0;

            double vx = (level.random.nextDouble() - 0.5) * 0.02;
            double vy = -0.02 - level.random.nextDouble() * 0.03;
            double vz = (level.random.nextDouble() - 0.5) * 0.02;

            level.addParticle(particle, px + ox, py + oy, pz + oz, vx, vy, vz);
        }
    }

    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
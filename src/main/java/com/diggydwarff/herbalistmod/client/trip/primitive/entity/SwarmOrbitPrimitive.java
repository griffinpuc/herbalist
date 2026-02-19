package com.diggydwarff.herbalistmod.client.trip.primitive.entity;

import com.diggydwarff.herbalistmod.client.trip.*;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;

public final class SwarmOrbitPrimitive implements Primitive {

    @Override public String id() { return "entity.swarm_orbit"; }
    @Override public Tags tag() { return Tags.ENTITY; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc.player == null || mc.level == null) return;

        float strength = inst.get("strength", 0.8f) * state.intensity;
        int count = (int)(6 + strength * 10);

        double radius = 3 + strength * 5;
        double baseAngle = state.ticks * 0.03 * inst.get("speed", 0.6f);

        for (int i = 0; i < count; i++) {
            double angle = baseAngle + (Math.PI * 2 * i / count);
            double x = mc.player.getX() + Math.cos(angle) * radius;
            double z = mc.player.getZ() + Math.sin(angle) * radius;
            double y = mc.player.getY() + 1.0;

            mc.level.addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    x, y, z,
                    0, 0.01, 0
            );
        }
    }
}
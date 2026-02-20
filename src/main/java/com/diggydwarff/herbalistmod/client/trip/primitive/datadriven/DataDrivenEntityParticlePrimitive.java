package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripParticleBudget;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.Primitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveInstance;
import com.diggydwarff.herbalistmod.client.trip.primitive.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public final class DataDrivenEntityParticlePrimitive implements Primitive {
    private final PrimitiveDef def;

    // Very small cache for afterimage mode
    private static final Map<Integer, Vec3> LAST_POS = new HashMap<>();
    private static int lastCleanTick = 0;

    public DataDrivenEntityParticlePrimitive(PrimitiveDef def) { this.def = def; }

    @Override public String id() { return def.id; }
    @Override public Tags tag() { return def.tag; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc.level == null || mc.player == null) return;

        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);
        if (strength < 0.15f) return;

        // Movement-aware density control (less while moving)
        double hSpeed = mc.player.getDeltaMovement().horizontalDistance();
        float move01 = Mth.clamp((float)(hSpeed / 0.18), 0f, 1f);

        int interval = 1 + (int)(move01 * 2.0f);
        if ((mc.player.tickCount % interval) != 0) return;

        float moveDensity = 1.0f - 0.75f * move01;
        float density = Mth.clamp(inst.get("density", 1.0f), 0f, 2f);
        float finalDensity = Mth.clamp(density * moveDensity, 0f, 1f);

        String mode = def.entityPattern == null ? "ring" : def.entityPattern;
        ParticleOptions p = resolveParticle(def.particle);

        int max = Math.max(1, def.entityMax);
        float rad = def.entityRadius <= 0 ? 1.2f : def.entityRadius;

        int used = 0;
        double base = state.ticks * 0.08 * (0.25 + 1.75 * clamp01(inst.get("speed", 0.6f)));

        // Periodic cache cleanup (avoid unbounded growth)
        if (state.ticks - lastCleanTick > 200) {
            LAST_POS.clear();
            lastCleanTick = state.ticks;
        }

        for (Entity e : mc.level.entitiesForRendering()) {
            if (e == mc.player) continue;
            if (e.distanceToSqr(mc.player) > 18 * 18) continue;

            switch (mode) {
                case "trail" -> {
                    if (mc.level.random.nextFloat() < finalDensity) {
                        if (TripParticleBudget.request(1) > 0) {
                            mc.level.addParticle(p, e.getX(), e.getY() + 0.9, e.getZ(), 0, 0.01, 0);
                        }
                    }
                }
                case "halo" -> {
                    int points = Math.max(1, Math.round((4 + (strength > 0.65f ? 6 : 2)) * finalDensity));
                    double y = e.getY() + e.getBbHeight() + 0.15;
                    for (int i = 0; i < points; i++) {
                        double ang = base + (Math.PI * 2.0 * i / (double) points);
                        double x = e.getX() + Math.cos(ang) * rad;
                        double z = e.getZ() + Math.sin(ang) * rad;
                        if (TripParticleBudget.request(1) > 0) {
                            mc.level.addParticle(p, x, y, z, 0, 0.01, 0);
                        }
                    }
                }
                case "double_ring" -> {
                    int points = Math.max(1, Math.round((4 + (strength > 0.65f ? 6 : 2)) * finalDensity));
                    for (int i = 0; i < points; i++) {
                        double ang = base + (Math.PI * 2.0 * i / (double) points);
                        double x1 = e.getX() + Math.cos(ang) * rad;
                        double z1 = e.getZ() + Math.sin(ang) * rad;
                        double x2 = e.getX() + Math.cos(-ang) * rad;
                        double z2 = e.getZ() + Math.sin(-ang) * rad;
                        double y1 = e.getY() + 0.9;
                        double y2 = e.getY() + 0.2 + e.getBbHeight() * 0.35;
                        if (TripParticleBudget.request(1) > 0) {
                            mc.level.addParticle(p, x1, y1, z1, 0, 0.01, 0);
                            if (i + 1 < points) mc.level.addParticle(p, x2, y2, z2, 0, 0.01, 0);
                        }
                    }
                }
                case "orbit" -> {
                    // single point orbiting around entity
                    if (mc.level.random.nextFloat() < finalDensity) {
                        double ang = base;
                        double x = e.getX() + Math.cos(ang) * rad;
                        double z = e.getZ() + Math.sin(ang) * rad;
                        double y = e.getY() + 0.9 + Math.sin(ang * 2.0) * 0.2;
                        if (TripParticleBudget.request(1) > 0) {
                            mc.level.addParticle(p, x, y, z, 0, 0.01, 0);
                        }
                    }
                }
                case "afterimage" -> {
                    // emit at previous position every few ticks
                    int every = 2 + (int)(3.0f * (1.0f - strength));
                    if ((state.ticks % every) == 0 && mc.level.random.nextFloat() < finalDensity) {
                        Vec3 prev = LAST_POS.get(e.getId());
                        if (prev != null) {
                            if (TripParticleBudget.request(1) > 0) {
                                mc.level.addParticle(p, prev.x, prev.y + 0.9, prev.z, 0, 0.01, 0);
                            }
                        }
                        LAST_POS.put(e.getId(), e.position());
                    } else {
                        LAST_POS.put(e.getId(), e.position());
                    }
                }
                default -> { // "ring"
                    int points = Math.max(1, Math.round((4 + (strength > 0.65f ? 4 : 0)) * finalDensity));
                    for (int i = 0; i < points; i++) {
                        double ang = base + (Math.PI * 2.0 * i / (double) points);
                        double x = e.getX() + Math.cos(ang) * rad;
                        double z = e.getZ() + Math.sin(ang) * rad;
                        double y = e.getY() + 0.9;
                        if (TripParticleBudget.request(1) > 0) {
                            mc.level.addParticle(p, x, y, z, 0, 0.01, 0);
                        }
                    }
                }
            }

            used++;
            if (used >= max) break;
        }
    }

    private static ParticleOptions resolveParticle(String id) {
        if (id == null || id.isBlank()) return ParticleTypes.END_ROD;

        try {
            ResourceLocation rl = ResourceLocation.tryParse(id);
            if (rl == null) return ParticleTypes.END_ROD;

            ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(rl);
            if (type instanceof SimpleParticleType simple) {
                return simple;
            }
        } catch (Throwable ignored) {
        }
        return ParticleTypes.END_ROD;
    }

    private static float clamp01(float v) { return v < 0 ? 0 : Math.min(v, 1); }
}
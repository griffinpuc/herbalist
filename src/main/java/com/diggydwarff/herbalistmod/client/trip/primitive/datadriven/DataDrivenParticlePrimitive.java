package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.client.trip.TripAudioBus;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class DataDrivenParticlePrimitive implements Primitive {
    private final PrimitiveDef def;

    public DataDrivenParticlePrimitive(PrimitiveDef def) { this.def = def; }

    @Override public String id() { return def.id; }
    @Override public Tags tag() { return def.tag; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc.level == null || mc.player == null) return;

        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);
        if (strength < 0.05f) return;

        float audio = TripAudioBus.energy(); // 0..1
        float audioBoost = inst.get("audioBoost", 0.0f); // per primitive control
        strength *= (1.0f + audio * audioBoost);

        Level level = mc.level;
        ParticleOptions p = resolveParticle(def.particle);

        // Movement-aware density control (less while moving)
        double hSpeed = mc.player.getDeltaMovement().horizontalDistance(); // blocks/tick
        float move01 = Mth.clamp((float) (hSpeed / 0.18), 0f, 1f);         // ~0 idle, ~1 sprint-ish

        int interval = 1 + (int)(move01 * 2.0f);                           // 1..3 ticks
        if ((mc.player.tickCount % interval) != 0) return;

        float moveDensity = 1.0f - 0.75f * move01;                         // down to 25%
        float density = Mth.clamp(inst.get("density", 1.0f), 0f, 2f);     // optional per-instance
        float finalDensity = Mth.clamp(density * moveDensity, 0f, 1.0f);

        int count = Mth.clamp((int)(lerp(def.minCount, def.maxCount, strength)), def.minCount, def.maxCount);
        count = Math.max(0, Math.round(count * finalDensity));

        count = TripParticleBudget.request(count);
        if (count <= 0) return;

        if (count <= 0) return;

        float r = def.radius <= 0 ? 10f : def.radius;

        String pattern = def.pattern == null ? "cloud" : def.pattern;

        double px = mc.player.getX();
        double py = mc.player.getY();
        double pz = mc.player.getZ();

        float speed = clamp01(inst.get("speed", 0.6f));
        double base = state.ticks * 0.05 * (0.25 + 1.75 * speed);

        switch (pattern) {
            case "ring" -> {
                double rad = 2.5 + strength * 5.0;
                for (int i = 0; i < count; i++) {
                    double ang = base + (Math.PI * 2.0 * i / Math.max(1, count));
                    double x = px + Math.cos(ang) * rad;
                    double z = pz + Math.sin(ang) * rad;
                    double y = py + 1.0 + Math.sin(ang * 2.0) * 0.6;
                    level.addParticle(p, x, y, z, 0, 0.01, 0);
                }
            }
            case "spiral" -> {
                // Expanding spiral around player
                double rad = 1.5 + 7.0 * strength;
                for (int i = 0; i < count; i++) {
                    double t = (i / (double)Math.max(1, count));
                    double ang = base + t * 8.0 * Math.PI;
                    double rr = rad * t;
                    double x = px + Math.cos(ang) * rr;
                    double z = pz + Math.sin(ang) * rr;
                    double y = py + 0.8 + (t * 2.5) + Math.sin(ang) * 0.2;
                    level.addParticle(p, x, y, z, 0, 0.01, 0);
                }
            }
            case "helix" -> {
                // Two counter-rotating helices
                double rad = 2.0 + 4.0 * strength;
                double h = 3.5 + 3.0 * strength;
                for (int i = 0; i < count; i++) {
                    double t = (i / (double)Math.max(1, count));
                    double ang = base + t * 10.0 * Math.PI;
                    double y = py + 0.5 + t * h;
                    double x1 = px + Math.cos(ang) * rad;
                    double z1 = pz + Math.sin(ang) * rad;
                    double x2 = px + Math.cos(-ang) * rad;
                    double z2 = pz + Math.sin(-ang) * rad;
                    level.addParticle(p, x1, y, z1, 0, 0.01, 0);
                    if (i + 1 < count) level.addParticle(p, x2, y, z2, 0, 0.01, 0);
                }
            }
            case "shockwave" -> {
                // Expanding ring pulse
                double rad = 1.0 + (base % 6.0) * (1.2 + 2.5 * strength);
                for (int i = 0; i < count; i++) {
                    double ang = base * 0.9 + (Math.PI * 2.0 * i / Math.max(1, count));
                    double x = px + Math.cos(ang) * rad;
                    double z = pz + Math.sin(ang) * rad;
                    double y = py + 0.2 + (level.random.nextDouble() * 0.3);
                    level.addParticle(p, x, y, z, 0, 0.02, 0);
                }
            }
            case "vortex_column" -> {
                // Twisting vertical column around player
                double rad = 0.8 + 2.8 * strength;
                double h = 2.5 + 5.0 * strength;
                for (int i = 0; i < count; i++) {
                    double t = level.random.nextDouble();
                    double ang = base + t * 12.0 * Math.PI;
                    double rr = rad * (0.3 + 0.7 * (1.0 - t));
                    double x = px + Math.cos(ang) * rr;
                    double z = pz + Math.sin(ang) * rr;
                    double y = py + def.yMin + t * h;
                    double vy = 0.01 + 0.03 * strength;
                    level.addParticle(p, x, y, z, 0, vy, 0);
                }
            }
            case "comet_streaks" -> {
                // Spawn ahead of look direction and drift back
                Vec3 look = mc.player.getLookAngle().normalize();
                double dist = 2.0 + 7.0 * strength;
                double cx = px + look.x * dist;
                double cz = pz + look.z * dist;
                double cy = py + 1.5;
                for (int i = 0; i < count; i++) {
                    double ox = (level.random.nextDouble() - 0.5) * (1.5 + 4.0 * strength);
                    double oy = (level.random.nextDouble() - 0.5) * (0.6 + 1.0 * strength);
                    double oz = (level.random.nextDouble() - 0.5) * (1.5 + 4.0 * strength);
                    double vx = -look.x * (0.02 + 0.05 * strength) + (level.random.nextDouble() - 0.5) * 0.01;
                    double vz = -look.z * (0.02 + 0.05 * strength) + (level.random.nextDouble() - 0.5) * 0.01;
                    double vy = (level.random.nextDouble() - 0.5) * 0.01;
                    level.addParticle(p, cx + ox, cy + oy, cz + oz, vx, vy, vz);
                }
            }
            case "ground_crawl" -> {
                // Low-to-ground drift around player
                for (int i = 0; i < count; i++) {
                    double ox = (level.random.nextDouble() - 0.5) * r;
                    double oz = (level.random.nextDouble() - 0.5) * r;
                    double x = px + ox;
                    double z = pz + oz;
                    double y = py + 0.05 + level.random.nextDouble() * 0.25;
                    double vx = (level.random.nextDouble() - 0.5) * 0.01;
                    double vy = 0.005 + level.random.nextDouble() * 0.01;
                    double vz = (level.random.nextDouble() - 0.5) * 0.01;
                    level.addParticle(p, x, y, z, vx, vy, vz);
                }
            }
            case "horizon_blob" -> {
                Vec3 look = mc.player.getLookAngle().normalize();
                double dist = 90.0 + 80.0 * strength;
                double cx = px + look.x * dist;
                double cz = pz + look.z * dist;
                double cy = py + 10.0;
                for (int i = 0; i < count; i++) {
                    double ox = (level.random.nextDouble() - 0.5) * (20.0 + 30.0 * strength);
                    double oz = (level.random.nextDouble() - 0.5) * (20.0 + 30.0 * strength);
                    double oy = level.random.nextDouble() * (8.0 + 18.0 * strength);
                    level.addParticle(p, cx + ox, cy + oy, cz + oz, 0, 0.0, 0);
                }
            }
            case "upward_snow" -> {
                for (int i = 0; i < count; i++) {
                    double ox = (level.random.nextDouble() - 0.5) * r;
                    double oz = (level.random.nextDouble() - 0.5) * r;
                    double oy = def.yMin + level.random.nextDouble() * Math.max(0.1, (def.yMax - def.yMin));
                    double vx = (level.random.nextDouble() - 0.5) * 0.01;
                    double vy = 0.02 + level.random.nextDouble() * 0.03;
                    double vz = (level.random.nextDouble() - 0.5) * 0.01;
                    level.addParticle(p, px + ox, py + oy, pz + oz, vx, vy, vz);
                }
            }
            default -> { // "cloud"
                for (int i = 0; i < count; i++) {
                    double ox = (level.random.nextDouble() - 0.5) * r;
                    double oz = (level.random.nextDouble() - 0.5) * r;
                    double oy = def.yMin + level.random.nextDouble() * Math.max(0.1, (def.yMax - def.yMin));
                    double vx = (level.random.nextDouble() - 0.5) * 0.02;
                    double vy = -0.02 - level.random.nextDouble() * 0.03;
                    double vz = (level.random.nextDouble() - 0.5) * 0.02;
                    level.addParticle(p, px + ox, py + 2.0 + oy, pz + oz, vx, vy, vz);
                }
            }
        }
    }

    private static ParticleOptions resolveParticle(String id) {
        if (id == null || id.isBlank()) return ParticleTypes.ASH;

        try {
            ResourceLocation rl = ResourceLocation.tryParse(id);
            if (rl == null) return ParticleTypes.ASH;

            ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(rl);
            if (type instanceof SimpleParticleType simple) {
                return simple;
            }
        } catch (Throwable ignored) {
        }
        // Non-simple particles (dust, block, item, vibration, etc.) need params.
        return ParticleTypes.ASH;
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float clamp01(float v) { return v < 0 ? 0 : Math.min(v, 1); }
}

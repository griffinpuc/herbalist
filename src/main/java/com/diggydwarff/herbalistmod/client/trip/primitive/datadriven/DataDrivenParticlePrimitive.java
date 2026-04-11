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
import net.minecraft.util.RandomSource;

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

        // Sound-driven instability multiplier (optional)
        float audio = TripAudioBus.energy(); // 0..1
        float audioBoost = inst.get("audioBoost", 0.0f);
        strength *= (1.0f + audio * audioBoost);

        Level level = mc.level;
        ParticleOptions p = resolveParticle(def.particle);

        // Movement-aware density control (less while moving)
        double hSpeed = mc.player.getDeltaMovement().horizontalDistance(); // blocks/tick
        float move01 = Mth.clamp((float) (hSpeed / 0.18), 0f, 1f);         // ~0 idle, ~1 sprint-ish

        int interval = 1 + (int) (move01 * 2.0f);                          // 1..3 ticks
        if ((mc.player.tickCount % interval) != 0) return;

        float moveDensity = 1.0f - 0.75f * move01;                         // down to 25%
        float density = Mth.clamp(inst.get("density", 1.0f), 0f, 2f);
        float finalDensity = Mth.clamp(density * moveDensity, 0f, 1.0f);

        int count = Mth.clamp((int) (lerp(def.minCount, def.maxCount, strength)), def.minCount, def.maxCount);
        count = Math.max(0, Math.round(count * finalDensity));
        count = TripParticleBudget.request(count);
        if (count <= 0) return;

        // Radius controls:
        // - radius comes from def.radius
        // - minRadius comes from instance param, defaults to pushing things outward
        float maxR = (def.radius <= 0) ? 10f : def.radius;
        float minR = Mth.clamp(inst.get("minRadius", Math.min(3.0f, maxR * 0.45f)), 0.0f, Math.max(0.0f, maxR - 0.25f));

        String pattern = (def.pattern == null) ? "cloud" : def.pattern;

        double px = mc.player.getX();
        double py = mc.player.getY();
        double pz = mc.player.getZ();

        float speed = clamp01(inst.get("speed", 0.6f));
        double base = state.ticks * 0.05 * (0.25 + 1.75 * speed);

        // Helper to sample a point in an annulus (minR..maxR) evenly by area
        RandomSource rng = level.random;

        switch (pattern) {
            case "ring" -> {
                // Ring is already "farther out" by design, keep it but use min/max bounds
                double rad = Mth.clamp((float) (2.5 + strength * 5.0), minR, maxR);
                for (int i = 0; i < count; i++) {
                    double ang = base + (Math.PI * 2.0 * i / Math.max(1, count));
                    double x = px + Math.cos(ang) * rad;
                    double z = pz + Math.sin(ang) * rad;
                    double y = py + 1.0 + Math.sin(ang * 2.0) * 0.6;
                    level.addParticle(p, x, y, z, 0, 0.01, 0);
                }
            }
            case "spiral" -> {
                // Spiral expands outward; clamp inner portion to minR so it doesn't clog the camera
                double rad = Math.max(minR, 1.5 + 7.0 * strength);
                double cap = Math.max(rad, maxR);
                for (int i = 0; i < count; i++) {
                    double t = (i / (double) Math.max(1, count));
                    double ang = base + t * 8.0 * Math.PI;
                    double rr = rad * t;
                    rr = Mth.clamp((float) rr, minR, (float) cap);

                    double x = px + Math.cos(ang) * rr;
                    double z = pz + Math.sin(ang) * rr;
                    double y = py + 0.8 + (t * 2.5) + Math.sin(ang) * 0.2;
                    level.addParticle(p, x, y, z, 0, 0.01, 0);
                }
            }
            case "helix" -> {
                // Helix around player; keep it outside minR so it doesn't block view
                double rad = Mth.clamp((float) (2.0 + 4.0 * strength), minR, maxR);
                double h = 3.5 + 3.0 * strength;
                for (int i = 0; i < count; i++) {
                    double t = (i / (double) Math.max(1, count));
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
                // Expanding ring pulse; clamp to [minR..maxR]
                double rad = 1.0 + (base % 6.0) * (1.2 + 2.5 * strength);
                rad = Mth.clamp((float) rad, minR, maxR);

                for (int i = 0; i < count; i++) {
                    double ang = base * 0.9 + (Math.PI * 2.0 * i / Math.max(1, count));
                    double x = px + Math.cos(ang) * rad;
                    double z = pz + Math.sin(ang) * rad;
                    double y = py + 0.2 + (rng.nextDouble() * 0.3);
                    level.addParticle(p, x, y, z, 0, 0.02, 0);
                }
            }
            case "vortex_column" -> {
                // Column around player; keep outside minR (prevents camera clutter)
                double rad = Mth.clamp((float) (0.8 + 2.8 * strength), minR, maxR);
                double h = 2.5 + 5.0 * strength;

                for (int i = 0; i < count; i++) {
                    double t = rng.nextDouble();
                    double ang = base + t * 12.0 * Math.PI;

                    // Bias towards outer radii (less center clutter)
                    double rr = rad * (0.65 + 0.35 * rng.nextDouble());

                    double x = px + Math.cos(ang) * rr;
                    double z = pz + Math.sin(ang) * rr;
                    double y = py + def.yMin + t * h;
                    double vy = 0.01 + 0.03 * strength;
                    level.addParticle(p, x, y, z, 0, vy, 0);
                }
            }
            case "comet_streaks" -> {
                // This one is in front of view; reduce obstruction by increasing distance + spreading
                Vec3 look = mc.player.getLookAngle().normalize();
                double dist = (minR + 2.0) + (maxR + 2.0) * strength; // push further out
                double cx = px + look.x * dist;
                double cz = pz + look.z * dist;
                double cy = py + 1.5;

                for (int i = 0; i < count; i++) {
                    double ox = (rng.nextDouble() - 0.5) * (1.5 + 4.0 * strength);
                    double oy = (rng.nextDouble() - 0.5) * (0.6 + 1.0 * strength);
                    double oz = (rng.nextDouble() - 0.5) * (1.5 + 4.0 * strength);

                    double vx = -look.x * (0.02 + 0.05 * strength) + (rng.nextDouble() - 0.5) * 0.01;
                    double vz = -look.z * (0.02 + 0.05 * strength) + (rng.nextDouble() - 0.5) * 0.01;
                    double vy = (rng.nextDouble() - 0.5) * 0.01;

                    level.addParticle(p, cx + ox, cy + oy, cz + oz, vx, vy, vz);
                }
            }
            case "ground_crawl" -> {
                // Use annulus sample (minR..maxR) instead of square fill
                for (int i = 0; i < count; i++) {
                    double ang = rng.nextDouble() * Math.PI * 2.0;
                    double rr = randRadius(minR, maxR, rng);

                    double x = px + Math.cos(ang) * rr;
                    double z = pz + Math.sin(ang) * rr;
                    double y = py + 0.05 + rng.nextDouble() * 0.25;

                    double vx = (rng.nextDouble() - 0.5) * 0.01;
                    double vy = 0.005 + rng.nextDouble() * 0.01;
                    double vz = (rng.nextDouble() - 0.5) * 0.01;

                    level.addParticle(p, x, y, z, vx, vy, vz);
                }
            }
            case "horizon_blob" -> {
                // Already far; keep as-is
                Vec3 look = mc.player.getLookAngle().normalize();
                double dist = 90.0 + 80.0 * strength;
                double cx = px + look.x * dist;
                double cz = pz + look.z * dist;
                double cy = py + 10.0;

                for (int i = 0; i < count; i++) {
                    double ox = (rng.nextDouble() - 0.5) * (20.0 + 30.0 * strength);
                    double oz = (rng.nextDouble() - 0.5) * (20.0 + 30.0 * strength);
                    double oy = rng.nextDouble() * (8.0 + 18.0 * strength);
                    level.addParticle(p, cx + ox, cy + oy, cz + oz, 0, 0.0, 0);
                }
            }
            case "upward_snow" -> {
                // Use annulus sample (minR..maxR) instead of square fill
                for (int i = 0; i < count; i++) {
                    double ang = rng.nextDouble() * Math.PI * 2.0;
                    double rr = randRadius(minR, maxR, rng);

                    double oy = def.yMin + rng.nextDouble() * Math.max(0.1, (def.yMax - def.yMin));
                    double vx = (rng.nextDouble() - 0.5) * 0.01;
                    double vy = 0.02 + rng.nextDouble() * 0.03;
                    double vz = (rng.nextDouble() - 0.5) * 0.01;

                    level.addParticle(p,
                            px + Math.cos(ang) * rr,
                            py + oy,
                            pz + Math.sin(ang) * rr,
                            vx, vy, vz);
                }
            }
            default -> { // "cloud"
                // Use annulus sample (minR..maxR) instead of square fill
                for (int i = 0; i < count; i++) {
                    double ang = rng.nextDouble() * Math.PI * 2.0;
                    double rr = randRadius(minR, maxR, rng);

                    double oy = def.yMin + rng.nextDouble() * Math.max(0.1, (def.yMax - def.yMin));
                    double vx = (rng.nextDouble() - 0.5) * 0.02;
                    double vy = -0.02 - rng.nextDouble() * 0.03;
                    double vz = (rng.nextDouble() - 0.5) * 0.02;

                    level.addParticle(p,
                            px + Math.cos(ang) * rr,
                            py + 2.0 + oy,
                            pz + Math.sin(ang) * rr,
                            vx, vy, vz);
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

    private static double randRadius(double minR, double maxR, RandomSource rng){
        // uniform area in annulus
        double t = rng.nextDouble();
        double r2 = minR*minR + t * (maxR*maxR - minR*minR);
        return Math.sqrt(r2);
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float clamp01(float v) { return v < 0 ? 0 : Math.min(v, 1); }
}

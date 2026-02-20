package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.Primitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveInstance;
import com.diggydwarff.herbalistmod.client.trip.primitive.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class DataDrivenAudioPrimitive implements Primitive {
    private final PrimitiveDef def;

    public DataDrivenAudioPrimitive(PrimitiveDef def) { this.def = def; }

    @Override public String id() { return def.id; }
    @Override public Tags tag() { return def.tag; }

    @Override
    public void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {
        if (mc.level == null || mc.player == null) return;

        float strength = clamp01(inst.get("strength", 0.6f)) * clamp01(state.intensity);
        if (strength < 0.2f) return;

        String mode = (def.audioMode == null || def.audioMode.isBlank()) ? "scatter" : def.audioMode;

        SoundEvent se = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(def.sound));
        if (se == null) return;

        int minD = Math.max(20, def.minDelayTicks);
        int maxD = Math.max(minD + 1, def.maxDelayTicks);
        int period = minD + (int)((maxD - minD) * 0.5f);

        int offset = (int)(Math.abs(inst.seed) % period);
        if (((state.ticks + offset) % period) != 0) return;

        float volRand = mc.level.random.nextFloat();
        float pitchRand = mc.level.random.nextFloat();

        float vol = lerp(def.volMin, def.volMax, volRand);
        float pitch = lerp(def.pitchMin, def.pitchMax, pitchRand);

        // Tie some modes to intensity more tightly
        if (mode.equals("pulse")) {
            float i = Mth.clamp(strength, 0f, 1f);
            vol *= (0.55f + 0.65f * i);
            pitch = lerp(pitch, pitch * (0.85f + 0.35f * i), 0.8f);
        }

        double r = def.soundRadius <= 0 ? 60 : def.soundRadius;
        Vec3 pos = switch (mode) {
            case "orbit" -> orbitPos(mc, inst, state, r);
            case "flyby" -> flybyPos(mc, inst, state, r);
            case "call_response" -> callResponsePos(mc, inst, state, r);
            case "pulse" -> nearPos(mc, r * 0.35);
            default -> scatterPos(mc, r);
        };

        mc.level.playLocalSound(pos.x, pos.y, pos.z, se, SoundSource.AMBIENT, vol, pitch, false);
    }

    private static Vec3 scatterPos(Minecraft mc, double r) {
        double x = mc.player.getX() + (mc.level.random.nextDouble() - 0.5) * r;
        double z = mc.player.getZ() + (mc.level.random.nextDouble() - 0.5) * r;
        double y = mc.player.getY() + 2.0 + mc.level.random.nextDouble() * 15.0;
        return new Vec3(x, y, z);
    }

    private static Vec3 nearPos(Minecraft mc, double r) {
        double x = mc.player.getX() + (mc.level.random.nextDouble() - 0.5) * r;
        double z = mc.player.getZ() + (mc.level.random.nextDouble() - 0.5) * r;
        double y = mc.player.getY() + 1.6 + mc.level.random.nextDouble() * 2.0;
        return new Vec3(x, y, z);
    }

    private static Vec3 orbitPos(Minecraft mc, PrimitiveInstance inst, TripState state, double r) {
        double ang = (state.ticks * 0.035) + ((inst.seed & 0xFFFF) / 65535.0) * Math.PI * 2.0;
        double rr = r * (0.25 + 0.35 * state.intensity);
        double x = mc.player.getX() + Math.cos(ang) * rr;
        double z = mc.player.getZ() + Math.sin(ang) * rr;
        double y = mc.player.getY() + 2.0 + Math.sin(ang * 2.0) * 1.5;
        return new Vec3(x, y, z);
    }

    private static Vec3 flybyPos(Minecraft mc, PrimitiveInstance inst, TripState state, double r) {
        // deterministic direction from seed
        double ang0 = ((inst.seed >>> 16) & 0xFFFF) / 65535.0 * Math.PI * 2.0;
        Vec3 dir = new Vec3(Math.cos(ang0), 0, Math.sin(ang0)).normalize();

        double t = ((state.ticks + (inst.seed & 0xFF)) % 240) / 240.0; // 0..1
        double along = (t - 0.5) * (r * 1.2);
        double side = (mc.level.random.nextDouble() - 0.5) * (r * 0.10);

        Vec3 right = new Vec3(-dir.z, 0, dir.x);

        double x = mc.player.getX() + dir.x * along + right.x * side;
        double z = mc.player.getZ() + dir.z * along + right.z * side;
        double y = mc.player.getY() + 2.0 + mc.level.random.nextDouble() * 6.0;
        return new Vec3(x, y, z);
    }

    private static Vec3 callResponsePos(Minecraft mc, PrimitiveInstance inst, TripState state, double r) {
        // alternate left/right around player
        boolean left = (((state.ticks / 20) + (int)(inst.seed & 3)) % 2) == 0;
        Vec3 look = mc.player.getLookAngle().normalize();
        Vec3 right = new Vec3(-look.z, 0, look.x).normalize();
        double side = (left ? -1.0 : 1.0) * (r * 0.18);
        double x = mc.player.getX() + right.x * side;
        double z = mc.player.getZ() + right.z * side;
        double y = mc.player.getY() + 1.8 + mc.level.random.nextDouble() * 2.5;
        return new Vec3(x, y, z);
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float clamp01(float v) { return v < 0 ? 0 : Math.min(v, 1); }
}
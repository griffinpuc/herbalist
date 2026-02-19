package com.diggydwarff.herbalistmod.client.trip.primitive.world;

import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public final class HorizonSilhouettePrimitive implements Primitive {

    @Override public String id() { return "world.horizon_silhouette"; }
    @Override public Tags tag() { return Tags.WORLD; }

    @Override
    public void onRenderStage(TripState state, RenderLevelStageEvent e, PrimitiveInstance inst) {
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        float strength = clamp01(inst.get("strength", 0.8f)) * clamp01(state.intensity);
        if (strength < 0.05f) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        PoseStack pose = e.getPoseStack();
        pose.pushPose();

        // Put a quad far away in view-space (straight ahead).
        // This rides the camera because it’s drawn in the level render pipeline.
        float dist = 140f;
        float w = 70f + 80f * strength;
        float h = 18f + 45f * strength;

        // slight wobble so it feels alive
        float t = state.ticks * 0.02f * inst.get("speed", 0.6f);
        float wob = (float)Math.sin(t) * 6f * strength;

        pose.translate(0.0, 20.0 + wob, -dist);

        Matrix4f m = pose.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tes = Tesselator.getInstance();
        BufferBuilder buf = tes.getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // purple/black silhouette
        float r1 = 0.10f, g1 = 0.00f, b1 = 0.18f;
        float r2 = 0.20f, g2 = 0.02f, b2 = 0.30f;
        float a = 0.25f + 0.55f * strength;

        buf.vertex(m, -w, 0, 0).color(r1, g1, b1, a).endVertex();
        buf.vertex(m,  w, 0, 0).color(r1, g1, b1, a).endVertex();
        buf.vertex(m,  w, h, 0).color(r2, g2, b2, a).endVertex();
        buf.vertex(m, -w, h, 0).color(r2, g2, b2, a).endVertex();

        tes.end();
        RenderSystem.disableBlend();

        pose.popPose();
    }

    private static float clamp01(float v) { return v < 0f ? 0f : Math.min(v, 1f); }
}
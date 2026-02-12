package com.diggydwarff.herbalistmod.client.render;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.effect.ModEffects;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

public class TurtleVisionEffectRenderer {

    public static final int MAX_INTROSPECTION_DISTANCE = 10;
    public static final ResourceLocation TURTLE_VISION_SHADER = new ResourceLocation(HerbalistMod.MODID,
            "shaders/post/turtle_vision.json");

    public static final ResourceLocation TURTLE_VISION_TEXTURE = new ResourceLocation(HerbalistMod.MODID,
            "textures/overlay/introspection.png");
    public boolean effectActiveLastTick = false;

    public Set<BlockPos> uncoveredBlocks = new HashSet<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide && event.player == Minecraft.getInstance().player) {
            this.onEffectTick(event);
        }
    }


    public void renderOverlay(PoseStack pose) {
        RenderSystem.setShaderTexture(0, TurtleVisionEffectRenderer.TURTLE_VISION_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);

        RenderSystem.clearColor(1, 1, 1, 1);

        Window window = Minecraft.getInstance().getWindow();
        pose.pushPose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(0.0D, window.getGuiScaledHeight(), -90.0D).uv(0.0f, 1.0f).endVertex();
        buffer.vertex(window.getGuiScaledWidth(), window.getGuiScaledHeight(), -90.0D)
                .uv(1.0f, 1.0f).endVertex();
        buffer.vertex(window.getGuiScaledWidth(), 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
        buffer.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
        tessellator.end();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        pose.popPose();

        RenderSystem.clearColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    public void onEffectTick(TickEvent.PlayerTickEvent event) {
        MobEffectInstance effect = event.player.getEffect(ModEffects.TURTLE_VISION.get());
        int duration = effect == null ? 0 : effect.getDuration();
        if (duration > 1) {
            if (!this.effectActiveLastTick) {
                this.effectActiveLastTick = true;

                Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.loadEffect(TURTLE_VISION_SHADER));
            }
        } else {
            if (this.effectActiveLastTick) {
                this.effectActiveLastTick = false;

                Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.shutdownEffect());
            }
        }
    }
}

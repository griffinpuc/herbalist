package com.diggydwarff.herbalistmod.client.handler;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT)
public final class TripTransitionOverlay {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post e) {
        float a = com.diggydwarff.herbalistmod.client.handler.TripTransitionClient.get().alpha();
        if (a <= 0.001f) return;

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics gg = e.getGuiGraphics();

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        gg.fill(0, 0, w, h, ((int)(a * 255) << 24));
        RenderSystem.disableBlend();
    }

    private TripTransitionOverlay() {}
}
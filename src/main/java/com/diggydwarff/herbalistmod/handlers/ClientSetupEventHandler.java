package com.diggydwarff.herbalistmod.handlers;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEventHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        //Register client side event handlers
        MinecraftForge.EVENT_BUS.register(HerbalistMod.BLAZED_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.DELRIUM_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.INTROSPECTION_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.DESERT_VISION_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.RIPPED_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.INTROSPECTIONII_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.TURTLE_VISION_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.AMPED_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.ACID_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.FRACTAL_EFFECT_RENDERER);
        MinecraftForge.EVENT_BUS.register(HerbalistMod.REALITY_BEND);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("blazed", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.BLAZED_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.BLAZED_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

        event.registerAboveAll("delirium", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.DELRIUM_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.DELRIUM_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

        event.registerAboveAll("introspection", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.INTROSPECTION_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.INTROSPECTION_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

        event.registerAboveAll("introspectionii", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.INTROSPECTIONII_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.INTROSPECTIONII_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

        event.registerAboveAll("desert_vision", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.DESERT_VISION_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.DESERT_VISION_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

        event.registerAboveAll("ripped", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.RIPPED_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.RIPPED_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

        event.registerAboveAll("turtle_vision", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.TURTLE_VISION_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.TURTLE_VISION_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

        event.registerAboveAll("amped", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            if (HerbalistMod.AMPED_EFFECT_RENDERER.effectActiveLastTick) {
                gui.setupOverlayRenderState(true, false);
                HerbalistMod.AMPED_EFFECT_RENDERER.renderOverlay(guiGraphics.pose());
            }
        });

    }


}

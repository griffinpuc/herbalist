package com.diggydwarff.herbalistmod;

import com.diggydwarff.herbalistmod.block.entity.ModBlockEntities;
import com.diggydwarff.herbalistmod.client.render.BlazedEffectRenderer;
import com.diggydwarff.herbalistmod.client.render.DeliriumEffectRenderer;
import com.diggydwarff.herbalistmod.client.render.IntrospectiveEffectRenderer;
import com.diggydwarff.herbalistmod.effect.BlazedEffect;
import com.diggydwarff.herbalistmod.effect.ModEffects;
import com.diggydwarff.herbalistmod.items.ModFoods;
import com.diggydwarff.herbalistmod.sound.ModSounds;
import com.mojang.logging.LogUtils;
import com.diggydwarff.herbalistmod.block.ModBlocks;
import com.diggydwarff.herbalistmod.items.ModItems;
import com.diggydwarff.herbalistmod.recipes.ModRecipes;
import com.diggydwarff.herbalistmod.villager.ModVillagers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HerbalistMod.MODID)
public class HerbalistMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "herbalistmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final BlazedEffectRenderer BLAZED_EFFECT_RENDERER = new BlazedEffectRenderer();
    public static final DeliriumEffectRenderer DELRIUM_EFFECT_RENDERER = new DeliriumEffectRenderer();
    public static final IntrospectiveEffectRenderer INTROSPECTION_EFFECT_RENDERER = new IntrospectiveEffectRenderer();

    public HerbalistMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        HerbalistCreativeTab.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::register);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModRecipes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MinecraftForge.EVENT_BUS.register(HerbalistMod.BLAZED_EFFECT_RENDERER);
            MinecraftForge.EVENT_BUS.register(HerbalistMod.DELRIUM_EFFECT_RENDERER);
            MinecraftForge.EVENT_BUS.register(HerbalistMod.INTROSPECTION_EFFECT_RENDERER);

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENDERPEARL_HAZE_BLAZEBUD_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.NETHERWART_KUSH_BLAZEBUD_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.REDSTONE_KUSH_BLAZEBUD_CROP.get(), RenderType.cutout());

        }
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
    }
}

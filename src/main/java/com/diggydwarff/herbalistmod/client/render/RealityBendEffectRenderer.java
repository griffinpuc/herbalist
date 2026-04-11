package com.diggydwarff.herbalistmod.client.render;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RealityBendEffectRenderer {

    public static final ResourceLocation REALITY_BEND_SHADER = new ResourceLocation(
            HerbalistMod.MODID, "shaders/post/reality_bend.json"
    );

    private boolean effectActiveLastTick = false;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide && event.player == Minecraft.getInstance().player) {
            onEffectTick(event);
        }
    }

    private void onEffectTick(TickEvent.PlayerTickEvent event) {
        MobEffectInstance effect = event.player.getEffect(ModEffects.REALITY_BEND.get());
        int duration = effect == null ? 0 : effect.getDuration();

        if (duration > 1) {
            if (!effectActiveLastTick) {
                effectActiveLastTick = true;
                Minecraft.getInstance().tell(() ->
                        Minecraft.getInstance().gameRenderer.loadEffect(REALITY_BEND_SHADER)
                );
            }
        } else {
            if (effectActiveLastTick) {
                effectActiveLastTick = false;
                Minecraft.getInstance().tell(() ->
                        Minecraft.getInstance().gameRenderer.shutdownEffect()
                );
            }
        }
    }
}
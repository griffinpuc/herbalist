package com.diggydwarff.herbalistmod.client.handler;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= HerbalistMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT )
public class ClientForgeHandeler {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
    }

}

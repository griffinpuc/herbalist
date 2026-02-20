package com.diggydwarff.herbalistmod.client.handler;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.client.trip.TripDirector;
import com.diggydwarff.herbalistmod.client.trip.TripProfile;
import com.diggydwarff.herbalistmod.client.trip.TripProfiles;
import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveRegistry;
import com.diggydwarff.herbalistmod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT)
public final class TripClientEvents {

    private static boolean primBoot = false;

    private static boolean bootstrapped = false;
    private static boolean ddLoaded = false;

    private static boolean wasActive = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        if (!ddLoaded && mc.level != null) {
            ddLoaded = true;
            System.out.println("[Trip] FORCING datadriven load now");
            com.diggydwarff.herbalistmod.client.trip.primitive.datadriven.DataDrivenPrimitiveLoader.loadAllIntoRegistry();
            System.out.println("[Trip] After load, primitive count = " + com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveRegistry.specs().size());
        }


        // Start/stop based on effect presence
        Player player = mc.player;
        if (player != null) {
            MobEffectInstance a = player.getEffect(ModEffects.INTROSPECTION.get());
            MobEffectInstance b = player.getEffect(ModEffects.INTROSPECTIONII.get());

            boolean active = (a != null) || (b != null);

            if (active && !wasActive) {
                MobEffectInstance inst = (b != null) ? b : a;
                int totalTicks = (inst != null) ? inst.getDuration() : (20 * 60);

                long seed =
                        mc.level.getGameTime()
                                ^ player.getUUID().getLeastSignificantBits()
                                ^ System.nanoTime();

                TripProfile profile = (b != null)
                        ? TripProfiles.introspectionII()
                        : TripProfiles.introspectionI();

                TripDirector.get().startTrip(mc, seed, profile, totalTicks);
            } else if (!active && wasActive) {
                TripDirector.get().stopTrip(mc);
            }

            wasActive = active;
        }

        TripDirector.get().tick(mc);
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        TripDirector.get().onFogColor(event);
    }

    @SubscribeEvent
    public static void onFov(ViewportEvent.ComputeFov event) {
        TripDirector.get().onFov(event);
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        TripDirector.get().onRenderStage(event);
    }
}
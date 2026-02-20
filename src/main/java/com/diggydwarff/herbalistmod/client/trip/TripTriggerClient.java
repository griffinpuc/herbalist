package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.effect.ModEffects; // <-- adjust to your actual class
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT)
public final class TripTriggerClient {

    private static boolean wasActive = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

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
        }

        if (active) {
            MobEffectInstance inst = (b != null) ? b : a;
            int remaining = (inst != null) ? inst.getDuration() : 0;

            TripDirector.get().updateTotalTicksFromRemaining(remaining);
        }

        if (!active && wasActive) {
            TripDirector.get().stopTrip();
        }

        wasActive = active;

        TripDirector.get().tick(mc);

    }
}
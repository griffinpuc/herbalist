package com.diggydwarff.herbalistmod.client.handler;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.client.trip.TripPostFxManager;
import com.diggydwarff.herbalistmod.world.deeptrip.TripDimensions;
import com.diggydwarff.herbalistmod.world.deeptrip.TripProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT)
public final class DeepTripVisualsClient {

    private static final ResourceLocation COLORGRADE_SHADER =
            new ResourceLocation(HerbalistMod.MODID, "shaders/post/deeptrip_colorgrade.json");

    private static boolean wasInTrip = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        // ALWAYS tick transition even while the world is unloading/loading
        com.diggydwarff.herbalistmod.client.handler.TripTransitionClient.get().tick(mc);

        // Everything below this needs a world/player
        if (mc.level == null || mc.player == null) return;

        boolean inTrip = mc.level.dimension() == TripDimensions.TRIP_LEVEL;
        long seed = com.diggydwarff.herbalistmod.client.handler.TripTransitionClient.get().deepTripSeed();
        long sessionId = com.diggydwarff.herbalistmod.client.handler.TripTransitionClient.get().deepTripSessionId();

        if (!inTrip) {
            TripPostFxManager.shutdownIfOwned(mc, sessionId);
            return;
        }
        if (seed == 0L || sessionId == 0L) return;

        TripProfile profile = TripProfile.fromSeed(seed);
        TripProfile.VisualParams v = profile.visuals;

        TripPostFxManager.ensure(mc, sessionId, COLORGRADE_SHADER);

        float time = (float) (mc.level.getGameTime() % 240000L) / 20.0f;

        TripPostFxManager.setUniformsIfOwned(mc, sessionId, "Time", time);
        TripPostFxManager.setUniformsIfOwned(mc, sessionId, "HueShift", v.hueShift());
        TripPostFxManager.setUniformsIfOwned(mc, sessionId, "HueDriftSpeed", v.hueDriftSpeed());
        TripPostFxManager.setUniformsIfOwned(mc, sessionId, "Saturation", v.saturation());
        TripPostFxManager.setUniformsIfOwned(mc, sessionId, "Contrast", v.contrast());
        TripPostFxManager.setUniformsIfOwned(mc, sessionId, "Exposure", v.exposure());
        TripPostFxManager.setUniformsIfOwned(mc, sessionId, "Vignette", v.vignette());
    }

    private DeepTripVisualsClient() {}
}
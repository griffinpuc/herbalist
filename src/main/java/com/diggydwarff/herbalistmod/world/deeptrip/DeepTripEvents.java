package com.diggydwarff.herbalistmod.world.deeptrip;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerPlayer;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID)
public final class DeepTripEvents {

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer p)) return;

        // If they are in trip mode but ended up somewhere else, force back
        if (DeepTripManager.isActive(p) && p.serverLevel().dimension() != TripDimensions.TRIP_LEVEL) {
            DeepTripManager.enter(p); // blunt: re-enter puts them back in trip
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer p)) return;
        // optional: cleanup dummy body on logout
        if (DeepTripManager.isActive(p)) {
            DeepTripManager.exit(p);
        }
    }

    private DeepTripEvents() {}
}
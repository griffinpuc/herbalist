package com.diggydwarff.herbalistmod.world.deeptrip;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID)
public final class DeepTripTransitionQueue {

    private record Pending(boolean enter, int ticksLeft) {}

    private static final Map<UUID, Pending> PENDING = new HashMap<>();

    private DeepTripTransitionQueue() {}

    public static void scheduleEnter(ServerPlayer p, int delayTicks) {
        PENDING.put(p.getUUID(), new Pending(true, Math.max(0, delayTicks)));
    }

    public static void scheduleExit(ServerPlayer p, int delayTicks) {
        PENDING.put(p.getUUID(), new Pending(false, Math.max(0, delayTicks)));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        if (PENDING.isEmpty()) return;

        Iterator<Map.Entry<UUID, Pending>> it = PENDING.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            UUID id = entry.getKey();
            Pending p = entry.getValue();

            ServerPlayer sp = e.getServer().getPlayerList().getPlayer(id);
            if (sp == null) { it.remove(); continue; }

            int next = p.ticksLeft - 1;
            if (next > 0) {
                entry.setValue(new Pending(p.enter, next));
                continue;
            }

            // execute
            if (p.enter) DeepTripManager.enterInternal(sp);
            else DeepTripManager.exitInternal(sp);

            it.remove();
        }
    }
}
package com.diggydwarff.herbalistmod.world.deeptrip;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID)
public final class DeepTripDropTick {
    private static final String TAG_ROOT = "herbalistmod_deeptrip";

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer p)) return;

        CompoundTag pd = p.getPersistentData();
        if (!pd.contains(TAG_ROOT)) return;

        CompoundTag root = pd.getCompound(TAG_ROOT);
        if (!root.getBoolean("dropping")) return;

        int lx = root.getInt("landingX");
        int ly = root.getInt("landingY");
        int lz = root.getInt("landingZ");

        // If they drift, keep them centered during the descent
        p.setPos(lx + 0.5, p.getY(), lz + 0.5);

        // Cap falling speed (negative Y). Smaller magnitude = slower fall.
        double vy = p.getDeltaMovement().y;
        double capped = -0.06; // tweak: -0.03 very slow, -0.12 faster
        if (vy < capped) p.setDeltaMovement(p.getDeltaMovement().x * 0.2, capped, p.getDeltaMovement().z * 0.2);

        // Stop the drop when near the landing height
        if (p.getY() <= ly) {
            p.teleportTo(p.serverLevel(), lx + 0.5, ly, lz + 0.5, p.getYRot(), p.getXRot());
            p.setDeltaMovement(0, 0, 0);
            root.putBoolean("dropping", false);
            pd.put(TAG_ROOT, root);

            // You can choose to keep invulnerable for the whole trip or disable it here:
            // p.setInvulnerable(false);
        }
    }
}
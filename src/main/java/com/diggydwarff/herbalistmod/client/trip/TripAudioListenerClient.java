package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT)
public final class TripAudioListenerClient {

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        if (event.getSound() == null) return;

        SoundInstance s = event.getSound();

        SoundSource src = s.getSource();

        float vol;
        float pitch;

        try {
            vol = s.getVolume();
            pitch = s.getPitch();
        } catch (Exception e) {
            // Some sounds are not fully ready yet
            return;
        }

        if (vol <= 0f) return;

        float impulse = vol * 0.35f;

        impulse *= 0.85f + 0.15f * Math.abs(pitch - 1.0f);
        impulse = Math.min(impulse, 0.25f);

        TripAudioBus.addImpulse(impulse);
    }

}
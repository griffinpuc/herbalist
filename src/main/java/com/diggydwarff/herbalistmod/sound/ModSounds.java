package com.diggydwarff.herbalistmod.sound;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HerbalistMod.MODID);

    public static final RegistryObject<SoundEvent> TROPIC_HERB =
            SOUND_EVENTS.register("tropic_herb",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(HerbalistMod.MODID, "tropic_herb")));

    public static final RegistryObject<SoundEvent> PRISM =
            SOUND_EVENTS.register("prism",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(HerbalistMod.MODID, "prism")));

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
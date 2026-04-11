package com.diggydwarff.herbalistmod.world.deeptrip;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public final class TripDimensions {
    public static final ResourceKey<Level> TRIP_LEVEL =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(HerbalistMod.MODID, "trip"));

    private TripDimensions() {}
}
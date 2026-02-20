package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class DeepTripSavedData extends SavedData {
    private static final String ID = "herbalistmod_deeptrip";
    private int nextRegion = 0;

    public static DeepTripSavedData get(ServerLevel overworld) {
        return overworld.getDataStorage().computeIfAbsent(DeepTripSavedData::load, DeepTripSavedData::new, ID);
    }

    public int allocateRegion() {
        setDirty();
        return nextRegion++;
    }

    public static DeepTripSavedData load(CompoundTag tag) {
        DeepTripSavedData d = new DeepTripSavedData();
        d.nextRegion = tag.getInt("nextRegion");
        return d;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("nextRegion", nextRegion);
        return tag;
    }
}
package com.diggydwarff.herbalistmod.client.trip.primitive;

import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveInstance;
import net.minecraftforge.client.event.ViewportEvent;

public interface FovHook {
    void onFov(TripState state, ViewportEvent.ComputeFov event, PrimitiveInstance inst);
}
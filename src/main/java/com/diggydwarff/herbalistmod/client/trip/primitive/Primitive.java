package com.diggydwarff.herbalistmod.client.trip.primitive;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;

public interface Primitive {
    String id();
    Tags tag();

    default void tick(TripState state, TripContext ctx, Minecraft mc, PrimitiveInstance inst) {}
    default void onFogColor(TripState state, ViewportEvent.ComputeFogColor e, PrimitiveInstance inst) {}
    default void onRenderStage(TripState state, RenderLevelStageEvent e, PrimitiveInstance inst) {}
}
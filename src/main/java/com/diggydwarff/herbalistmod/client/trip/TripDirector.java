package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveRegistry;
import com.diggydwarff.herbalistmod.client.trip.scene.GeneratedScene;
import com.diggydwarff.herbalistmod.client.trip.scene.SceneGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;

public final class TripDirector {
    private static final TripDirector INSTANCE = new TripDirector();
    public static TripDirector get() { return INSTANCE; }

    private final TripState state = new TripState();
    private final TripContextSampler contextSampler = new TripContextSampler();

    private GeneratedScene current;
    private int sceneIndex = 0;

    private TripDirector() {
        PrimitiveRegistry.bootstrap();
    }

    public void tick(Minecraft mc) {
        // TEMP activation (you already have this working)
        state.ensureSession(mc);
        state.tick(mc);

        if (!state.active) {
            current = null;
            return;
        }

        TripContext ctx = contextSampler.sample(mc);

        if (current == null || current.isDone(state.ticks)) {
            current = SceneGenerator.generate(state, ctx, sceneIndex++);
        }

        System.out.println("[Trip] Scene " + sceneIndex + " intensity=" + state.intensity
                + " prims=" + current.instances.stream().map(i -> i.spec.id).toList());

        current.tick(state, ctx, mc);
    }

    public void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (current == null || !state.active) return;
        current.onFogColor(state, event);
    }

    public void onFov(ViewportEvent.ComputeFov event) {
        if (current == null || !state.active) return;
        current.onFov(state, event);
    }

    public void onRenderStage(RenderLevelStageEvent event) {
        if (current == null || !state.active) return;
        current.onRenderStage(state, event);
    }
}
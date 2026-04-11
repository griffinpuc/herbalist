package com.diggydwarff.herbalistmod.client.trip;

import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveRegistry;
import com.diggydwarff.herbalistmod.client.trip.scene.GeneratedScene;
import com.diggydwarff.herbalistmod.client.trip.scene.SceneGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;

import java.util.List;

public final class TripDirector {
    private static final TripDirector INSTANCE = new TripDirector();
    public static TripDirector get() { return INSTANCE; }

    private final TripState state = new TripState();
    private final TripContextSampler contextSampler = new TripContextSampler();

    private GeneratedScene current;
    private int sceneIndex = 0;
    private int lastPhaseIndex = -1;

    private boolean debugCycleEnabled = false;
    private int debugIndex = 0;
    private int debugPeriodTicks = 5 * 20;
    private int debugNextSwitchTick = 0;
    private String debugForcedPrimitiveId = null;

    private TripDirector() {
        PrimitiveRegistry.bootstrap();
    }

    public void startTrip(Minecraft mc, long seed, TripProfile profile, int totalTicks) {
        state.startTrip(mc, seed, profile, totalTicks);
        current = null;
        sceneIndex = 0;
        lastPhaseIndex = -1;
        TripPostFxManager.shutdownIfOwned(mc, state.tripSessionId); // ensure clean
    }

    public void stopTrip(Minecraft mc) {
        state.stopTrip();
        current = null;
        lastPhaseIndex = -1;
        // if you do postfx cleanup, do it here too
    }

    public boolean isActive() { return state.active; }

    // For renderers / primitives to scale by phase bias
    public float intensity() { return state.intensity; }
    public float progress() { return state.progress; }
    public float phaseProgress() { return state.phaseProgress; }
    public int phaseIndex() { return state.phaseIndex; }
    public int phaseCount() { return state.phaseCount; }

    public void tick(Minecraft mc) {
        state.tick(mc);

        if (!state.active) {
            current = null;
            return;
        }

        if (!state.active) { current = null; return; }

        // Hard cap. Tune these.
        // 6–10 is mild, 12–18 medium, 20+ heavy.
        int cap = 12;

        // Also scale cap by intensity if you want:
        cap = Math.round(6 + 14 * state.intensity); // 6..20

        TripParticleBudget.beginTick(cap);

        TripContext ctx = contextSampler.sample(mc);

        boolean phaseChanged = (state.phaseIndex != lastPhaseIndex);
        lastPhaseIndex = state.phaseIndex;

        // Regenerate at phase boundaries
        if (current == null || phaseChanged || current.isDone(state.ticks)) {
            if (phaseChanged && mc != null) {
                TripPostFxManager.shutdownIfOwned(mc, state.tripSessionId);
            }
            current = SceneGenerator.generate(state, ctx, sceneIndex++);
        }

        current.tick(state, ctx, mc);
    }

    public void debugStartCycle(int seconds) {
        debugCycleEnabled = true;
        debugPeriodTicks = Math.max(1, seconds) * 20;
        debugNextSwitchTick = state.ticks; // switch immediately on next tick
        debugForcedPrimitiveId = null;
        current = null;
    }

    public void debugStopCycle() {
        debugCycleEnabled = false;
        debugForcedPrimitiveId = null;
        current = null;
    }

    public void debugNext() {
        debugCycleEnabled = true;
        debugNextSwitchTick = state.ticks; // force immediate switch
        debugForcedPrimitiveId = null;
        current = null;
    }

    public void debugSet(String id) {
        debugCycleEnabled = false;
        debugForcedPrimitiveId = id;
        current = null;
    }

    public List<String> debugListIds() {
        return PrimitiveRegistry.specs().stream().map(s -> s.id).sorted().toList();
    }

    public void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (current == null || !state.active) return;
        current.onFogColor(state, event);
    }

    public void onFov(ViewportEvent.ComputeFov event) {
        if (current == null || !state.active) return;
        current.onFov(state, event);
    }

    public void stopTrip() {
        stopTrip(Minecraft.getInstance());
    }

    public void onRenderStage(RenderLevelStageEvent event) {
        if (current == null || !state.active) return;
        current.onRenderStage(state, event);
    }

    public void updateTotalTicksFromRemaining(int remainingTicks) {
        if (!state.active) return;
        int total = Math.max(state.elapsedTicks + Math.max(0, remainingTicks), 20);
        state.totalTicks = Math.max(state.totalTicks, total);

        // recompute phase split too if total grew
        int minute = 20 * 60;
        state.phaseCount = Math.max(1, (state.totalTicks + (minute - 1)) / minute);
        state.ticksPerPhase = Math.max(20, state.totalTicks / state.phaseCount);
    }
}
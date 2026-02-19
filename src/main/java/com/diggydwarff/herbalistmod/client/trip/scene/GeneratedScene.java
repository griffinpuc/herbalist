package com.diggydwarff.herbalistmod.client.trip.scene;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.FovHook;
import com.diggydwarff.herbalistmod.client.trip.primitive.PrimitiveInstance;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;

import java.util.List;

public final class GeneratedScene {
    public final int startTick;
    public final int durationTicks;
    public final int fadeTicks;
    public final List<PrimitiveInstance> instances;

    public GeneratedScene(int startTick, int durationTicks, int fadeTicks, List<PrimitiveInstance> instances) {
        this.startTick = startTick;
        this.durationTicks = durationTicks;
        this.fadeTicks = fadeTicks;
        this.instances = instances;
    }

    public boolean isDone(int nowTick) {
        return nowTick > startTick + durationTicks;
    }

    public float sceneAlpha(int nowTick) {
        int t = nowTick - startTick;
        if (t < 0) return 0f;
        if (fadeTicks <= 0) return 1f;

        if (t < fadeTicks) return t / (float) fadeTicks;

        int endFadeStart = durationTicks - fadeTicks;
        if (t > endFadeStart) {
            int tt = durationTicks - t;
            return Math.max(0f, tt / (float) fadeTicks);
        }
        return 1f;
    }

    public void tick(TripState state, TripContext ctx, Minecraft mc) {
        for (PrimitiveInstance inst : instances) {
            inst.primitive.tick(state, ctx, mc, inst);
        }
    }

    public void onFogColor(TripState state, ViewportEvent.ComputeFogColor e) {
        for (PrimitiveInstance inst : instances) {
            inst.primitive.onFogColor(state, e, inst);
        }
    }

    public void onFov(TripState state, ViewportEvent.ComputeFov event) {
        for (PrimitiveInstance inst : instances) {
            if (inst.primitive instanceof FovHook hook) {
                hook.onFov(state, event, inst);
            }
        }
    }

    public void onRenderStage(TripState state, RenderLevelStageEvent e) {
        for (PrimitiveInstance inst : instances) {
            inst.primitive.onRenderStage(state, e, inst);
        }
    }
}
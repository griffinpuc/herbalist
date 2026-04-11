package com.diggydwarff.herbalistmod.client.trip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Manages ownership of the vanilla post chain while the trip system is active.
 * We only shut down effects that were loaded by this manager (tracked by session id).
 */
public final class TripPostFxManager {

    private static ResourceLocation activeShader = null;
    private static long ownerSessionId = 0L;
    private static boolean effectLoaded = false;

    private TripPostFxManager() {}

    public static void ensure(Minecraft mc, long sessionId, ResourceLocation shader) {
        if (mc == null) return;
        if (shader == null) return;

        // If another session owns it, take over.
        boolean shouldReload = !effectLoaded
                || ownerSessionId != sessionId
                || activeShader == null
                || !activeShader.equals(shader);

        if (shouldReload) {
            ownerSessionId = sessionId;
            activeShader = shader;
            effectLoaded = true;
            mc.tell(() -> mc.gameRenderer.loadEffect(shader));
        }
    }

    public static void shutdownIfOwned(Minecraft mc, long sessionId) {
        if (mc == null) return;
        if (!effectLoaded) return;
        if (ownerSessionId != sessionId) return;

        effectLoaded = false;
        activeShader = null;
        ownerSessionId = 0L;

        mc.tell(() -> mc.gameRenderer.shutdownEffect());
    }

    public static void setUniformsIfOwned(Minecraft mc, long sessionId, String name, float v) {
        if (mc == null) return;
        if (!effectLoaded) return;
        if (ownerSessionId != sessionId) return;

        PostChain chain = getPostChain(mc.gameRenderer);
        if (chain == null) return;
        setUniformEveryPass(chain, name, v);
    }

    private static PostChain getPostChain(GameRenderer renderer) {
        try {
            Field f = GameRenderer.class.getDeclaredField("postEffect");
            f.setAccessible(true);
            return (PostChain) f.get(renderer);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void setUniformEveryPass(PostChain chain, String name, float v) {
        try {
            Field passesField = PostChain.class.getDeclaredField("passes");
            passesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<?> passes = (List<?>) passesField.get(chain);

            for (Object passObj : passes) {
                Field effectField = passObj.getClass().getDeclaredField("effect");
                effectField.setAccessible(true);
                Object effect = effectField.get(passObj); // EffectInstance

                Object uniform = effect.getClass().getMethod("getUniform", String.class).invoke(effect, name);
                if (uniform != null) {
                    uniform.getClass().getMethod("set", float.class).invoke(uniform, v);
                }
            }
        } catch (Throwable ignored) {
            // ignore missing uniforms and reflection issues silently
        }
    }
}
package com.diggydwarff.herbalistmod.client.render;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT)
public class FractalEffectRenderer {

    private static final ResourceLocation FRACTAL_SHADER =
            new ResourceLocation(HerbalistMod.MODID, "shaders/post/fractal.json");

    private static boolean activeLastTick = false;
    private static float t = 0f;
    private static float intensity = 0f;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!event.player.level().isClientSide) return;
        if (event.player != mc.player) return;
        if (event.phase != TickEvent.Phase.END) return;

        MobEffectInstance eff = mc.player.getEffect(ModEffects.FRACTAL.get());
        int dur = eff == null ? 0 : eff.getDuration();
        int amp = eff == null ? 0 : eff.getAmplifier();

        boolean shouldBeActive = dur > 1;

        float target = shouldBeActive ? 1.0f : 0.0f;
        float lerpSpeed = shouldBeActive ? 0.10f : 0.18f;
        intensity = Mth.lerp(lerpSpeed, intensity, target);

        if (intensity > 0.001f) t += 0.045f;

        if (shouldBeActive && !activeLastTick) {
            activeLastTick = true;
            mc.tell(() -> mc.gameRenderer.loadEffect(FRACTAL_SHADER));
        } else if (!shouldBeActive && activeLastTick && intensity < 0.01f) {
            activeLastTick = false;
            mc.tell(() -> mc.gameRenderer.shutdownEffect());
        }

        if (!activeLastTick) return;

        PostChain chain = getPostChain(mc.gameRenderer);
        if (chain == null) return;

        float ampBoost = 1.0f + (amp * 0.25f);

        setUniformEveryPass(chain, "Time", t);
        setUniformEveryPass(chain, "Intensity", intensity);

        // breathe the warp so it fades in/out
        float breathe = 0.55f + 0.45f * Mth.sin(t * 0.20f);
        float baseWarp = 0.0060f * intensity * ampBoost;
        setUniformEveryPass(chain, "WarpStrength", baseWarp * breathe);
        setUniformEveryPass(chain, "WarpScale", 2.1f);

        // color motion (fractal shader uses these)
        setUniformEveryPass(chain, "HueSpeed", 0.06f + 0.18f * intensity);
        setUniformEveryPass(chain, "SatBoost", 1.20f + 0.55f * intensity);

        // trails (keep playable)
        setUniformEveryPass(chain, "TrailStrength", 0.55f + 0.18f * intensity);
    }

    private static PostChain getPostChain(GameRenderer renderer) {
        try {
            Field f = GameRenderer.class.getDeclaredField("postEffect");
            f.setAccessible(true);
            return (PostChain) f.get(renderer);
        } catch (Throwable t) {
            t.printStackTrace();
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
                Object effect = effectField.get(passObj);

                Object uniform = effect.getClass().getMethod("getUniform", String.class).invoke(effect, name);
                if (uniform != null) {
                    uniform.getClass().getMethod("set", float.class).invoke(uniform, v);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
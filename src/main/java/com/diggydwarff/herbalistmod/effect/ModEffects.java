package com.diggydwarff.herbalistmod.effect;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.client.render.MirageEffectRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HerbalistMod.MODID);

    public static final RegistryObject<MobEffect> BLAZED = MOB_EFFECTS.register("blazed",
            () -> new BlazedEffect(MobEffectCategory.BENEFICIAL, 1934612));

    public static final RegistryObject<MobEffect> INTROSPECTION = MOB_EFFECTS.register("introspection",
            () -> new IntrospectionEffect(MobEffectCategory.BENEFICIAL, 1235642));

    public static final RegistryObject<MobEffect> INTROSPECTIONII = MOB_EFFECTS.register("introspectionii",
            () -> new IntrospectionIIEffect(MobEffectCategory.BENEFICIAL, 1235642));

    public static final RegistryObject<MobEffect> DESERT_VISION = MOB_EFFECTS.register("desert_vision",
            () -> new DesertVisionEffect(MobEffectCategory.NEUTRAL, 1235642));

    public static final RegistryObject<MobEffect> TURTLE_VISION = MOB_EFFECTS.register("turtle_vision",
            () -> new TurtleVisionEffect(MobEffectCategory.NEUTRAL, 1235642));

    public static final RegistryObject<MobEffect> AMPED = MOB_EFFECTS.register("amped",
            () -> new AmpedEffect(MobEffectCategory.NEUTRAL, 1235642));

    public static final RegistryObject<MobEffect> RIPPED = MOB_EFFECTS.register("ripped",
            () -> new RippedEffect(MobEffectCategory.NEUTRAL, 1235642));

    public static final RegistryObject<MobEffect> DELIRIUM = MOB_EFFECTS.register("delirium",
            () -> new DeliriumEffect(MobEffectCategory.HARMFUL, 1235642));

    public static final RegistryObject<MobEffect> DISASSOCIATED = MOB_EFFECTS.register("disassociated",
            () -> new DeliriumEffect(MobEffectCategory.HARMFUL, 1235642));

    public static final RegistryObject<MobEffect> ACID = MOB_EFFECTS.register("acid",
            () -> new AcidEffect(MobEffectCategory.NEUTRAL, 0x7BFF4A));

    public static final RegistryObject<MobEffect> FRACTAL = MOB_EFFECTS.register("fractal",
            () -> new FractalEffect(MobEffectCategory.NEUTRAL, 0x7BFF4A));

    public static final RegistryObject<MobEffect> MIRAGE = MOB_EFFECTS.register("mirage",
            () -> new MirageEffect(MobEffectCategory.NEUTRAL, 0x7BFF4A));

    public static final RegistryObject<MobEffect> REALITY_BEND = MOB_EFFECTS.register("reality_bend",
            () -> new RealityBendEffect(MobEffectCategory.NEUTRAL, 0xB84CFF));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

}

package com.diggydwarff.herbalistmod.items;

import com.diggydwarff.herbalistmod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.event.entity.living.MobEffectEvent;

public class ModFoods {

    public static final FoodProperties DREAMCAP_MUSHROOM = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 500), 0.7f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 1f)
            .build();

    public static final FoodProperties ETHEREAL_FUNGUS = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 100), 0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 5000), 1f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5000), 1f)
            .build();

    public static final FoodProperties ETHEREAL_FUNGUS_DUST = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 1f)
            .build();

    public static final FoodProperties CHORUS_DUST = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.RIPPED.get(), 5000), 1f)
            .build();

    public static final FoodProperties MIRAGE_DUST = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.DESERT_VISION.get(), 5000), 1f)
            .build();

    public static final FoodProperties SUSPICIOUS_DUST = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 5000), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 5000), 0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 5000), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5000), 0.6f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 5000), 0.8f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 0.1f)
            .build();

    /*
        MIRAGE CACTUS JUICE EFFECTS
            First confusion, delirium, and blindness hits, then that subsides into introspection, health boost, and glowingness
     */
    public static final FoodProperties MIRAGE_CACTUS_JUICE = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 500), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 750), 1f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 750), 1f)
            .effect(() -> new MobEffectInstance(ModEffects.DESERT_VISION.get(), 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 5000), 1f)
            .build();

    public static final FoodProperties BLAZEBUD_BROWNIE = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.BLAZED.get(), 5000), 1f)
            .build();

    public static final FoodProperties BLAZEBUD_COOKIE = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.BLAZED.get(), 5000), 1f)
            .build();

}

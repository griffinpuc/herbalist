package com.diggydwarff.herbalistmod.items;

import com.diggydwarff.herbalistmod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.event.entity.living.MobEffectEvent;

public class ModFoods {

    public static final FoodProperties DREAMCAP_MUSHROOM = new FoodProperties.Builder().nutrition(2).fast()
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 0.7f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 0.3f)
            .build();

    public static final FoodProperties ETHEREAL_FUNGUS = new FoodProperties.Builder().nutrition(2).fast()
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5000), 1f)
            .build();

    public static final FoodProperties ETHEREAL_FUNGUS_DUST = new FoodProperties.Builder().nutrition(0).fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 1f)
            .build();

    public static final FoodProperties CHORUS_DUST = new FoodProperties.Builder().nutrition(0).fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 1f)
            .build();

    public static final FoodProperties MIRAGE_DUST = new FoodProperties.Builder().nutrition(0).fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(ModEffects.DESERT_VISION.get(), 5000), 1f)
            .build();

    public static final FoodProperties SUSPICIOUS_DUST = new FoodProperties.Builder().nutrition(0).fast()
            .saturationMod(0f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 5000), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5000), 0.6f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 5000), 0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 0.1f)
            .build();

}

package com.diggydwarff.herbalistmod.items;

import com.diggydwarff.herbalistmod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.event.entity.living.MobEffectEvent;

public class ModFoods {

    public static final FoodProperties DREAMCAP_MUSHROOM_RAW_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 100), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 500), 0.75f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 2500), 1f)
            .build();

    public static final FoodProperties SNOWCAP_MUSHROOM_RAW_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 100), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 500), 0.75f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 2500), 1f)
            .build();

    public static final FoodProperties GOLDENGLOW_MUSHROOM_RAW_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 100), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 500), 0.75f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 2500), 1f)
            .build();

    public static final FoodProperties ETHEREAL_FUNGUS_RAW_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 100), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 500), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 500), 0.75f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 2500), 0.5f)
            .build();

    public static final FoodProperties MIRAGE_CACTUS_RAW_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 100), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 500), 1f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 2500), 0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.DESERT_VISION.get(), 2500), 0.1f)
            .build();


    public static final FoodProperties MAGIC_FUNGUS_DUST_FOOD = new FoodProperties.Builder().nutrition(0)
            .fast()
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTIONII.get(), 5000), 1f)
            .build();

    public static final FoodProperties CHORUS_DUST_FOOD = new FoodProperties.Builder().nutrition(0)
            .fast()
            .effect(() -> new MobEffectInstance(ModEffects.RIPPED.get(), 5000), 1f)
            .build();

    public static final FoodProperties MIRAGE_DUST_FOOD = new FoodProperties.Builder().nutrition(0)
            .fast()
            .effect(() -> new MobEffectInstance(ModEffects.DESERT_VISION.get(), 10000), 1f)
            .build();

    public static final FoodProperties TURTLE_SHELL_DUST_FOOD = new FoodProperties.Builder().nutrition(0)
            .fast()
            .effect(() -> new MobEffectInstance(ModEffects.TURTLE_VISION.get(), 10000), 1f)
            .build();

    public static final FoodProperties SUSPICIOUS_DUST_FOOD = new FoodProperties.Builder().nutrition(0)
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 5000), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 5000), 0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 5000), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5000), 0.6f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 5000), 0.8f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTION.get(), 5000), 0.2f)
            .effect(() -> new MobEffectInstance(ModEffects.INTROSPECTIONII.get(), 5000), 0.1f)
            .effect(() -> new MobEffectInstance(ModEffects.RIPPED.get(), 5000), 0.1f)
            .build();

    public static final FoodProperties MIRAGE_CACTUS_JUICE_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 500), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 750), 1f)
            .effect(() -> new MobEffectInstance(ModEffects.DELIRIUM.get(), 750), 1f)
            .effect(() -> new MobEffectInstance(ModEffects.DESERT_VISION.get(), 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 5000), 1f)
            .build();

    public static final FoodProperties CHORUS_JUICE_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(ModEffects.RIPPED.get(), 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 5000), 1f)
            .build();

    public static final FoodProperties AXOLOTL_VENOM_FOOD = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(ModEffects.AMPED.get(), 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 5000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 5000), 1f)
            .build();

    public static final FoodProperties BLAZEBUD_BROWNIE_FOOD = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(ModEffects.BLAZED.get(), 5000), 1f)
            .build();

    public static final FoodProperties BLAZEBUD_COOKIE_FOOD = new FoodProperties.Builder().nutrition(0)
            .fast()
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(ModEffects.BLAZED.get(), 2500), 1f)
            .build();

}

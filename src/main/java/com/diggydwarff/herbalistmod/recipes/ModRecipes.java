package com.diggydwarff.herbalistmod.recipes;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HerbalistMod.MODID);

    public static final RegistryObject<RecipeSerializer<BlazebudCigaretteRecipe>> BLAZEBUD_CIGARETTE_RECIPE_SERIALIZER =
            SERIALIZERS.register("crafting_special_blazebud_cigarette", () -> BlazebudCigaretteRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<BlazebudCigarRecipe>> BLAZEBUD_CIGAR_RECIPE_SERIALIZER =
            SERIALIZERS.register("crafting_special_blazebud_cigar", () -> BlazebudCigarRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<BlazebudMixedCigaretteRecipe>> BLAZEBUD_MIXED_CIGARETTE_RECIPE_SERIALIZER =
            SERIALIZERS.register("crafting_special_blazebud_mixed_cigarette", () -> BlazebudMixedCigaretteRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }


}

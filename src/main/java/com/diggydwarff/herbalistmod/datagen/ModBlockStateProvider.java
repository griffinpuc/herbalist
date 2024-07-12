package com.diggydwarff.herbalistmod.datagen;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.ModBlocks;
import com.diggydwarff.herbalistmod.block.custom.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HerbalistMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        makeTobaccoCrop(((CropBlock) ModBlocks.ENDERPEARL_HAZE_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "enderpearlkush");
        makeTobaccoCrop(((CropBlock) ModBlocks.REDSTONE_KUSH_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "redstonekush");
        makeTobaccoCrop(((CropBlock) ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "creepergreen");
        makeTobaccoCrop(((CropBlock) ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "emeralddream");
        makeTobaccoCrop(((CropBlock) ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "blockheadblue");
        makeTobaccoCrop(((CropBlock) ModBlocks.NETHERWART_KUSH_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "netherwartkush");

    }

    public void makeTobaccoCrop(CropBlock block, String modelName, String textureName, String type) {
        Function<BlockState, ConfiguredModel[]> function = state -> blazebudStates(state, block, modelName, textureName, type);
        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] blazebudStates(BlockState state, CropBlock block, String modelName, String textureName, String type) {
        ConfiguredModel[] models = new ConfiguredModel[1];

        switch (type) {
            case "enderpearlkush":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((EnderpearlHazeCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((EnderpearlHazeCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
            case "redstonekush":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RedstoneKushCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((RedstoneKushCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
            case "netherwartkush":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((NetherwartKushCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((NetherwartKushCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
            case "creepergreen":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CreeperGreenCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((CreeperGreenCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
            case "blockheadblue":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((BlockheadBlueCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((BlockheadBlueCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
            case "emeralddream":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((EmeraldDreamCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((EmeraldDreamCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
        }

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}

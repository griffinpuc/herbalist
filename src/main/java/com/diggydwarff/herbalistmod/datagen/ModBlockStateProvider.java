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
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HerbalistMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        makeTobaccoCrop(((CropBlock) ModBlocks.ENDERPEARL_ECHOS_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "enderpearlechos");
        makeTobaccoCrop(((CropBlock) ModBlocks.REDSTONE_CHARGE_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "redstoneechos");
        makeTobaccoCrop(((CropBlock) ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "creepergreen");
        makeTobaccoCrop(((CropBlock) ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "emeralddream");
        makeTobaccoCrop(((CropBlock) ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "blockheadblue");
        makeTobaccoCrop(((CropBlock) ModBlocks.NETHERWART_ECHOS_BLAZEBUD_CROP.get()), "blazebud", "blazebud", "netherwartechos");

        simpleBlockWithItem(ModBlocks.DIVINERS_SAGE_BLOCK.get(), models().cross(blockTexture(ModBlocks.DIVINERS_SAGE_BLOCK.get()).getPath(),
                blockTexture(ModBlocks.DIVINERS_SAGE_BLOCK.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.DREAMCAP_MUSHROOM_BLOCK.get(), models().cross(blockTexture(ModBlocks.DREAMCAP_MUSHROOM_BLOCK.get()).getPath(),
                blockTexture(ModBlocks.DREAMCAP_MUSHROOM_BLOCK.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.SNOWCAP_MUSHROOM_BLOCK.get(), models().cross(blockTexture(ModBlocks.SNOWCAP_MUSHROOM_BLOCK.get()).getPath(),
                blockTexture(ModBlocks.SNOWCAP_MUSHROOM_BLOCK.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.GOLDENGLOW_MUSHROOM_BLOCK.get(), models().cross(blockTexture(ModBlocks.GOLDENGLOW_MUSHROOM_BLOCK.get()).getPath(),
                blockTexture(ModBlocks.GOLDENGLOW_MUSHROOM_BLOCK.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.ETHEREAL_FUNGUS_BLOCK.get(), models().cross(blockTexture(ModBlocks.ETHEREAL_FUNGUS_BLOCK.get()).getPath(),
                blockTexture(ModBlocks.ETHEREAL_FUNGUS_BLOCK.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.EXTRACTION_STAND_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/extraction_stand")));

        simpleBlockWithItem(ModBlocks.MIRAGE_CACTUS_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/mirage_cactus_block")));


    }

    public void makeTobaccoCrop(CropBlock block, String modelName, String textureName, String type) {
        Function<BlockState, ConfiguredModel[]> function = state -> blazebudStates(state, block, modelName, textureName, type);
        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] blazebudStates(BlockState state, CropBlock block, String modelName, String textureName, String type) {
        ConfiguredModel[] models = new ConfiguredModel[1];

        switch (type) {
            case "enderpearlechos":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((EnderpearlEchosCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((EnderpearlEchosCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
            case "redstoneechos":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RedstoneChargeCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((RedstoneChargeCropBlock) block).getAgeProperty()))).renderType("cutout"));
                break;
            case "netherwartechos":
                models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((NetherwartEchosCropBlock) block).getAgeProperty()),
                        new ResourceLocation(HerbalistMod.MODID, "block/" + textureName + state.getValue(((NetherwartEchosCropBlock) block).getAgeProperty()))).renderType("cutout"));
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

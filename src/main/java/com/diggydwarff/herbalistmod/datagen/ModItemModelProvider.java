package com.diggydwarff.herbalistmod.datagen;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.ModBlocks;
import com.diggydwarff.herbalistmod.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HerbalistMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlockItemBlockTexture(ModBlocks.DIVINERS_SAGE_BLOCK);
        simpleBlockItemBlockTexture(ModBlocks.DREAMCAP_MUSHROOM_BLOCK);
        simpleBlockItemBlockTexture(ModBlocks.ETHEREAL_FUNGUS_BLOCK);
        simpleBlockItemBlockTexture(ModBlocks.SNOWCAP_MUSHROOM_BLOCK);
        simpleBlockItemBlockTexture(ModBlocks.GOLDENGLOW_MUSHROOM_BLOCK);

        simpleItem(ModItems.CHROUS_FRUIT_JUICE);
        simpleItem(ModItems.DREAMCAP_MUSHROOM);
        simpleItem(ModItems.GOLDENGLOW_MUSHROOM);
        simpleItem(ModItems.SNOWCAP_MUSHROOM);
        simpleItem(ModItems.ETHEREAL_FUNGUS);
        simpleItem(ModItems.BLAZEBUD_BROWNIE);
        simpleItem(ModItems.BLAZEBUD_COOKIE);
        simpleItem(ModItems.DIVINERS_SAGE);
        simpleItem(ModItems.AXOLOTL_VENOM);
        simpleItem(ModItems.GLASS_VIAL);
        simpleItem(ModItems.TURTLE_SHELL_DUST);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HerbalistMod.MODID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HerbalistMod.MODID,"block/" + item.getId().getPath()));
    }

}

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
        simpleBlockItemBlockTexture(ModBlocks.EXTRACTION_STAND_BLOCK);

        simpleItem(ModItems.MUSIC_DISC_TROPIC_HERB);
        simpleItem(ModItems.MUSIC_DISC_PRISM);

        simpleItem(ModItems.BLAZEBUD_CIGAR);
        simpleItem(ModItems.BLAZEBUD_CIGARETTE);
        simpleItem(ModItems.DIVINERS_SAGE_CIGARETTE);
        simpleItem(ModItems.BLAZEBUD_MIXED_CIGARETTE);
        simpleItem(ModItems.BLAZEBUD_BROWNIE);
        simpleItem(ModItems.BLAZEBUD_COOKIE);
        simpleItem(ModItems.ENDERPEARL_ECHOS_BLAZEBUD);
        simpleItem(ModItems.REDSTONE_CHARGE_BLAZEBUD);
        simpleItem(ModItems.CREEPER_GREEN_BLAZEBUD);
        simpleItem(ModItems.EMERALD_DREAM_BLAZEBUD);
        simpleItem(ModItems.BLOCKHEAD_BLUE_BLAZEBUD);
        simpleItem(ModItems.NETHERWART_ECHOS_BLAZEBUD);
        simpleItem(ModItems.ENDERPEARL_ECHOS_BLAZEBUD_STALK);
        simpleItem(ModItems.REDSTONE_CHARGE_BLAZEBUD_STALK);
        simpleItem(ModItems.CREEPER_GREEN_BLAZEBUD_STALK);
        simpleItem(ModItems.EMERALD_DREAM_BLAZEBUD_STALK);
        simpleItem(ModItems.BLOCKHEAD_BLUE_BLAZEBUD_STALK);
        simpleItem(ModItems.NETHERWART_ECHOS_BLAZEBUD_STALK);
        simpleItem(ModItems.ENDERPEARL_ECHOS_SEEDS);
        simpleItem(ModItems.REDSTONE_CHARGE_SEEDS);
        simpleItem(ModItems.CREEPER_GREEN_SEEDS);
        simpleItem(ModItems.EMERALD_DREAM_SEEDS);
        simpleItem(ModItems.BLOCKHEAD_BLUE_SEEDS);
        simpleItem(ModItems.NETHERWART_ECHOS_SEEDS);

        simpleItem(ModItems.DREAMCAP_MUSHROOM);
        simpleItem(ModItems.GOLDENGLOW_MUSHROOM);
        simpleItem(ModItems.SNOWCAP_MUSHROOM);
        simpleItem(ModItems.ETHEREAL_FUNGUS);
        simpleItem(ModItems.DIVINERS_SAGE);
        simpleItem(ModItems.MIRAGE_CACTUS);

        simpleItem(ModItems.FROG_VENOM);
        simpleItem(ModItems.AXOLOTL_VENOM);
        simpleItem(ModItems.CHORUS_EXTRACT);
        simpleItem(ModItems.MAGIC_FUNGUS_EXTRACT);
        simpleItem(ModItems.SUSPICIOUS_EXTRACT);
        simpleItem(ModItems.MIRAGE_CACTUS_EXTRACT);

        simpleItem(ModItems.FROG_VENOM_DUST);
        simpleItem(ModItems.AXOLOTL_VENOM_DUST);
        simpleItem(ModItems.CHORUS_DUST);
        simpleItem(ModItems.MAGIC_FUNGUS_DUST);
        simpleItem(ModItems.MIRAGE_DUST);
        simpleItem(ModItems.SUSPICIOUS_DUST);
        simpleItem(ModItems.TURTLE_SHELL_DUST);

        simpleItem(ModItems.CHROUS_FRUIT_JUICE);
        simpleItem(ModItems.MIRAGE_CACTUS_JUICE);
        simpleItem(ModItems.GLASS_VIAL);

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

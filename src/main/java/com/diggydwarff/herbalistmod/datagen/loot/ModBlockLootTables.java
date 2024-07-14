package com.diggydwarff.herbalistmod.datagen.loot;

import com.diggydwarff.herbalistmod.block.ModBlocks;
import com.diggydwarff.herbalistmod.block.custom.*;
import com.diggydwarff.herbalistmod.items.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(ModBlocks.ENDERPEARL_ECHOS_BLAZEBUD_CRATE.get());
        this.dropSelf(ModBlocks.REDSTONE_CHARGE_BLAZEBUD_CRATE.get());
        this.dropSelf(ModBlocks.CREEPER_GREEN_BLAZEBUD_CRATE.get());
        this.dropSelf(ModBlocks.EMERALD_DREAM_BLAZEBUD_CRATE.get());
        this.dropSelf(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CRATE.get());
        this.dropSelf(ModBlocks.NETHERWART_ECHOS_BLAZEBUD_CRATE.get());

        this.add(ModBlocks.MIRAGE_CACTUS_BLOCK.get(), createOreDrop(ModBlocks.MIRAGE_CACTUS_BLOCK.get(), ModItems.MIRAGE_CACTUS.get()));

        this.add(ModBlocks.DIVINERS_SAGE_BLOCK.get(), createOreDrop(ModBlocks.DIVINERS_SAGE_BLOCK.get(), ModItems.DIVINERS_SAGE.get()));

        this.add(ModBlocks.DREAMCAP_MUSHROOM_BLOCK.get(), createOreDrop(ModBlocks.DREAMCAP_MUSHROOM_BLOCK.get(), ModItems.DREAMCAP_MUSHROOM.get()));

        this.add(ModBlocks.SNOWCAP_MUSHROOM_BLOCK.get(), createOreDrop(ModBlocks.SNOWCAP_MUSHROOM_BLOCK.get(), ModItems.SNOWCAP_MUSHROOM.get()));

        this.add(ModBlocks.GOLDENGLOW_MUSHROOM_BLOCK.get(), createOreDrop(ModBlocks.GOLDENGLOW_MUSHROOM_BLOCK.get(), ModItems.GOLDENGLOW_MUSHROOM.get()));

        this.add(ModBlocks.ETHEREAL_FUNGUS_BLOCK.get(), createOreDrop(ModBlocks.ETHEREAL_FUNGUS_BLOCK.get(), ModItems.ETHEREAL_FUNGUS.get()));

        LootItemCondition.Builder enderpearlBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks
                        .ENDERPEARL_ECHOS_BLAZEBUD_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(EnderpearlEchosCropBlock.AGE, 3));

        LootItemCondition.Builder redstoneBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.REDSTONE_CHARGE_BLAZEBUD_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RedstoneChargeCropBlock.AGE, 3));

        LootItemCondition.Builder creeperBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CreeperGreenCropBlock.AGE, 3));

        LootItemCondition.Builder emeraldBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockheadBlueCropBlock.AGE, 3));

        LootItemCondition.Builder blockheadBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(EmeraldDreamCropBlock.AGE, 3));

        LootItemCondition.Builder netherwartBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.NETHERWART_ECHOS_BLAZEBUD_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(NetherwartEchosCropBlock.AGE, 3));

        this.add(ModBlocks.ENDERPEARL_ECHOS_BLAZEBUD_CROP.get(), createCropDrops(ModBlocks.ENDERPEARL_ECHOS_BLAZEBUD_CROP.get(), ModItems.ENDERPEARL_ECHOS_BLAZEBUD_STALK.get(),
                ModItems.ENDERPEARL_ECHOS_SEEDS.get(), enderpearlBuilder));

        this.add(ModBlocks.REDSTONE_CHARGE_BLAZEBUD_CROP.get(), createCropDrops(ModBlocks.REDSTONE_CHARGE_BLAZEBUD_CROP.get(), ModItems.REDSTONE_CHARGE_BLAZEBUD_STALK.get(),
                ModItems.REDSTONE_CHARGE_SEEDS.get(), redstoneBuilder));

        this.add(ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get(), createCropDrops(ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get(), ModItems.CREEPER_GREEN_BLAZEBUD_STALK.get(),
                ModItems.CREEPER_GREEN_SEEDS.get(), creeperBuilder));

        this.add(ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get(), createCropDrops(ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get(), ModItems.EMERALD_DREAM_BLAZEBUD_STALK.get(),
                ModItems.EMERALD_DREAM_SEEDS.get(), emeraldBuilder));

        this.add(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get(), createCropDrops(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get(), ModItems.BLOCKHEAD_BLUE_BLAZEBUD_STALK.get(),
                ModItems.BLOCKHEAD_BLUE_SEEDS.get(), blockheadBuilder));

        this.add(ModBlocks.NETHERWART_ECHOS_BLAZEBUD_CROP.get(), createCropDrops(ModBlocks.NETHERWART_ECHOS_BLAZEBUD_CROP.get(), ModItems.NETHERWART_ECHOS_BLAZEBUD_STALK.get(),
                ModItems.NETHERWART_ECHOS_SEEDS.get(), netherwartBuilder));

    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}

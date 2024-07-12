package com.diggydwarff.herbalistmod.block;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.custom.*;
import com.diggydwarff.herbalistmod.items.ModItems;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HerbalistMod.MODID);

    public static final RegistryObject<Block> ENDERPEARL_HAZE_BLAZEBUD_CROP = BLOCKS.register("enderpearl_haze_blazebud_crop",
            () -> new EnderpearlHazeCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> REDSTONE_KUSH_BLAZEBUD_CROP = BLOCKS.register("redstone_kush_blazebud_crop",
            () -> new RedstoneKushCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> CREEPER_GREEN_BLAZEBUD_CROP = BLOCKS.register("creeper_green_blazebud_crop",
            () -> new CreeperGreenCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> EMERALD_DREAM_BLAZEBUD_CROP = BLOCKS.register("emerald_dream_blazebud_crop",
            () -> new EmeraldDreamCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> BLOCKHEAD_BLUE_BLAZEBUD_CROP = BLOCKS.register("blockhead_blue_blazebud_crop",
            () -> new BlockheadBlueCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> NETHERWART_KUSH_BLAZEBUD_CROP = BLOCKS.register("netherwart_kush_blazebud_crop",
            () -> new NetherwartKushCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));



    public static final RegistryObject<Block> DREAMCAP_MUSHROOM = BLOCKS.register("dreamcap_mushroom",
            () -> new MushroomBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS), TreeFeatures.HUGE_RED_MUSHROOM));

    public static final RegistryObject<Block> ETHEREAL_FUNGUS = BLOCKS.register("ethereal_fungus",
            () -> new MushroomBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS), TreeFeatures.HUGE_RED_MUSHROOM));


    public static final RegistryObject<Block> EXTRACTION_STATION_BLOCK = registerBlock("extraction_station_block",
            () -> new ExtractionStationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(6f).requiresCorrectToolForDrops().noOcclusion().noLootTable()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
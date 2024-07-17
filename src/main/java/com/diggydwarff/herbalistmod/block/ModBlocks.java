package com.diggydwarff.herbalistmod.block;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.custom.*;
import com.diggydwarff.herbalistmod.items.ModItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HerbalistMod.MODID);

    public static final RegistryObject<Block> ENDERPEARL_ECHOS_BLAZEBUD_CROP = BLOCKS.register("enderpearl_echos_blazebud_crop",
            () -> new EnderpearlEchosCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> REDSTONE_CHARGE_BLAZEBUD_CROP = BLOCKS.register("redstone_charge_blazebud_crop",
            () -> new RedstoneChargeCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> CREEPER_GREEN_BLAZEBUD_CROP = BLOCKS.register("creeper_green_blazebud_crop",
            () -> new CreeperGreenCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> EMERALD_DREAM_BLAZEBUD_CROP = BLOCKS.register("emerald_dream_blazebud_crop",
            () -> new EmeraldDreamCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> BLOCKHEAD_BLUE_BLAZEBUD_CROP = BLOCKS.register("blockhead_blue_blazebud_crop",
            () -> new BlockheadBlueCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> NETHERWART_ECHOS_BLAZEBUD_CROP = BLOCKS.register("netherwart_echos_blazebud_crop",
            () -> new NetherwartEchosCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));


    public static final RegistryObject<Block> ENDERPEARL_ECHOS_BLAZEBUD_CRATE = registerBlock("enderpearl_echos_blazebud_crate",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> REDSTONE_CHARGE_BLAZEBUD_CRATE = registerBlock("redstone_charge_blazebud_crate",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> EMERALD_DREAM_BLAZEBUD_CRATE = registerBlock("emerald_dream_blazebud_crate",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> BLOCKHEAD_BLUE_BLAZEBUD_CRATE = registerBlock("blockhead_blue_blazebud_crate",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> NETHERWART_ECHOS_BLAZEBUD_CRATE = registerBlock("netherwart_echos_blazebud_crate",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> CREEPER_GREEN_BLAZEBUD_CRATE = registerBlock("creeper_green_blazebud_crate",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));



    public static final RegistryObject<Block> DREAMCAP_MUSHROOM_BLOCK = registerBlock("dreamcap_mushroom_block",
            () -> new DreamcapMushroomBlock(() -> MobEffects.CONFUSION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission()));

    public static final RegistryObject<Block> SNOWCAP_MUSHROOM_BLOCK = registerBlock("snowcap_mushroom_block",
            () -> new SnowcapMushroomBlock(() -> MobEffects.CONFUSION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission()));

    public static final RegistryObject<Block> GOLDENGLOW_MUSHROOM_BLOCK = registerBlock("goldenglow_mushroom_block",
            () -> new GoldenglowMushroomBlock(() -> MobEffects.CONFUSION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission()));

    public static final RegistryObject<Block> ETHEREAL_FUNGUS_BLOCK = registerBlock("ethereal_fungus_block",
            () -> new EtherealFungusBlock(() -> MobEffects.CONFUSION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission()));

    public static final RegistryObject<Block> DIVINERS_SAGE_BLOCK = registerBlock("diviners_sage_block",
            () -> new FlowerBlock(() -> MobEffects.CONFUSION, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission()));


    public static final RegistryObject<Block> EXTRACTION_STAND_BLOCK = registerBlock("extraction_stand_block",
            () -> new ExtractionStandBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.5f).noOcclusion().noLootTable()));

    public static final RegistryObject<Block> MIRAGE_CACTUS_BLOCK = registerBlock("mirage_cactus_block",
            () -> new CactusBlock(BlockBehaviour.Properties.copy(Blocks.CACTUS)));


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
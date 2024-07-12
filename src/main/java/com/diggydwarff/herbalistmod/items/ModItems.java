package com.diggydwarff.herbalistmod.items;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.ModBlocks;
import com.diggydwarff.herbalistmod.items.custom.*;
import com.diggydwarff.herbalistmod.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HerbalistMod.MODID);

    public static final RegistryObject<Item> MUSIC_DISC_TROPIC_HERB = ITEMS.register("music_disc_tropic_herb",
            () -> new RecordItem(6, ModSounds.TROPIC_HERB, new Item.Properties().stacksTo(1), 3920));

    public static final RegistryObject<Item> ROLLING_PAPER = ITEMS.register("rolling_paper", () -> new RollingPaperItem(new Item.Properties()));

    public static final RegistryObject<Item> BLUNT = ITEMS.register("blunt",
            () -> new BluntItem(new Item.Properties().durability(100)));

    public static final RegistryObject<Item> BLAZEBUD_CIGARETTE = ITEMS.register("blazebud_cigarette",
            () -> new BlazebudCigaretteItem(new Item.Properties().durability(15)));

    public static final RegistryObject<Item> SPLIFF = ITEMS.register("spliff",
            () -> new BlazebudCigaretteItem(new Item.Properties().durability(15)));

    public static final RegistryObject<Item> BLAZEBUD_BROWNIE = ITEMS.register("blazebud_brownie", () -> new BlazebudBrownieItem(new Item.Properties()));
    public static final RegistryObject<Item> BLAZEBUD_COOKIE = ITEMS.register("blazebud_cookie", () -> new BlazebudCookieItem(new Item.Properties()));

    public static final RegistryObject<Item> ENDERPEARL_HAZE_BLAZEBUD = ITEMS.register("enderpearl_haze_blazebud", () -> new BlazebudItem(new Item.Properties(), "enderpearl_haze"));
    public static final RegistryObject<Item> REDSTONE_KUSH_BLAZEBUD = ITEMS.register("redstone_kush_blazebud", () -> new BlazebudItem(new Item.Properties(), "redstone_kush"));
    public static final RegistryObject<Item> CREEPER_GREEN_BLAZEBUD = ITEMS.register("creeper_green_blazebud", () -> new BlazebudItem(new Item.Properties(), "creeper_green"));
    public static final RegistryObject<Item> EMERALD_DREAM_BLAZEBUD = ITEMS.register("emerald_dream_blazebud", () -> new BlazebudItem(new Item.Properties(), "emerald_dream"));
    public static final RegistryObject<Item> BLOCKHEAD_BLUE_BLAZEBUD = ITEMS.register("blockhead_blue_blazebud", () -> new BlazebudItem(new Item.Properties(), "blockhead_blue"));
    public static final RegistryObject<Item> NETHERWART_KUSH_BLAZEBUD = ITEMS.register("netherwart_kush_blazebud", () -> new BlazebudItem(new Item.Properties(), "netherwart_kush"));

    public static final RegistryObject<Item> ENDERPEARL_HAZE_BLAZEBUD_STALK = ITEMS.register("enderpearl_haze_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "enderpearl_haze"));
    public static final RegistryObject<Item> REDSTONE_KUSH_BLAZEBUD_STALK  = ITEMS.register("redstone_kush_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "redstone_kush"));
    public static final RegistryObject<Item> CREEPER_GREEN_BLAZEBUD_STALK  = ITEMS.register("creeper_green_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "creeper_green"));
    public static final RegistryObject<Item> EMERALD_DREAM_BLAZEBUD_STALK  = ITEMS.register("emerald_dream_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "emerald_dream"));
    public static final RegistryObject<Item> BLOCKHEAD_BLUE_BLAZEBUD_STALK  = ITEMS.register("blockhead_blue_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "blockhead_blue"));
    public static final RegistryObject<Item> NETHERWART_KUSH_BLAZEBUD_STALK  = ITEMS.register("netherwart_kush_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "netherwart_kush"));


    public static final RegistryObject<Item> ENDERPEARL_HAZE_SEEDS = ITEMS.register("enderpearl_haze_seeds",
            () -> new ItemNameBlockItem(ModBlocks.ENDERPEARL_HAZE_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> REDSTONE_KUSH_SEEDS = ITEMS.register("redstone_kush_seeds",
            () -> new ItemNameBlockItem(ModBlocks.REDSTONE_KUSH_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> CREEPER_GREEN_SEEDS = ITEMS.register("creeper_green_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_DREAM_SEEDS = ITEMS.register("emerald_dream_seeds",
            () -> new ItemNameBlockItem(ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> BLOCKHEAD_BLUE_SEEDS = ITEMS.register("blockhead_blue_seeds",
            () -> new ItemNameBlockItem(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> NETHERWART_KUSH_SEEDS = ITEMS.register("netherwart_kush_seeds",
            () -> new ItemNameBlockItem(ModBlocks.NETHERWART_KUSH_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> DREAMCAP_MUSHROOM = ITEMS.register("dreamcap_mushroom", () -> new Item(new Item.Properties().food(ModFoods.DREAMCAP_MUSHROOM)));
    public static final RegistryObject<Item> ETHEREAL_FUNGUS = ITEMS.register("ethereal_fungus", () -> new Item(new Item.Properties().food(ModFoods.ETHEREAL_FUNGUS)));

    public static final RegistryObject<Item> MIRAGE_CACTUS = ITEMS.register("mirage_cactus", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ETHEREAL_EXTRACT = ITEMS.register("ethereal_extract", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHORUS_EXTRACT = ITEMS.register("chorus_extract", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MIRAGE_CACTUS_EXTRACT = ITEMS.register("mirage_cactus_extract", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SUSPICIOUS_POTATO_EXTRACT = ITEMS.register("suspicious_extract", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ETHEREAL_DUST = ITEMS.register("ethereal_dust", () -> new Item(new Item.Properties().food(ModFoods.ETHEREAL_FUNGUS_DUST)));

    public static final RegistryObject<Item> CHORUS_DUST = ITEMS.register("chorus_dust", () -> new Item(new Item.Properties().food(ModFoods.CHORUS_DUST)));

    public static final RegistryObject<Item> MIRAGE_DUST = ITEMS.register("mirage_dust", () -> new Item(new Item.Properties().food(ModFoods.MIRAGE_DUST)));

    public static final RegistryObject<Item> SUSPICIOUS_DUST = ITEMS.register("suspicious_dust", () -> new Item(new Item.Properties().food(ModFoods.SUSPICIOUS_DUST)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
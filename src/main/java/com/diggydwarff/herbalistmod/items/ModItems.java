package com.diggydwarff.herbalistmod.items;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.ModBlocks;
import com.diggydwarff.herbalistmod.items.custom.*;
import com.diggydwarff.herbalistmod.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HerbalistMod.MODID);

    public static final RegistryObject<Item> MUSIC_DISC_TROPIC_HERB = ITEMS.register("music_disc_tropic_herb",
            () -> new RecordItem(6, ModSounds.TROPIC_HERB, new Item.Properties().stacksTo(1), 3920));

    public static final RegistryObject<Item> MUSIC_DISC_PRISM = ITEMS.register("music_disc_prism",
            () -> new RecordItem(6, ModSounds.PRISM, new Item.Properties().stacksTo(1), 3920));

    //public static final RegistryObject<Item> ROLLING_PAPER = ITEMS.register("rolling_paper", () -> new RollingPaperItem(new Item.Properties()));

    public static final RegistryObject<Item> BLAZEBUD_CIGAR = ITEMS.register("blazebud_cigar",
            () -> new BlazebudCigarItem(new Item.Properties().durability(100)));

    public static final RegistryObject<Item> BLAZEBUD_CIGARETTE = ITEMS.register("blazebud_cigarette",
            () -> new BlazebudCigaretteItem(new Item.Properties().durability(15)));

    public static final RegistryObject<Item> DIVINERS_SAGE_CIGARETTE = ITEMS.register("diviners_sage_cigarette",
            () -> new DivinersSageCigaretteItem(new Item.Properties().durability(15)));

    public static final RegistryObject<Item> BLAZEBUD_MIXED_CIGARETTE = ITEMS.register("blazebud_mixed_cigarette",
            () -> new BlazebudMixedCigaretteItem(new Item.Properties().durability(15)));

    public static final RegistryObject<Item> BLAZEBUD_BROWNIE = ITEMS.register("blazebud_brownie", () -> new BlazebudBrownieItem(new Item.Properties().food(ModFoods.BLAZEBUD_BROWNIE_FOOD)));
    public static final RegistryObject<Item> BLAZEBUD_COOKIE = ITEMS.register("blazebud_cookie", () -> new BlazebudCookieItem(new Item.Properties().food(ModFoods.BLAZEBUD_COOKIE_FOOD)));

    public static final RegistryObject<Item> ENDERPEARL_ECHOS_BLAZEBUD = ITEMS.register("enderpearl_echos_blazebud", () -> new BlazebudItem(new Item.Properties(), "enderpearl_echos"));
    public static final RegistryObject<Item> REDSTONE_CHARGE_BLAZEBUD = ITEMS.register("redstone_charge_blazebud", () -> new BlazebudItem(new Item.Properties(), "redstone_charge"));
    public static final RegistryObject<Item> CREEPER_GREEN_BLAZEBUD = ITEMS.register("creeper_green_blazebud", () -> new BlazebudItem(new Item.Properties(), "creeper_green"));
    public static final RegistryObject<Item> EMERALD_DREAM_BLAZEBUD = ITEMS.register("emerald_dream_blazebud", () -> new BlazebudItem(new Item.Properties(), "emerald_dream"));
    public static final RegistryObject<Item> BLOCKHEAD_BLUE_BLAZEBUD = ITEMS.register("blockhead_blue_blazebud", () -> new BlazebudItem(new Item.Properties(), "blockhead_blue"));
    public static final RegistryObject<Item> NETHERWART_ECHOS_BLAZEBUD = ITEMS.register("netherwart_echos_blazebud", () -> new BlazebudItem(new Item.Properties(), "netherwart_echos"));

    public static final RegistryObject<Item> ENDERPEARL_ECHOS_BLAZEBUD_STALK = ITEMS.register("enderpearl_echos_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "enderpearl_echos"));
    public static final RegistryObject<Item> REDSTONE_CHARGE_BLAZEBUD_STALK  = ITEMS.register("redstone_charge_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "redstone_charge"));
    public static final RegistryObject<Item> CREEPER_GREEN_BLAZEBUD_STALK  = ITEMS.register("creeper_green_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "creeper_green"));
    public static final RegistryObject<Item> EMERALD_DREAM_BLAZEBUD_STALK  = ITEMS.register("emerald_dream_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "emerald_dream"));
    public static final RegistryObject<Item> BLOCKHEAD_BLUE_BLAZEBUD_STALK  = ITEMS.register("blockhead_blue_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "blockhead_blue"));
    public static final RegistryObject<Item> NETHERWART_ECHOS_BLAZEBUD_STALK  = ITEMS.register("netherwart_echos_blazebud_stalk", () -> new BlazebudItem(new Item.Properties(), "netherwart_echos"));


    public static final RegistryObject<Item> ENDERPEARL_ECHOS_SEEDS = ITEMS.register("enderpearl_echos_seeds",
            () -> new ItemNameBlockItem(ModBlocks.ENDERPEARL_ECHOS_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> REDSTONE_CHARGE_SEEDS = ITEMS.register("redstone_charge_seeds",
            () -> new ItemNameBlockItem(ModBlocks.REDSTONE_CHARGE_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> CREEPER_GREEN_SEEDS = ITEMS.register("creeper_green_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CREEPER_GREEN_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_DREAM_SEEDS = ITEMS.register("emerald_dream_seeds",
            () -> new ItemNameBlockItem(ModBlocks.EMERALD_DREAM_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> BLOCKHEAD_BLUE_SEEDS = ITEMS.register("blockhead_blue_seeds",
            () -> new ItemNameBlockItem(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> NETHERWART_ECHOS_SEEDS = ITEMS.register("netherwart_echos_seeds",
            () -> new ItemNameBlockItem(ModBlocks.NETHERWART_ECHOS_BLAZEBUD_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> DREAMCAP_MUSHROOM = ITEMS.register("dreamcap_mushroom", () -> new Item(new Item.Properties().food(ModFoods.DREAMCAP_MUSHROOM_RAW_FOOD)));
    public static final RegistryObject<Item> GOLDENGLOW_MUSHROOM = ITEMS.register("goldenglow_mushroom", () -> new Item(new Item.Properties().food(ModFoods.GOLDENGLOW_MUSHROOM_RAW_FOOD)));
    public static final RegistryObject<Item> SNOWCAP_MUSHROOM = ITEMS.register("snowcap_mushroom", () -> new Item(new Item.Properties().food(ModFoods.SNOWCAP_MUSHROOM_RAW_FOOD)));
    public static final RegistryObject<Item> ETHEREAL_FUNGUS = ITEMS.register("ethereal_fungus", () -> new Item(new Item.Properties().food(ModFoods.ETHEREAL_FUNGUS_RAW_FOOD)));

    public static final RegistryObject<Item> MIRAGE_CACTUS = ITEMS.register("mirage_cactus", () -> new Item(new Item.Properties().food(ModFoods.MIRAGE_CACTUS_RAW_FOOD)));
    public static final RegistryObject<Item> DIVINERS_SAGE = ITEMS.register("diviners_sage", () -> new Item(new Item.Properties().food(ModFoods.MIRAGE_CACTUS_JUICE_FOOD)));
    public static final RegistryObject<Item> MAGIC_FUNGUS_EXTRACT = ITEMS.register("magic_fungus_extract", () -> new Item(new Item.Properties().food(ModFoods.MAGIC_FUNGUS_DUST_FOOD).stacksTo(1)));
    public static final RegistryObject<Item> CHORUS_EXTRACT = ITEMS.register("chorus_extract", () -> new Item(new Item.Properties().food(ModFoods.CHORUS_DUST_FOOD)));

    public static final RegistryObject<Item> MIRAGE_CACTUS_EXTRACT = ITEMS.register("mirage_cactus_extract", () -> new Item(new Item.Properties().food(ModFoods.MIRAGE_DUST_FOOD).stacksTo(1)));

    public static final RegistryObject<Item> MIRAGE_CACTUS_JUICE = ITEMS.register("mirage_cactus_juice", () -> new Item(new Item.Properties().food(ModFoods.MIRAGE_CACTUS_JUICE_FOOD).stacksTo(1)));
    public static final RegistryObject<Item> CHROUS_FRUIT_JUICE = ITEMS.register("chorus_fruit_juice", () -> new Item(new Item.Properties().food(ModFoods.CHORUS_JUICE_FOOD).stacksTo(1)));

    public static final RegistryObject<Item> SUSPICIOUS_EXTRACT = ITEMS.register("suspicious_extract", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> GLASS_VIAL = ITEMS.register("glass_vial", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> AXOLOTL_VENOM = ITEMS.register("axolotl_venom", () -> new Item(new Item.Properties().food(ModFoods.AXOLOTL_VENOM_FOOD).stacksTo(1)));

    public static final RegistryObject<Item> FROG_VENOM = ITEMS.register("frog_venom", () -> new Item(new Item.Properties().food(ModFoods.AXOLOTL_VENOM_FOOD).stacksTo(1)));

    public static final RegistryObject<Item> MAGIC_FUNGUS_DUST = ITEMS.register("magic_fungus_dust", () -> new Item(new Item.Properties().food(ModFoods.MAGIC_FUNGUS_DUST_FOOD)));


    public static final RegistryObject<Item> AXOLOTL_VENOM_DUST = ITEMS.register("axolotl_venom_dust", () -> new Item(new Item.Properties().food(ModFoods.MAGIC_FUNGUS_DUST_FOOD)));
    public static final RegistryObject<Item> FROG_VENOM_DUST = ITEMS.register("frog_venom_dust", () -> new Item(new Item.Properties().food(ModFoods.MAGIC_FUNGUS_DUST_FOOD)));

    public static final RegistryObject<Item> CHORUS_DUST = ITEMS.register("chorus_dust", () -> new Item(new Item.Properties().food(ModFoods.CHORUS_DUST_FOOD)));

    public static final RegistryObject<Item> MIRAGE_DUST = ITEMS.register("mirage_dust", () -> new Item(new Item.Properties().food(ModFoods.MIRAGE_DUST_FOOD)));

    public static final RegistryObject<Item> SUSPICIOUS_DUST = ITEMS.register("suspicious_dust", () -> new Item(new Item.Properties().food(ModFoods.SUSPICIOUS_DUST_FOOD)));
    public static final RegistryObject<Item> TURTLE_SHELL_DUST = ITEMS.register("turtle_shell_dust", () -> new Item(new Item.Properties().food(ModFoods.TURTLE_SHELL_DUST_FOOD)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
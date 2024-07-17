package com.diggydwarff.herbalistmod;

import com.diggydwarff.herbalistmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.diggydwarff.herbalistmod.items.ModItems.*;
public class HerbalistCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HerbalistMod.MODID);

    public static final RegistryObject<CreativeModeTab> COURSE_TAB = CREATIVE_MODE_TABS.register("herbalistmod",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(DREAMCAP_MUSHROOM.get()))
                    .title(Component.translatable("creativetab.herbalistmod"))
                    .displayItems((displayParameters, output) -> {

                        output.accept(ENDERPEARL_ECHOS_BLAZEBUD.get());
                        output.accept(REDSTONE_CHARGE_BLAZEBUD.get());
                        output.accept(CREEPER_GREEN_BLAZEBUD.get());
                        output.accept(EMERALD_DREAM_BLAZEBUD.get());
                        output.accept(BLOCKHEAD_BLUE_BLAZEBUD.get());
                        output.accept(NETHERWART_ECHOS_BLAZEBUD.get());

                        output.accept(ENDERPEARL_ECHOS_BLAZEBUD_STALK.get());
                        output.accept(REDSTONE_CHARGE_BLAZEBUD_STALK.get());
                        output.accept(CREEPER_GREEN_BLAZEBUD_STALK.get());
                        output.accept(EMERALD_DREAM_BLAZEBUD_STALK.get());
                        output.accept(BLOCKHEAD_BLUE_BLAZEBUD_STALK.get());
                        output.accept(NETHERWART_ECHOS_BLAZEBUD_STALK.get());

                        output.accept(ModBlocks.ENDERPEARL_ECHOS_BLAZEBUD_CRATE.get());
                        output.accept(ModBlocks.REDSTONE_CHARGE_BLAZEBUD_CRATE.get());
                        output.accept(ModBlocks.CREEPER_GREEN_BLAZEBUD_CRATE.get());
                        output.accept(ModBlocks.EMERALD_DREAM_BLAZEBUD_CRATE.get());
                        output.accept(ModBlocks.BLOCKHEAD_BLUE_BLAZEBUD_CRATE.get());
                        output.accept(ModBlocks.NETHERWART_ECHOS_BLAZEBUD_CRATE.get());

                        output.accept(ENDERPEARL_ECHOS_SEEDS.get());
                        output.accept(REDSTONE_CHARGE_SEEDS.get());
                        output.accept(CREEPER_GREEN_SEEDS.get());
                        output.accept(EMERALD_DREAM_SEEDS.get());
                        output.accept(BLOCKHEAD_BLUE_SEEDS.get());
                        output.accept(NETHERWART_ECHOS_SEEDS.get());

                        output.accept(DREAMCAP_MUSHROOM.get());
                        output.accept(GOLDENGLOW_MUSHROOM.get());
                        output.accept(SNOWCAP_MUSHROOM.get());
                        output.accept(ETHEREAL_FUNGUS.get());
                        output.accept(MIRAGE_CACTUS.get());
                        output.accept(DIVINERS_SAGE.get());

                        output.accept(ModBlocks.EXTRACTION_STAND_BLOCK.get());
                        output.accept(GLASS_VIAL.get());
                        output.accept(CHORUS_EXTRACT.get());
                        output.accept(MIRAGE_CACTUS_EXTRACT.get());
                        output.accept(SUSPICIOUS_EXTRACT.get());
                        output.accept(AXOLOTL_VENOM.get());
                        output.accept(FROG_VENOM.get());
                        output.accept(MAGIC_FUNGUS_EXTRACT.get());

                        output.accept(MAGIC_FUNGUS_DUST.get());
                        output.accept(CHORUS_DUST.get());
                        output.accept(MIRAGE_DUST.get());
                        output.accept(SUSPICIOUS_DUST.get());
                        output.accept(TURTLE_SHELL_DUST.get());

                        output.accept(MIRAGE_CACTUS_JUICE.get());
                        output.accept(CHROUS_FRUIT_JUICE.get());

                        output.accept(BLAZEBUD_CIGARETTE.get());
                        output.accept(BLAZEBUD_MIXED_CIGARETTE.get());
                        output.accept(BLAZEBUD_CIGAR.get());
                        output.accept(BLAZEBUD_BROWNIE.get());
                        output.accept(BLAZEBUD_COOKIE.get());

                        output.accept(MUSIC_DISC_TROPIC_HERB.get());
                        output.accept(MUSIC_DISC_PRISM.get());

                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

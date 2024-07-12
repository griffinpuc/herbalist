package com.diggydwarff.herbalistmod;

import com.diggydwarff.herbalistmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.diggydwarff.herbalistmod.items.ModItems.*;
public class HerbalistCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HerbalistMod.MODID);

    public static final RegistryObject<CreativeModeTab> COURSE_TAB = CREATIVE_MODE_TABS.register("herbalistmod",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BLAZEBUD_CIGARETTE.get()))
                    .title(Component.translatable("creativetab.herbalistmod"))
                    .displayItems((displayParameters, output) -> {

                        output.accept(BLAZEBUD_CIGARETTE.get());
                        output.accept(SPLIFF.get());
                        output.accept(BLUNT.get());
                        output.accept(ROLLING_PAPER.get());
                        output.accept(MUSIC_DISC_TROPIC_HERB.get());

                        output.accept(ENDERPEARL_HAZE_BLAZEBUD.get());
                        output.accept(REDSTONE_KUSH_BLAZEBUD.get());
                        output.accept(CREEPER_GREEN_BLAZEBUD.get());
                        output.accept(EMERALD_DREAM_BLAZEBUD.get());
                        output.accept(BLOCKHEAD_BLUE_BLAZEBUD.get());
                        output.accept(NETHERWART_KUSH_BLAZEBUD.get());

                        output.accept(ENDERPEARL_HAZE_BLAZEBUD_STALK.get());
                        output.accept(REDSTONE_KUSH_BLAZEBUD_STALK.get());
                        output.accept(CREEPER_GREEN_BLAZEBUD_STALK.get());
                        output.accept(EMERALD_DREAM_BLAZEBUD_STALK.get());
                        output.accept(BLOCKHEAD_BLUE_BLAZEBUD_STALK.get());
                        output.accept(NETHERWART_KUSH_BLAZEBUD_STALK.get());

                        output.accept(ENDERPEARL_HAZE_SEEDS.get());
                        output.accept(REDSTONE_KUSH_SEEDS.get());
                        output.accept(CREEPER_GREEN_SEEDS.get());
                        output.accept(EMERALD_DREAM_SEEDS.get());
                        output.accept(BLOCKHEAD_BLUE_SEEDS.get());
                        output.accept(NETHERWART_KUSH_SEEDS.get());

                        output.accept(DREAMCAP_MUSHROOM.get());
                        output.accept(ETHEREAL_FUNGUS.get());
                        output.accept(MIRAGE_CACTUS.get());
                        output.accept(SUSPICIOUS_DUST.get());
                        output.accept(ETHEREAL_EXTRACT.get());
                        output.accept(CHORUS_EXTRACT.get());
                        output.accept(MIRAGE_CACTUS_EXTRACT.get());
                        output.accept(SUSPICIOUS_POTATO_EXTRACT.get());
                        output.accept(ETHEREAL_DUST.get());
                        output.accept(CHORUS_DUST.get());
                        output.accept(MIRAGE_DUST.get());
                        output.accept(SUSPICIOUS_DUST.get());


                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

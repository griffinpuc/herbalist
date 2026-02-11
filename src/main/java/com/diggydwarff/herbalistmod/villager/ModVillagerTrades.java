package com.diggydwarff.herbalistmod.villager;

import com.diggydwarff.herbalistmod.items.ModItems;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ModVillagerTrades {

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.HERBALIST_MASTER.get()) {

            List<VillagerTrades.ItemListing> level1 = event.getTrades().get(1);
            List<VillagerTrades.ItemListing> level2 = event.getTrades().get(2);
            List<VillagerTrades.ItemListing> level3 = event.getTrades().get(3);

            level1.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.ENDERPEARL_ECHOS_SEEDS.get(), 1),
                    16, 1, 0.05f
            ));

            level1.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.REDSTONE_CHARGE_SEEDS.get(), 1),
                    12, 2, 0.05f
            ));

            level1.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.CREEPER_GREEN_SEEDS.get(), 1),
                    12, 2, 0.05f
            ));

            level1.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.EMERALD_DREAM_SEEDS.get(), 1),
                    12, 2, 0.05f
            ));

            level1.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.BLOCKHEAD_BLUE_SEEDS.get(), 1),
                    12, 2, 0.05f
            ));

            level1.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.NETHERWART_ECHOS_SEEDS.get(), 1),
                    12, 2, 0.05f
            ));

            level2.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(ModItems.DREAMCAP_MUSHROOM.get(), 1),
                    12, 2, 0.05f
            ));

            level2.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(ModItems.GOLDENGLOW_MUSHROOM.get(), 1),
                    12, 2, 0.05f
            ));

            level2.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(ModItems.SNOWCAP_MUSHROOM.get(), 1),
                    12, 2, 0.05f
            ));

            level2.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(ModItems.ETHEREAL_FUNGUS.get(), 1),
                    12, 2, 0.05f
            ));

            level3.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(ModItems.MAGIC_FUNGUS_EXTRACT.get(), 1),
                    12, 2, 0.05f
            ));

            level3.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(ModItems.CHORUS_EXTRACT.get(), 1),
                    12, 2, 0.05f
            ));

            level3.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(ModItems.TURTLE_SHELL_DUST.get(), 1),
                    12, 2, 0.05f
            ));

            level3.add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(ModItems.FROG_VENOM_DUST.get(), 1),
                    12, 2, 0.05f
            ));
        }
    }
}


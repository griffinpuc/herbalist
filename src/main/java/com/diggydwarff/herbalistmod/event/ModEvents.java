package com.diggydwarff.herbalistmod.event;

import com.diggydwarff.herbalistmod.items.ModItems;
import com.diggydwarff.herbalistmod.villager.ModVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class ModEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.TOOLSMITH) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ItemStack stack = new ItemStack(ModItems.ENDERPEARL_HAZE_SEEDS.get(), 1);
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    stack,10,8,0.02F));
        }

        if(event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ItemStack ENDERPEARL_HAZE_SEEDS = new ItemStack(ModItems.ENDERPEARL_HAZE_SEEDS.get(), 1);
            ItemStack REDSTONE_KUSH_SEEDS = new ItemStack(ModItems.REDSTONE_KUSH_SEEDS.get(), 1);
            ItemStack CREEPER_GREEN_SEEDS = new ItemStack(ModItems.CREEPER_GREEN_SEEDS.get(), 1);
            ItemStack EMERALD_DREAM_SEEDS = new ItemStack(ModItems.EMERALD_DREAM_SEEDS.get(), 1);
            ItemStack BLOCKHEAD_BLUE_SEEDS = new ItemStack(ModItems.BLOCKHEAD_BLUE_SEEDS.get(), 1);
            ItemStack NETHERWART_KUSH_SEEDS = new ItemStack(ModItems.NETHERWART_KUSH_SEEDS.get(), 1);
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    ENDERPEARL_HAZE_SEEDS,10,8,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    REDSTONE_KUSH_SEEDS,10,8,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    CREEPER_GREEN_SEEDS,10,8,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    EMERALD_DREAM_SEEDS,10,8,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    BLOCKHEAD_BLUE_SEEDS,10,8,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    NETHERWART_KUSH_SEEDS,10,8,0.02F));
        }
    }
}

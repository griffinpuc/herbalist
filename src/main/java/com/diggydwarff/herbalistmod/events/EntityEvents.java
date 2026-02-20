package com.diggydwarff.herbalistmod.events;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    private static final long DAY_TICKS = 24000L;

    private static final String TAG_LAST_HARVEST_DAY = HerbalistMod.MODID + ":last_vial_harvest_day";
    private static final String TAG_ENTITY_LOCK_TICK   = HerbalistMod.MODID + ":vial_lock_tick";

    // Catch both routes; frogs especially can trigger either depending on where you click.
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        handle(event.getEntity(), event.getTarget(), event.getHand(), event);
    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        handle(event.getEntity(), event.getTarget(), event.getHand(), event);
    }

    private static void handle(Player player, Entity target, InteractionHand hand, PlayerInteractEvent event) {
        if (player.level().isClientSide) return;

        // Only care about these mobs
        Item rewardItem;
        Component failMsg;

        if (target instanceof Frog) {
            rewardItem = ModItems.FROG_VENOM.get();
            failMsg = Component.literal("The frog’s venom glands seem dry...");
        } else if (target instanceof Axolotl) {
            rewardItem = ModItems.AXOLOTL_VENOM.get();
            failMsg = Component.literal("The axolotl doesn’t seem to have any venom right now...");
        } else {
            return;
        }

        // Prefer MAIN_HAND vial if present; otherwise OFF_HAND vial.
        // This avoids weird double-hand routing.
        InteractionHand useHand = null;
        ItemStack main = player.getMainHandItem();
        ItemStack off  = player.getOffhandItem();

        if (main.is(ModItems.GLASS_VIAL.get())) useHand = InteractionHand.MAIN_HAND;
        else if (off.is(ModItems.GLASS_VIAL.get())) useHand = InteractionHand.OFF_HAND;

        if (useHand == null) return;

        // If this callback is for the "other" hand, ignore it and let the correct one handle.
        if (hand != useHand) return;

        // Hard entity tick lock: guarantees single processing even if both events fire this tick.
        long nowTick = player.level().getGameTime();
        CompoundTag eData = target.getPersistentData();
        if (eData.getLong(TAG_ENTITY_LOCK_TICK) == nowTick) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }
        eData.putLong(TAG_ENTITY_LOCK_TICK, nowTick);

        ItemStack held = player.getItemInHand(useHand);

        // Per-mob, once-per-day cooldown
        long currentDay = player.level().getDayTime() / DAY_TICKS;
        long lastDay = eData.getLong(TAG_LAST_HARVEST_DAY);

        if (lastDay == currentDay) {
            if (player instanceof ServerPlayer sp) {
                sp.displayClientMessage(failMsg, true);
            }
            player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 0.8f, 1.0f);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }

        eData.putLong(TAG_LAST_HARVEST_DAY, currentDay);

        // Consume one vial and give reward
        ItemStack reward = new ItemStack(rewardItem, 1);
        giveOrReplace(player, useHand, held, reward);

        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 0.9f, 1.1f);

        // Critical: stop vanilla entity interaction from also doing stuff.
        player.stopUsingItem();
        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);
    }

    private static void giveOrReplace(Player player, InteractionHand hand, ItemStack held, ItemStack reward) {
        int selected = player.getInventory().selected;

        // Consume exactly 1 empty vial
        held.shrink(1);
        if (held.isEmpty()) {
            player.setItemInHand(hand, ItemStack.EMPTY);
        }

        // Try to place reward somewhere NOT the selected slot.
        if (!addToInventoryAvoidSelected(player, reward, selected)) {
            player.drop(reward, false);
        }
    }

    private static boolean addToInventoryAvoidSelected(Player player, ItemStack stack, int selected) {
        var inv = player.getInventory();

        // 1) Find an empty slot in main inventory/hotbar that is NOT selected
        for (int i = 0; i < inv.items.size(); i++) {
            if (i == selected) continue;
            if (inv.items.get(i).isEmpty()) {
                inv.items.set(i, stack);
                return true;
            }
        }

        // 2) If none empty, try merging into existing stacks (shouldn’t usually apply, but safe)
        for (int i = 0; i < inv.items.size(); i++) {
            if (i == selected) continue;
            ItemStack s = inv.items.get(i);
            if (ItemStack.isSameItemSameTags(s, stack) && s.getCount() < s.getMaxStackSize()) {
                int move = Math.min(stack.getCount(), s.getMaxStackSize() - s.getCount());
                s.grow(move);
                stack.shrink(move);
                if (stack.isEmpty()) return true;
            }
        }

        return stack.isEmpty();
    }
}
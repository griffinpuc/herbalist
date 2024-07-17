package com.diggydwarff.herbalistmod.events;

import com.diggydwarff.herbalistmod.items.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class EntityEvents {

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!event.getLevel().isClientSide) {
            return;
        }

        Player player = event.getEntity();

        if(event.getHand().toString() == "MAIN_HAND"){

            // IF FROG GIVE FROG VENOM
            if ((event.getTarget() instanceof Frog)) {
                Frog frog = (Frog) event.getTarget();
                if(player.isHolding(ModItems.GLASS_VIAL.get())){
                    int glassVialCount = Lists.newArrayList(player.getHandSlots()).get(0).getCount();
                    if(glassVialCount == 1){
                        player.getInventory().setItem(player.getInventory().selected, new ItemStack(ModItems.FROG_VENOM.get()));
                    } else {
                        int freeSlotId = player.getInventory().getFreeSlot();
                        if(freeSlotId >= 0){
                            player.getInventory().setItem(freeSlotId, new ItemStack(ModItems.FROG_VENOM.get()));
                        } else{
                            return;
                        }
                    }
                }
                return;
            }

            // IF AXOLOTL GIVE AXOLOTL VENOM
            else if ((event.getTarget() instanceof Axolotl)) {
                Axolotl axolotl = (Axolotl) event.getTarget();
                if(player.isHolding(ModItems.GLASS_VIAL.get())){
                    if(Lists.newArrayList(player.getHandSlots()).get(0).getCount() == 1){
                        int freeSlotId = player.getInventory().getFreeSlot();
                        if(freeSlotId >= 0){
                            player.getInventory().setItem(freeSlotId, new ItemStack(ModItems.FROG_VENOM.get()));
                        } else{
                            return;
                        }
                    } else {
                        int freeSlotId = player.getInventory().getFreeSlot();
                        if(freeSlotId >= 0){
                            player.getInventory().setItem(freeSlotId, new ItemStack(ModItems.FROG_VENOM.get()));
                        } else{
                            return;
                        }
                    }
                }
                return;
            }
        }

        NetUtils.sendToServer(Main.SIMPLE_CHANNEL, new MessageSyncInventory(player.getUUID()));

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

}

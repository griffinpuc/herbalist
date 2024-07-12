package com.diggydwarff.herbalistmod.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BlazebudItem extends Item {

    String blazebudType;
    public BlazebudItem(Properties p_41383_, String blazebudType) {
        super(p_41383_);
        this.blazebudType = blazebudType;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
    {
        switch (this.blazebudType){
            case "enderpearl_haze":
                components.add(Component.literal("Enderpearl Haze").withStyle(ChatFormatting.GREEN));
                break;
            case "redstone_kush":
                components.add(Component.literal("Redstone Kush").withStyle(ChatFormatting.GREEN));
                break;
            case "creeper_green":
                components.add(Component.literal("Creeper Green").withStyle(ChatFormatting.GREEN));
                break;
            case "emerald_dream":
                components.add(Component.literal("Emerald Green").withStyle(ChatFormatting.GREEN));
                break;
            case "blockhead_blue":
                components.add(Component.literal("Blockhead Blue").withStyle(ChatFormatting.GREEN));
                break;
            case "netherwart_kush":
                components.add(Component.literal("Netherwart Kush").withStyle(ChatFormatting.GREEN));
                break;
        }
    }

    public String getBlazebudType() {
        return blazebudType;
    }
}

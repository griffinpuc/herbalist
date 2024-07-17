package com.diggydwarff.herbalistmod.items.custom;

import com.google.common.eventbus.Subscribe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GlassVial extends Item {
    public GlassVial(Properties p_41383_) {
        super(p_41383_);
    }
/*
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide()) {
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();

            ProjectileUtil.getEntityHitResult(
                    player.level(),
                    player,
                    positionClicked,
                    positionClicked,
                    player.getBoundingBox().expandTowards(player.getDeltaMovement()).inflate(1.0D),
                    (val) -> true);
        }

        }

        return InteractionResult.SUCCESS;
    }
*/
}

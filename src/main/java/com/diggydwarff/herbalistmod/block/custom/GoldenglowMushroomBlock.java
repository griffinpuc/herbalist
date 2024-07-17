package com.diggydwarff.herbalistmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class GoldenglowMushroomBlock extends FlowerBlock {

    public GoldenglowMushroomBlock(Supplier<MobEffect> effectSupplier, int p_53513_, Properties p_53514_) {
        super(effectSupplier, p_53513_, p_53514_);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(BlockTags.DIRT) || 
                blockState.is(Blocks.GRASS_BLOCK) ||
                blockState.is(Blocks.COARSE_DIRT) ||
                blockState.is(Blocks.ROOTED_DIRT) ||
                blockState.is(Blocks.MOSS_BLOCK) ||
                blockState.is(Blocks.GRANITE) ||
                blockState.is(Blocks.ANDESITE) ||
                blockState.is(Blocks.DEEPSLATE) ||
                blockState.is(Blocks.CLAY) ||
                blockState.is(Blocks.STONE);
    }

}

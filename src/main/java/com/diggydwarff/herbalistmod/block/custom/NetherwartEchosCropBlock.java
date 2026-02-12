package com.diggydwarff.herbalistmod.block.custom;

import com.diggydwarff.herbalistmod.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.IPlantable;

public class NetherwartEchosCropBlock extends CropBlock {

    public static final int FIRST_STAGE_MAX_AGE = 3;

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    public NetherwartEchosCropBlock(Properties properties) {
        super(properties);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int currentAge = this.getCurrentAge(pLevel, pPos, pState);

            int nextAge = currentAge + this.getBonemealAgeIncrease(pLevel);
            int maxAge = this.getMaxAge();

            if(nextAge > maxAge) {
                nextAge = maxAge;
            }

            if(nextAge > FIRST_STAGE_MAX_AGE) {
                if(pLevel.getBlockState(pPos.below(1)).is(this)){
                    pLevel.setBlock(pPos.below(1), this.getStateForAge(FIRST_STAGE_MAX_AGE), 2);
                    pLevel.setBlock(pPos, this.getStateForAge(nextAge), 2);
                } else{
                    pLevel.setBlock(pPos.above(1), this.getStateForAge(nextAge), 2);
                    pLevel.setBlock(pPos, this.getStateForAge(FIRST_STAGE_MAX_AGE), 2);
                }
            }
            else {
                pLevel.setBlock(pPos, this.getStateForAge(nextAge), 2);
            }
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return super.mayPlaceOn(state, world, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());

        // normal bottom crop rules (must be on farmland etc.)
        if (!below.is(this)) {
            return super.canSurvive(state, level, pos);
        }

        // upper part: only survive if the lower part itself is validly planted
        // (i.e., the block under the lower part supports the crop)
        BlockPos lowerPos = pos.below();
        BlockState lowerState = below;

        // optional: require the lower part to be "mature enough" to have an upper part
        if (this.getAge(lowerState) < FIRST_STAGE_MAX_AGE) {
            return false;
        }

        return super.canSurvive(lowerState, level, lowerPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();

        // prevent manual stacking
        if (level.getBlockState(pos.below()).is(this)) {
            return null; // placement fails
        }

        return super.getStateForPlacement(ctx);
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int currentAge = this.getCurrentAge(pLevel, pPos, pState);

        int nextAge = currentAge + this.getBonemealAgeIncrease(pLevel);
        int maxAge = this.getMaxAge();

        if(nextAge > maxAge) {
            nextAge = maxAge;
        }

        if(nextAge > FIRST_STAGE_MAX_AGE) {
            if(pLevel.getBlockState(pPos.below(1)).is(this)){
                pLevel.setBlock(pPos.below(1), this.getStateForAge(FIRST_STAGE_MAX_AGE), 2);
                pLevel.setBlock(pPos, this.getStateForAge(nextAge), 2);
            } else{
                pLevel.setBlock(pPos.above(1), this.getStateForAge(nextAge), 2);
                pLevel.setBlock(pPos, this.getStateForAge(FIRST_STAGE_MAX_AGE), 2);
            }
        }
        else {
            pLevel.setBlock(pPos, this.getStateForAge(nextAge), 2);
        }
    }

    @Override
    public int getMaxAge() {
        return FIRST_STAGE_MAX_AGE ;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.NETHERWART_ECHOS_SEEDS.get();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    protected int getCurrentAge(Level pLevel, BlockPos pPos, BlockState pState){
        if(pLevel.getBlockState(pPos.above(1)).is(this)){
            return this.getAge(pState)+this.getAge(pLevel.getBlockState(pPos.above(1)));
        } else if(pLevel.getBlockState(pPos.below(1)).is(this)){
            return this.getAge(pState)+this.getAge(pLevel.getBlockState(pPos.below(1)));
        } else{
            return this.getAge(pState);
        }
    }
}

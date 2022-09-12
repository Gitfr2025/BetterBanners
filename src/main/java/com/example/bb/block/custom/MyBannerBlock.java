package com.example.bb.block.custom;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class MyBannerBlock extends AbstractBannerBlock {
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    private static final Map<DyeColor, Block> BY_COLOR = Maps.newHashMap();
    private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public MyBannerBlock(DyeColor dyeColor, BlockBehaviour.Properties properties) {
        super(dyeColor, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, Integer.valueOf(0)));
        BY_COLOR.put(dyeColor, this);
    }

    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.below()).getMaterial().isSolid();
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(ROTATION, Integer.valueOf(Mth.floor((double)((180.0F + blockPlaceContext.getRotation()) * 16.0F / 360.0F) + 0.5D) & 15));
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos p_49033_, BlockPos p_49034_) {
        return direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, p_49033_) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, levelAccessor, p_49033_, p_49034_);
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(ROTATION, Integer.valueOf(rotation.rotate(blockState.getValue(ROTATION), 16)));
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(ROTATION, Integer.valueOf(mirror.mirror(blockState.getValue(ROTATION), 16)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    public static Block byColor(DyeColor dyeColor) {
        return BY_COLOR.getOrDefault(dyeColor, Blocks.WHITE_BANNER);
    }
}

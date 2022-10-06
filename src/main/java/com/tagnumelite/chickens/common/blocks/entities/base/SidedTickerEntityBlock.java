package com.tagnumelite.chickens.common.blocks.entities.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SidedTickerEntityBlock extends EntityBlock {
    @Nullable
    @Override
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return level.isClientSide ? this::tickClient : this::tickServer;
    }

    default <T extends BlockEntity> void tickClient(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) { }

    default <T extends BlockEntity> void tickServer(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) { }
}

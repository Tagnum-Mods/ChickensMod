package com.tagnumelite.chickens.common.blocks;

import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.common.blocks.entities.CoopBlockEntity;
import com.tagnumelite.chickens.common.blocks.entities.base.SidedTickerEntityBlock;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoopBlock extends BaseEntityBlock implements SidedTickerEntityBlock, IProbeInfoAccessor {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public CoopBlock(Properties pProperties) {
        super(pProperties.noOcclusion().dynamicShape());
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState blockState, @NotNull Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void setPlacedBy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof CoopBlockEntity coop) {
            if (stack.hasCustomHoverName()) coop.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof CoopBlockEntity coop) {
                Containers.dropContents(level, pos, coop);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CoopBlockEntity coop) {
                NetworkHooks.openScreen((ServerPlayer) player, coop, pos);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof CoopBlockEntity coop ? coop : null;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CoopBlockEntity(pos, state);
    }

    public <T extends BlockEntity> void tickServer(Level level, BlockPos blockPos, BlockState blockState, T entity) {
        if (entity instanceof CoopBlockEntity coop) {
            coop.tickServer();
        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState blockState, IProbeHitData hitData) {
        BlockEntity blockEntity = level.getBlockEntity(hitData.getPos());
        if (blockEntity instanceof CoopBlockEntity coop) {
            if (coop.hasChicken()) info.text(TranslationUtils.CHICKEN_NAME(coop.getChickenType()));
            info.horizontal().progress(coop.getProgress(), 100).text("Time Remaining: " + coop.getProgress() / 20 + "s");
        }
    }
}

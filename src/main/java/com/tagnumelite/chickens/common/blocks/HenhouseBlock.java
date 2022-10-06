package com.tagnumelite.chickens.common.blocks;

import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
import com.tagnumelite.chickens.client.menus.HenhouseMenu;
import com.tagnumelite.chickens.common.blocks.entities.HenhouseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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

import java.util.List;

/**
 * Created by setyc on 01.03.2016.
 */
public class HenhouseBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    static final Component CONTAINER_TITLE = TranslationUtils.CGUI_CONTAINER("henhouse");

    public HenhouseBlock(Properties blockProperties) {
        super(blockProperties);
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState blockState, @NotNull Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof HenhouseBlockEntity henhouse) {
            NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(HenhouseMenu.getServerMenu(henhouse, pos), CONTAINER_TITLE));
            return InteractionResult.CONSUME;
        }

        return InteractionResult.sidedSuccess(!level.isClientSide);
    }

    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new SimpleMenuProvider((index, playerInventory, player) -> HenhouseMenu.getClientMenu(index, playerInventory), CONTAINER_TITLE);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, BlockGetter level, @NotNull List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltips, tooltipFlag);
        tooltips.add(TranslationUtils.CGUI_CONTAINER(TranslationConstants.HENHOUSE_TOOLTIP));
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return ModBlockEntityTypes.HENHOUSE.get().create(blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof HenhouseBlockEntity henhouse) { // TODO: container
                Containers.dropContents(level, pos, new SimpleContainer());
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    // OLD

    /*
    @Override
    public void onBlockPlacedBy(Level worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            BlockEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof HenhouseBlockEntity) {
                ((HenhouseBlockEntity) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }
     */
}

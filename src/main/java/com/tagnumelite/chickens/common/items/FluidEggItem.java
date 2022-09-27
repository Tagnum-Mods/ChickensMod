package com.tagnumelite.chickens.common.items;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.capability.FluidEggWrapper;
import com.tagnumelite.chickens.api.fluid_egg.FluidEggData;
import com.tagnumelite.chickens.api.utils.Utils;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by setyc on 14.02.2016.
 */
public class FluidEggItem extends EggItem implements DispensibleContainerItem, ItemColor {
    public FluidEggItem(Properties itemProperties) {
        super(itemProperties);
    }

    public static Fluid getFluid(ItemStack stack) {
        if (stack.getTag() == null) return Fluids.EMPTY;

        return ForgeRegistries.FLUIDS.getValue(Utils.getFluidFromStack(stack));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.liquid_egg.tooltip"));
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab category, @NotNull NonNullList<ItemStack> subItems) {
        if (allowedIn(category)) {
            for (ResourceLocation id : Chickens.getLiquidEggManager().getEggs().keySet()) {
                subItems.add(Utils.itemWithType(this, id));
            }
        }
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (!stack.hasTag()) return 0xFFFFFF;
        FluidEggData eggData = Chickens.getLiquidEggManager().getEggData(Utils.getTypeFromStack(stack));
        return tintIndex == 0 ? eggData.primaryColor() : eggData.secondaryColor();
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        ResourceLocation type = Utils.getTypeFromTag(stack.getTag());
        return Component.translatable("item." + type.getNamespace() + ".fluid_egg." + type.getPath().replace('/', '.'));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        //Direction face = context.getClickedFace();
        //BlockPos hit = context.getClickedPos().relative(face);

        /*
        if (level.isEmptyBlock(hit)) {
            return InteractionResult.PASS;
        } else {
            if (!level.isBlockModifiable(player, hit)) {
                return InteractionResult.FAIL;
            } else {
                boolean flag1 = level.getBlockState(hit).getBlock().isReplaceable(level, hit);
                BlockPos blockPos1 = flag1 && raytraceresult.sideHit == Direction.UP ? hit : hit.offset(raytraceresult.sideHit);

                Block liquid = LiquidEggRegistry.findById(itemStack.getTag()).liquid();
                if (!player.mayUseItemAt(blockPos1,face, itemStack)) {
                    return InteractionResult.FAIL;
                } else if (this.tryPlaceContainedLiquid(player, level, blockPos1, liquid)) {
                    player.awardStat(StatList.getObjectUseStats(this));
                    return !player.capabilities.isCreativeMode ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(itemStack.getItem(), itemStack.stackSize - 1, itemStack.getMetadata())) : new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
                } else {
                    return InteractionResult.FAIL;
                }
            }
        }
         */
        return super.use(level, player, hand);
    }

    public boolean tryPlaceContainedLiquid(@Nullable Player playerIn, Level worldIn, BlockPos pos, Block liquid) {
        Material material = worldIn.getBlockState(pos).getMaterial();
        boolean flag = !material.isSolid();

        if (!worldIn.isEmptyBlock(pos) && !flag) {
            return false;
        } else {
            if (worldIn.dimensionType().ultraWarm() && liquid == Blocks.WATER) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.playSound(playerIn, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else {
                if (!worldIn.isClientSide && flag && !material.isLiquid()) {
                    worldIn.destroyBlock(pos, true);
                }

                worldIn.setBlock(pos, liquid.defaultBlockState(), 3);
            }

            return true;
        }
    }

    protected void playEmptySound(@Nullable Player pPlayer, LevelAccessor pLevel, BlockPos pPos) {
        ItemStack stack = pPlayer.getItemInHand(pPlayer.getUsedItemHand());
        Fluid fluid = getFluid(stack);
        SoundEvent soundevent = fluid.getFluidType().getSound(pPlayer, pLevel, pPos, net.minecraftforge.common.SoundActions.BUCKET_EMPTY);
        if (soundevent == null)
            soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        pLevel.playSound(pPlayer, pPos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pPos);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        if (this.getClass() == FluidEggItem.class)
            return new FluidEggWrapper(stack);
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public boolean emptyContents(Player pPlayer, @NotNull Level pLevel, @NotNull BlockPos pPos, BlockHitResult pResult) {
        ItemStack stack = pPlayer.getItemInHand(pPlayer.getUsedItemHand());
        Fluid fluid = getFluid(stack);
        if (!(fluid instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState blockstate = pLevel.getBlockState(pPos);
            Block block = blockstate.getBlock();
            Material material = blockstate.getMaterial();
            boolean flag = blockstate.canBeReplaced(fluid);
            boolean flag1 = blockstate.isAir() || flag || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(pLevel, pPos, blockstate, fluid);
            if (!flag1) {
                return pResult != null && this.emptyContents(pPlayer, pLevel, pResult.getBlockPos().relative(pResult.getDirection()), null);
            } else if (pLevel.dimensionType().ultraWarm() && fluid.is(FluidTags.WATER)) {
                int i = pPos.getX();
                int j = pPos.getY();
                int k = pPos.getZ();
                pLevel.playSound(pPlayer, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    pLevel.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            } else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(pLevel, pPos, blockstate, fluid)) {
                ((LiquidBlockContainer) block).placeLiquid(pLevel, pPos, blockstate, ((FlowingFluid) fluid).getSource(false));
                this.playEmptySound(pPlayer, pLevel, pPos);
                return true;
            } else {
                if (!pLevel.isClientSide && flag && !material.isLiquid()) {
                    pLevel.destroyBlock(pPos, true);
                }

                if (!pLevel.setBlock(pPos, fluid.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(pPlayer, pLevel, pPos);
                    return true;
                }
            }
        }
    }

    public ItemStack fromFluidType(ResourceLocation fluid) {
        return Utils.itemWithFluid(this, fluid);
    }
}

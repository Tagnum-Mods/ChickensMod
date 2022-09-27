package com.tagnumelite.chickens.common.items;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenData;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import com.tagnumelite.chickens.common.entities.ModEntityTypes;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by setyc on 12.02.2016.
 */
public class SpawnEggItem extends Item implements ItemColor {
    private static final DispenseItemBehavior DEFAULT_DISPENSE_BEHAVIOR = ((pSource, pStack) -> {
        Direction face = pSource.getBlockState().getValue(DispenserBlock.FACING);
        EntityType<?> type = ((net.minecraft.world.item.SpawnEggItem) pStack.getItem()).getType(pStack.getTag());

        try {
            type.spawn(pSource.getLevel(), pStack, null, pSource.getPos().relative(face), MobSpawnType.DISPENSER, face != Direction.UP, false);
        } catch (Exception exception) {
            DispenseItemBehavior.LOGGER.error("Error while dispensing spawn egg from dispenser at {}", pSource.getPos(), exception);
            return ItemStack.EMPTY;
        }

        pStack.shrink(1);
        pSource.getLevel().gameEvent(GameEvent.ENTITY_PLACE, pSource.getPos(), GameEvent.Context.of(pSource.getBlockState()));
        return pStack;
    });

    public SpawnEggItem(Properties props) {
        super(props);
    }

    public static DispenseItemBehavior createDispenseBehavior() {
        return DEFAULT_DISPENSE_BEHAVIOR;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> subItems) {
        if (allowedIn(tab)) {
            for (ResourceLocation chickenId : Chickens.getChickenManager().getChickens().keySet()) {
                subItems.add(Utils.itemWithType(this, chickenId));
            }
        }
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        ResourceLocation type = Utils.getTypeFromTag(stack.getTag());
        return Component.translatable("entity." + type.toLanguageKey() + ".name");
    }

    @Override
    public int getColor(@NotNull ItemStack stack, int tintIndex) {
        ChickenData chickenData = Chickens.getChickenManager().getChickenData(Utils.getTypeFromStack(stack));
        return tintIndex == 0 ? chickenData.secondaryColor() : chickenData.primaryColor();
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = pContext.getItemInHand();
            BlockPos blockPos = pContext.getClickedPos();
            Direction clickedFace = pContext.getClickedFace();
            BlockState blockState = level.getBlockState(blockPos);
            /*
            if (blockState.is(Blocks.SPAWNER)) {
                BlockEntity blockentity = level.getBlockEntity(blockPos);
                if (blockentity instanceof SpawnerBlockEntity) {
                    BaseSpawner basespawner = ((SpawnerBlockEntity)blockentity).getSpawner();
                    EntityType<?> chickenEntityType = ModEntityTypes.CHICKEN.get();
                    basespawner.setEntityId(chickenEntityType);
                    blockentity.setChanged();
                    level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                    level.gameEvent(pContext.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                    stack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
             */

            BlockPos newBlockPos;
            if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
                newBlockPos = blockPos;
            } else {
                newBlockPos = blockPos.relative(clickedFace);
            }

            EntityType<?> chickenEntityType = ModEntityTypes.CHICKEN.get();
            Entity chicken = chickenEntityType.spawn((ServerLevel) level, stack, pContext.getPlayer(), newBlockPos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockPos, newBlockPos) && clickedFace == Direction.UP);
            if (chicken != null) {
                stack.shrink(1);
                level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
                ((ChickensChicken) chicken).setChickenType(Utils.getTypeFromStack(stack));
            }

            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        BlockHitResult hitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else if (pLevel.isClientSide) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockPos blockPos = hitResult.getBlockPos();
            if (!(pLevel.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (pLevel.mayInteract(pPlayer, blockPos) && pPlayer.mayUseItemAt(blockPos, hitResult.getDirection(), itemstack)) {
                EntityType<?> chickenEntityType = ModEntityTypes.CHICKEN.get();
                Entity entity = chickenEntityType.spawn((ServerLevel) pLevel, itemstack, pPlayer, blockPos, MobSpawnType.SPAWN_EGG, false, false);
                if (entity == null) {
                    return InteractionResultHolder.pass(itemstack);
                } else {
                    ((ChickensChicken) entity).setChickenType(Utils.getTypeFromStack(itemstack));
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, entity.position());
                    return InteractionResultHolder.consume(itemstack);
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    private BlockPos correctPosition(BlockPos pos, Direction side) {
        final int[] offsetsXForSide = new int[]{0, 0, 0, 0, -1, 1};
        final int[] offsetsYForSide = new int[]{-1, 1, 0, 0, 0, 0};
        final int[] offsetsZForSide = new int[]{0, 0, -1, 1, 0, 0};

        int posX = pos.getX() + offsetsXForSide[side.ordinal()];
        int posY = pos.getY() + offsetsYForSide[side.ordinal()];
        int posZ = pos.getZ() + offsetsZForSide[side.ordinal()];

        return new BlockPos(posX, posY, posZ);
    }

    private void activate(ItemStack stack, Level level, BlockPos pos, CompoundTag metadata) {
        ChickensChicken entity = ModEntityTypes.CHICKEN.get().create(level);
        if (entity == null) return;

        entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        //entity.onInitialSpawn(level.getCurrentDifficultyAt(pos), null); TODO
        entity.setChickenType(Utils.getTypeFromStack(stack));

        Chickens.LOGGER.debug("Chicken Type Stack: {}; Entity: {}", Utils.getTypeFromStack(stack), entity.getChickenType());

        CompoundTag stackNBT = stack.getTag();
        if (stackNBT != null) {
            CompoundTag entityNBT = entity.saveWithoutId(new CompoundTag());
            entityNBT.merge(stackNBT);
            entity.load(entityNBT);
        }

        level.addFreshEntity(entity);
    }

    public ItemStack fromChickenType(ResourceLocation chickenId) {
        return Utils.itemWithType(this, chickenId);
    }
}
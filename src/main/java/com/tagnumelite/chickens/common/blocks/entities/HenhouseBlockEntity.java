package com.tagnumelite.chickens.common.blocks.entities;

import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.client.menus.ModMenuTypes;
import com.tagnumelite.chickens.common.blocks.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by setyc on 01.03.2016.
 */
public class HenhouseBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
    public static final int HAY_BALE_SLOT = 0;
    public static final int DIRT_SLOT = 1;
    public static final int hayBaleEnergy = 100;
    public static final int hayBaleSlotIndex = 0;
    public static final int dirtSlotIndex = 1;
    public static final int firstItemSlotIndex = 2;
    private static final int[] SLOTS_FOR_UP = new int[]{HAY_BALE_SLOT};
    private static final int[] SLOTS_FOR_SIDES = new int[]{DIRT_SLOT};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int lastItemSlotIndex = 10;
    private static final double HENHOUSE_RADIUS = 0.5;
    private static final double FENCE_THRESHOLD = 0.5;
    private final ItemStackHandler inventory = new ItemStackHandler(11);
    private final ItemStack[] slots = new ItemStack[11];
    protected NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);
    private int progress = 0;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                progress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    private int energy = 0;

    public HenhouseBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.HENHOUSE.get(), pos, blockState);
    }

    @Nullable
    public static ItemStack pushItemStack(ItemStack itemToLay, Level worldObj, Vec3 pos) {
        List<HenhouseBlockEntity> henhouses = findHenhouses(worldObj, pos, 4 + HENHOUSE_RADIUS + FENCE_THRESHOLD);
        for (HenhouseBlockEntity henhouse : henhouses) {
            itemToLay = henhouse.pushItemStack(itemToLay);
            if (itemToLay == null) {
                break;
            }
        }
        return itemToLay;
    }

    private static List<HenhouseBlockEntity> findHenhouses(Level level, Vec3 pos, double radius) {
        int firstChunkX = Mth.floor((pos.x - radius - Level.MAX_ENTITY_SPAWN_Y) / 16.0D);
        int lastChunkX = Mth.floor((pos.x + radius + Level.MAX_ENTITY_SPAWN_Y) / 16.0D);
        int firstChunkY = Mth.floor((pos.z - radius - Level.MAX_ENTITY_SPAWN_Y) / 16.0D);
        int lastChunkY = Mth.floor((pos.z + radius + Level.MAX_ENTITY_SPAWN_Y) / 16.0D);

        List<Double> distances = new ArrayList<>();
        List<HenhouseBlockEntity> result = new ArrayList<>();
        for (int chunkX = firstChunkX; chunkX <= lastChunkX; ++chunkX) {
            for (int chunkY = firstChunkY; chunkY <= lastChunkY; ++chunkY) {
                LevelChunk chunk = level.getChunk(chunkX, chunkY);
                for (BlockEntity tileEntity : chunk.getBlockEntities().values()) {
                    if (tileEntity instanceof HenhouseBlockEntity) {
                        Vec3 tileEntityPos = new Vec3(tileEntity.getBlockPos().getX(), tileEntity.getBlockPos().getY(), tileEntity.getBlockPos().getZ()).add(HENHOUSE_RADIUS, HENHOUSE_RADIUS, HENHOUSE_RADIUS);
                        boolean inRage = testRange(pos, tileEntityPos, radius);
                        if (inRage) {
                            double distance = pos.distanceTo(tileEntityPos);
                            addHenhouseToResults((HenhouseBlockEntity) tileEntity, distance, distances, result);
                        }
                    }
                }
            }
        }
        return result;
    }

    private static boolean testRange(Vec3 pos1, Vec3 pos2, double range) {
        return Math.abs(pos1.x - pos2.x) <= range &&
                Math.abs(pos1.y - pos2.y) <= range &&
                Math.abs(pos1.z - pos2.z) <= range;
    }

    private static void addHenhouseToResults(HenhouseBlockEntity henhouse, double distance, List<Double> distances, List<HenhouseBlockEntity> henhouses) {
        for (int resultIndex = 0; resultIndex < distances.size(); resultIndex++) {
            if (distance < distances.get(resultIndex)) {
                distances.add(resultIndex, distance);
                henhouses.add(resultIndex, henhouse);
                return;
            }
        }
        distances.add(distance);
        henhouses.add(henhouse);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return TranslationUtils.CGUI_CONTAINER("henhouse");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory) {
        return ModMenuTypes.HENHOUSE.get().create(containerId, playerInventory);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.energy = compound.getInt("energy");
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.items);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("energy", this.energy);
        ContainerHelper.saveAllItems(compound, this.items);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (!itemStack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack slotItem = this.items.get(slot);
        boolean flag = !stack.isEmpty() && stack.sameItem(slotItem) && ItemStack.tagMatches(stack, slotItem);
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (slot == 0 && !flag) { // TODO
            //this.cookingTotalTime = getTotalCookTime(this.level, this);
            //this.cookingProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        return switch (side) {
            case DOWN -> SLOTS_FOR_DOWN;
            case UP -> SLOTS_FOR_UP;
            default -> SLOTS_FOR_SIDES;
        };
        /*
        switch (side) {
            case DOWN:
                int itemSlotCount = lastItemSlotIndex - firstItemSlotIndex + 1;
                int[] itemSlots = new int[itemSlotCount + 1];
                itemSlots[0] = dirtSlotIndex;
                for (int resultIndex = 0; resultIndex < itemSlotCount; resultIndex++) {
                    itemSlots[resultIndex + 1] = firstItemSlotIndex + resultIndex;
                }
                return itemSlots;
            case UP:
                return new int[]{hayBaleSlotIndex};
            default:
                return new int[0];
        }
         */
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, Direction side) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction side) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        if (index == hayBaleSlotIndex) {
            return stack.getItem() == Item.byBlock(Blocks.HAY_BLOCK);
        }
        return index != dirtSlotIndex;
    }

    @Override
    public void fillStackedContents(@NotNull StackedContents helper) {
        for (ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }
    }

    @Nullable
    private ItemStack pushItemStack(ItemStack stack) {
        ItemStack rest = stack.copy();

        int capacity = getEffectiveCapacity();
        if (capacity <= 0) {
            return rest;
        }

        for (int slotIndex = firstItemSlotIndex; slotIndex <= lastItemSlotIndex; slotIndex++) {
            int canAdd = canAdd(slots[slotIndex], rest);
            int willAdd = Math.min(canAdd, capacity);
            if (willAdd > 0) {
                consumeEnergy(willAdd);
                capacity -= willAdd;

                if (slots[slotIndex] == null) {
                    slots[slotIndex] = rest.split(willAdd);
                } else {
                    //slots[slotIndex].stackSize += willAdd; TODO
                    //rest.stackSize -= willAdd;
                }

                if (rest.getMaxStackSize() <= 0) {
                    return null;
                }
            }
        }

        //markDirty();
        return rest;
    }

    private void consumeEnergy(int amount) {
        while (amount > 0) {
            if (energy == 0) {
                assert slots[hayBaleSlotIndex] != null;
                //slots[hayBaleSlotIndex].stackSize--; TODO
                //if (slots[hayBaleSlotIndex].stackSize <= 0) {
                //    slots[hayBaleSlotIndex] = null;
                //}
                energy += hayBaleEnergy;
            }

            int consumed = Math.min(amount, energy);
            energy -= consumed;
            amount -= consumed;

            if (energy <= 0) {
                if (slots[dirtSlotIndex] == null) {
                    slots[dirtSlotIndex] = new ItemStack(Blocks.DIRT, 1);
                } else {
                    //slots[dirtSlotIndex].stackSize++; TODO
                }
            }
        }
    }

    private int canAdd(@Nullable ItemStack slotStack, ItemStack inputStack) {
        if (slotStack == null) {
            return Math.min(getMaxStackSize(), inputStack.getCount());
        }
        if (!slotStack.sameItem(inputStack)) {
            return 0;
        }
        if (slotStack.getCount() >= getMaxStackSize()) {
            return 0;
        }
        return Math.min(inputStack.getCount(), getMaxStackSize() - slotStack.getCount());
    }

    private int getEffectiveCapacity() {
        return Math.min(getInputCapacity(), getOutputCapacity());
    }

    private int getInputCapacity() {
        int potential = energy;

        ItemStack hayBaleStack = slots[hayBaleSlotIndex];
        if (hayBaleStack != null && hayBaleStack.getItem() == Item.byBlock(Blocks.HAY_BLOCK)) {
            potential += hayBaleStack.getCount() * hayBaleEnergy;
        }

        return potential;
    }

    private int getOutputCapacity() {
        ItemStack dirtStack = slots[dirtSlotIndex];
        if (dirtStack == null) {
            return getMaxStackSize() * hayBaleEnergy;
        }
        if (dirtStack.getItem() != Item.byBlock(Blocks.DIRT)) {
            return 0;
        }
        return (getMaxStackSize() - dirtStack.getCount()) * hayBaleEnergy;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ContainerData getContainerData() {
        return this.data;
    }
}

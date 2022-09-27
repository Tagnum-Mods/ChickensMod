package com.tagnumelite.chickens.client.menus;

import com.tagnumelite.chickens.api.SlotWithRestriction;
import com.tagnumelite.chickens.api.utils.MenuUtils;
import com.tagnumelite.chickens.common.blocks.ModBlocks;
import com.tagnumelite.chickens.common.blocks.entities.HenhouseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class HenhouseMenu extends AbstractContainerMenu {
    private final Player player;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData data;

    public HenhouseMenu(int containerId, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(ModMenuTypes.HENHOUSE.get(), containerId);
        this.levelAccess = ContainerLevelAccess.create(playerInventory.player.getLevel(), pos);
        this.player = playerInventory.player;
        this.data = data;

        addSlot(new SlotItemHandler(slots, HenhouseBlockEntity.hayBaleSlotIndex, 25, 19));
        addSlot(new SlotWithRestriction(slots, HenhouseBlockEntity.dirtSlotIndex, 25, 55));

        MenuUtils.addPlayerInvSlots(playerInventory, this::addSlot);

        this.addDataSlots(this.data);
    }

    public static HenhouseMenu getClientMenu(int id, Inventory playerInventory) {
        return new HenhouseMenu(id, playerInventory, new ItemStackHandler(2), BlockPos.ZERO, new SimpleContainerData(3));
    }

    public static MenuConstructor getServerMenu(HenhouseBlockEntity blockEntity, BlockPos pos) {
        return (id, playerInventory, player) -> new HenhouseMenu(id, playerInventory, blockEntity.getInventory(), pos, blockEntity.getContainerData());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack currentStack = null;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack current = slot.getItem();
            currentStack = current.copy();

            /* TODO
            if (index < this.henhouseBlockEntity.getSizeInventory()) {
                if (!this.mergeItemStack(current, this.henhouseBlockEntity.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(current, 0, this.henhouseBlockEntity.getSizeInventory(), false)) {
                return null;
            }

            if (current.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
             */
        }

        return currentStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        // TODO: Include other henhouses
        return stillValid(levelAccess, player, ModBlocks.HENHOUSE_OAK.get());
    }
}

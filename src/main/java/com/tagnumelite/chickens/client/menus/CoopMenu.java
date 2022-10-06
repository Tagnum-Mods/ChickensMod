package com.tagnumelite.chickens.client.menus;

import com.tagnumelite.chickens.api.RestrictedSlot;
import com.tagnumelite.chickens.api.utils.MenuUtils;
import com.tagnumelite.chickens.common.items.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CoopMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final Level level;

    public CoopMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, playerInventory, new SimpleContainer(6), new SimpleContainerData(1));
    }

    public CoopMenu(int pContainerId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.COOP.get(), pContainerId);
        checkContainerSize(container, 6);
        checkContainerDataCount(data, 1);
        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level;

        addSlot(new RestrictedSlot(container, 0, 18, 20, stack -> stack.is(ModItems.SPAWN_EGG.get())));
        for (int slot = 1; slot < 6; ++slot) {
            addSlot(new RestrictedSlot(container, slot, 75 - 18 + slot * 18, 20));
        }

        MenuUtils.addPlayerInvSlots(playerInventory, this::addSlot, 8, 51);
        this.addDataSlots(data);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIdx) { // TODO
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIdx);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (slotIdx < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(slotStack, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }
}

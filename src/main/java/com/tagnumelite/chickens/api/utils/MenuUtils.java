package com.tagnumelite.chickens.api.utils;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import java.util.function.Function;

public class MenuUtils {
    public static final int SLOT_SIZE_PLUS_2 = 18;
    public static final int PLAYER_INV_ROWS = 3;
    public static final int PLAYER_INV_COLS = 9;
    public static final int OFFSET_X = 8;
    public static final int OFFSET_Y = 84;
    public static final int HOT_BAR_OFFSET_Y = 58;

    /**
     * A utility to add the players inventory to the menu
     *
     * @param playerInventory The inventory of the player
     * @param addSlot         The function {@link AbstractContainerMenu#addSlot(Slot)} to add slots with
     */
    public static void addPlayerInvSlots(Inventory playerInventory, Function<Slot, Slot> addSlot) {
        addPlayerInvSlots(playerInventory, addSlot, OFFSET_X, OFFSET_Y);
    }

    /**
     * A utility to add the players inventory to the menu
     *
     * @param playerInventory The inventory of the player
     * @param addSlot         The function {@link AbstractContainerMenu#addSlot(Slot)} to add slots with
     * @param offsetX         stuifwadaasd
     * @param offsetY         stuff
     */
    public static void addPlayerInvSlots(Inventory playerInventory, Function<Slot, Slot> addSlot, int offsetX, int offsetY) {
        for (int row = 0; row < PLAYER_INV_ROWS; ++row) {
            for (int column = 0; column < PLAYER_INV_COLS; ++column) {
                addSlot.apply(new Slot(playerInventory, column + row * 9 + PLAYER_INV_COLS, offsetX + column * SLOT_SIZE_PLUS_2, offsetY + row * SLOT_SIZE_PLUS_2));
            }
        }

        for (int column = 0; column < PLAYER_INV_COLS; ++column) {
            addSlot.apply(new Slot(playerInventory, column, offsetX + column * SLOT_SIZE_PLUS_2, offsetY + HOT_BAR_OFFSET_Y));
        }
    }
}

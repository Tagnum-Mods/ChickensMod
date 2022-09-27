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
    public static final int HOT_BAR_OFFSET_Y = 142;

    /**
     * A utility to add the players inventory to the menu
     *
     * @param playerInventory The inventory of the player
     * @param addSlot         The function {@link AbstractContainerMenu#addSlot(Slot)} to add slots with
     */
    public static void addPlayerInvSlots(Inventory playerInventory, Function<Slot, Slot> addSlot) {
        for (int row = 0; row < PLAYER_INV_ROWS; ++row) {
            for (int column = 0; column < PLAYER_INV_COLS; ++column) {
                addSlot.apply(new Slot(playerInventory, column + row * 9 + PLAYER_INV_COLS, OFFSET_X + column * SLOT_SIZE_PLUS_2, OFFSET_Y + row * SLOT_SIZE_PLUS_2));
            }
        }

        for (int column = 0; column < PLAYER_INV_COLS; ++column) {
            addSlot.apply(new Slot(playerInventory, column, OFFSET_X + column * SLOT_SIZE_PLUS_2, HOT_BAR_OFFSET_Y));
        }
    }
}

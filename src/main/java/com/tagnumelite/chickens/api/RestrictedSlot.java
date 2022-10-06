package com.tagnumelite.chickens.api;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class RestrictedSlot extends Slot {
    private final Predicate<ItemStack> validator;

    public RestrictedSlot(Container pContainer, int pSlot, int pX, int pY) {
        this(pContainer, pSlot, pX, pY, stack -> false);
    }

    public RestrictedSlot(Container pContainer, int pSlot, int pX, int pY, Predicate<ItemStack> validator) {
        super(pContainer, pSlot, pX, pY);
        this.validator = validator;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return validator.test(stack);
    }
}

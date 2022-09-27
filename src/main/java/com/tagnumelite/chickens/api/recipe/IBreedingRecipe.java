package com.tagnumelite.chickens.api.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface IBreedingRecipe extends ISpecialRecipe<Container> {
    ItemStack getFirstParent();

    ItemStack getSecondParent();
}

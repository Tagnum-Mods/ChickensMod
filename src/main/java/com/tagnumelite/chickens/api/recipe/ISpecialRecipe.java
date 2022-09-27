package com.tagnumelite.chickens.api.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface ISpecialRecipe<C extends Container> extends Recipe<C> {
    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    default boolean isSpecial() {
        return true;
    }
}

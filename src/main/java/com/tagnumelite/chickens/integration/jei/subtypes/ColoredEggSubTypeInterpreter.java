package com.tagnumelite.chickens.integration.jei.subtypes;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ColoredEggSubTypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public @NotNull String apply(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return null;
    }
}

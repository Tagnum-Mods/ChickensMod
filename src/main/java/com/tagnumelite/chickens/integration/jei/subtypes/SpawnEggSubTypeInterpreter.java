package com.tagnumelite.chickens.integration.jei.subtypes;

import com.tagnumelite.chickens.api.utils.Utils;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpawnEggSubTypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public @NotNull String apply(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return Utils.getTypeFromStack(ingredient).toString();
    }
}

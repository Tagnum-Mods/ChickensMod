package com.tagnumelite.chickens.crafting;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.recipe.IBreedingRecipe;
import com.tagnumelite.chickens.api.recipe.IThrowingRecipe;
import com.tagnumelite.chickens.api.utils.Utils;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Chickens.MOD_ID);

    public static final RegistryObject<RecipeType<IBreedingRecipe>> BREEDING = register("breeding");
    public static final RegistryObject<RecipeType<IThrowingRecipe>> THROWING = register("throwing");

    private static <R extends Recipe<?>> RegistryObject<RecipeType<R>> register(String name) {
        return RECIPE_TYPES.register(name, () -> RecipeType.simple(Utils.rl(name)));
    }
}

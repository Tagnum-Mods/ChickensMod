package com.tagnumelite.chickens.integration.jei;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenHolder;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.items.ModItems;
import com.tagnumelite.chickens.crafting.ModRecipeTypes;
import com.tagnumelite.chickens.integration.jei.categories.BreedingRecipeCategory;
import com.tagnumelite.chickens.integration.jei.categories.LayingRecipeCategory;
import com.tagnumelite.chickens.integration.jei.categories.ThrowingRecipeCategory;
import com.tagnumelite.chickens.integration.jei.subtypes.ColoredEggSubTypeInterpreter;
import com.tagnumelite.chickens.integration.jei.subtypes.FluidEggSubTypeInterpreter;
import com.tagnumelite.chickens.integration.jei.subtypes.SpawnEggSubTypeInterpreter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by setyc on 21.02.2016.
 */
@JeiPlugin
public class ChickensJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID = Utils.rl("jei");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        //registration.addRecipeClickArea(); TODO: Breeding, Laying and Throwing
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.COLORED_EGG.get(), new ColoredEggSubTypeInterpreter());
        registration.registerSubtypeInterpreter(ModItems.SPAWN_EGG.get(), new SpawnEggSubTypeInterpreter());
        registration.registerSubtypeInterpreter(ModItems.FLUID_EGG.get(), new FluidEggSubTypeInterpreter());
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.SPAWN_EGG.get()), BreedingRecipeCategory.RECIPE_TYPE, LayingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(
                new BreedingRecipeCategory(jeiHelpers.getGuiHelper()),
                new LayingRecipeCategory(jeiHelpers.getGuiHelper()),
                new ThrowingRecipeCategory(jeiHelpers.getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(BreedingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ModRecipeTypes.BREEDING.get()));
        registration.addRecipes(LayingRecipeCategory.RECIPE_TYPE, Chickens.getChickenManager().getChickens().entrySet().stream().map(ChickenHolder::from).toList());
    }
}

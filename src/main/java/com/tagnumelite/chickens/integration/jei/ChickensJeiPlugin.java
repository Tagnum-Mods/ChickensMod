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
        registration.addRecipeCatalyst(new ItemStack(ModItems.SPAWN_EGG.get()), LayingRecipeCategory.RECIPE_TYPE, BreedingRecipeCategory.RECIPE_TYPE);
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

    /*
    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeCategories(
                new LayingRecipeCategory(jeiHelpers.getGuiHelper()),
                new BreedingRecipeCategory(jeiHelpers.getGuiHelper()),
                new DropRecipeCategory(jeiHelpers.getGuiHelper()),
                new ThrowingRecipeCategory(jeiHelpers.getGuiHelper()),
                new HenhousingRecipeCategory(jeiHelpers.getGuiHelper())
        );
        registry.addRecipeHandlers(
                new LayingRecipeHandler(),
                new BreedingRecipeHandler(),
                new DropRecipeHandler(),
                new ThrowingRecipeHandler(),
                new HenhousingRecipeHandler()
        );
        registry.addRecipes(getLayingRecipes());
        registry.addRecipes(getBreedingRecipes());
        registry.addRecipes(getDropRecipes());
        registry.addRecipes(getThrowRecipes());
        registry.addRecipes(getHenhouseRecipes());
    }

    private List<LayingRecipeWrapper> getLayingRecipes() {
        List<LayingRecipeWrapper> result = new ArrayList<LayingRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
            result.add(new LayingRecipeWrapper(
                    new ItemStack(Chickens.spawnEgg, 1, chicken.getId()),
                    chicken.createLayItem(),
                    chicken.getMinLayTime(), chicken.getMaxLayTime()
            ));
        }
        return result;
    }

    private List<DropRecipeWrapper> getDropRecipes() {
        List<DropRecipeWrapper> result = new ArrayList<DropRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
            result.add(new DropRecipeWrapper(
                    new ItemStack(Chickens.spawnEgg, 1, chicken.getId()),
                    chicken.createDropItem()
            ));
        }
        return result;
    }

    private List<BreedingRecipeWrapper> getBreedingRecipes() {
        List<BreedingRecipeWrapper> result = new ArrayList<BreedingRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
            if (chicken.isBreedable()) {
                //noinspection ConstantConditions
                result.add(new BreedingRecipeWrapper(
                        new ItemStack(Chickens.spawnEgg, 1, chicken.getParent1().getId()),
                        new ItemStack(Chickens.spawnEgg, 1, chicken.getParent2().getId()),
                        new ItemStack(Chickens.spawnEgg, 1, chicken.getId()),
                        ChickensRegistry.getChildChance(chicken)
                ));
            }
        }
        return result;
    }

    private List<ThrowingRecipeWrapper> getThrowRecipes() {
        List<ThrowingRecipeWrapper> result = new ArrayList<ThrowingRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
            if (chicken.isDye()) {
                result.add(new ThrowingRecipeWrapper(
                        new ItemStack(Chickens.coloredEgg, 1, chicken.getDyeMetadata()),
                        new ItemStack(Chickens.spawnEgg, 1, chicken.getId())));
            }
        }
        return result;
    }

    private List<HenhousingRecipeWrapper> getHenhouseRecipes() {
        List<HenhousingRecipeWrapper> henhouseRecipes = new ArrayList<HenhousingRecipeWrapper>();
        henhouseRecipes.add(new HenhousingRecipeWrapper(new ItemStack(Blocks.HAY_BLOCK), new ItemStack(Blocks.DIRT)));
        return henhouseRecipes;
    }
     */
}

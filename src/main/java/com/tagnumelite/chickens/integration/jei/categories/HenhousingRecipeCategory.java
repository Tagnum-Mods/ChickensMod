package com.tagnumelite.chickens.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.recipe.IBreedingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Created by setyc on 07.01.2017.
 */
public class HenhousingRecipeCategory implements IRecipeCategory<IBreedingRecipe> {
    public static final String UID = "chickens.Henhousing";
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;

    public HenhousingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(Chickens.MOD_ID, "textures/gui/henhouse.png");
        background = guiHelper.createDrawable(location, 18, 12, 72, 62);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 195, 0, 12, 57);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.TOP, true);

        ResourceLocation iconLocation = new ResourceLocation(Chickens.MOD_ID, "textures/gui/henhousing_icon.png");
        icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);
    }

    @Override
    public @NotNull RecipeType<IBreedingRecipe> getRecipeType() {
        return null;
    }

    @Override
    public @NotNull Component getTitle() {
        return null;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IBreedingRecipe recipe, IFocusGroup focuses) {
        /*
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        int inputSlot = 0;
        guiItemStacks.init(inputSlot, true, 24 - 18, 17 - 12);
        guiItemStacks.set(ingredients);

        int outputSlot = 1;
        guiItemStacks.init(outputSlot, false, 24 - 18, 54 - 12);
        guiItemStacks.set(ingredients);
         */
    }

    @Override
    public void draw(IBreedingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        arrow.draw(stack, 75 - 18, 14 - 12);
    }
}

package com.tagnumelite.chickens.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.recipe.IThrowingRecipe;
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
 * Created by setyc on 21.02.2016.
 */
public class DropRecipeCategory implements IRecipeCategory<IThrowingRecipe> {
    public static final String UID = "chickens.Drops";
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;

    public DropRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(Chickens.MOD_ID, "textures/gui/drops.png");
        background = guiHelper.createDrawable(location, 0, 0, 82, 54);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 82, 0, 13, 10);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        ResourceLocation iconLocation = new ResourceLocation(Chickens.MOD_ID, "textures/gui/drops_icon.png");
        icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);
    }

    @Override
    public @NotNull RecipeType<IThrowingRecipe> getRecipeType() {
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, IThrowingRecipe recipe, IFocusGroup focuses) {
        /*
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        int inputSlot = 0;
        guiItemStacks.init(inputSlot, true, 13, 15);
        guiItemStacks.set(ingredients);

        int outputSlot = 1;
        guiItemStacks.init(outputSlot, false, 57, 15);
        guiItemStacks.set(ingredients);
         */
    }

    @Override
    public void draw(IThrowingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        arrow.draw(stack, 40, 21);
    }
}

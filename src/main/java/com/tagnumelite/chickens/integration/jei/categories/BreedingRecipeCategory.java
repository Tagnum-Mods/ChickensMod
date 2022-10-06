package com.tagnumelite.chickens.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.recipe.IBreedingRecipe;
import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Created by setyc on 21.02.2016.
 */
public class BreedingRecipeCategory implements IRecipeCategory<IBreedingRecipe> {
    public static final RecipeType<IBreedingRecipe> RECIPE_TYPE = RecipeType.create(Chickens.MOD_ID, "breeding", IBreedingRecipe.class);
    public static final Component TITLE = TranslationUtils.CGUI(TranslationConstants.JEI_BREEDING);
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;

    public BreedingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(Chickens.MOD_ID, "textures/gui/jei/breeding.png");
        background = guiHelper.createDrawable(location, 0, 0, 82, 54);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 82, 0, 7, 7);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);

        ResourceLocation iconLocation = new ResourceLocation(Chickens.MOD_ID, "textures/gui/jei/breeding_icon.png");
        icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);
    }

    @Override
    public @NotNull Component getTitle() {
        return TITLE;
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
    public @NotNull RecipeType<IBreedingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public void draw(@NotNull IBreedingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
        arrow.draw(stack, 37, 5);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull IBreedingRecipe recipe, @NotNull IFocusGroup focuses) {
        IRecipeSlotBuilder parent_1 = builder.addSlot(RecipeIngredientRole.INPUT, 10, 15);
        parent_1.addIngredient(VanillaTypes.ITEM_STACK, recipe.getFirstParent());

        IRecipeSlotBuilder parent_2 = builder.addSlot(RecipeIngredientRole.INPUT, 53, 15);
        parent_2.addIngredient(VanillaTypes.ITEM_STACK, recipe.getSecondParent());

        IRecipeSlotBuilder child = builder.addSlot(RecipeIngredientRole.OUTPUT, 33, 30);
        child.addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }
}

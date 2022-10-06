package com.tagnumelite.chickens.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenHolder;
import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
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
public class LayingRecipeCategory implements IRecipeCategory<ChickenHolder> {
    public static final Component TITLE = TranslationUtils.CGUI(TranslationConstants.JEI_LAYING);
    public static final RecipeType<ChickenHolder> RECIPE_TYPE = RecipeType.create(Chickens.MOD_ID, "laying", ChickenHolder.class);
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;

    public LayingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(Chickens.MOD_ID, "textures/gui/laying.png");
        background = guiHelper.createDrawable(location, 0, 0, 82, 54);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 82, 0, 13, 10);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        ResourceLocation iconLocation = new ResourceLocation(Chickens.MOD_ID, "textures/gui/laying_icon.png");
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
    public @NotNull RecipeType<ChickenHolder> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public void draw(@NotNull ChickenHolder recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
        arrow.draw(stack, 40, 21);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ChickenHolder recipe, @NotNull IFocusGroup focuses) {
        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 13, 15);
        //input.addIngredients(ingredients);

        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 57, 15);
        //output.addIngredients(ingredients);
    }
}

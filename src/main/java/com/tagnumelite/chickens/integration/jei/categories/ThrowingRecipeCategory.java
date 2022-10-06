package com.tagnumelite.chickens.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.recipe.IThrowingRecipe;
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
 * Created by setyc on 07.01.2017.
 */
public class ThrowingRecipeCategory implements IRecipeCategory<IThrowingRecipe> {
    public static final String UID = "chickens.Throws";
    public static final Component TITLE = TranslationUtils.CGUI(TranslationConstants.JEI_THROWING);
    public static final RecipeType<IThrowingRecipe> RECIPE_TYPE = RecipeType.create(Chickens.MOD_ID, "throwing", IThrowingRecipe.class);
    private final IDrawableStatic background;
    private final IDrawableStatic icon;
    private final IDrawableAnimated arrow;

    public ThrowingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(Chickens.MOD_ID, "textures/gui/throws.png");
        background = guiHelper.createDrawable(location, 0, 0, 82, 54);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 82, 0, 13, 10);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        ResourceLocation iconLocation = new ResourceLocation(Chickens.MOD_ID, "textures/gui/throws_icon.png");
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
    public @NotNull RecipeType<IThrowingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull IThrowingRecipe recipe, @NotNull IFocusGroup focuses) {
        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 10, 15);
        //input.addIngredients(VanillaTypes.ITEM_STACK, recipe.getIngredients());

        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 15);
        //output.addIngredients(ingredients);
    }

    @Override
    public void draw(@NotNull IThrowingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
        arrow.draw(stack, 32, 21);
    }
}

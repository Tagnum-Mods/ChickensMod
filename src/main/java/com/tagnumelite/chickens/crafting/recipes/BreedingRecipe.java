package com.tagnumelite.chickens.crafting.recipes;

import com.google.gson.JsonObject;
import com.tagnumelite.chickens.api.recipe.IBreedingRecipe;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.crafting.ModRecipeSerializers;
import com.tagnumelite.chickens.crafting.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BreedingRecipe implements IBreedingRecipe {
    private final ResourceLocation id;
    private final ItemStack child;
    private final ItemStack parent1;
    private final ItemStack parent2;

    public BreedingRecipe(ResourceLocation id, ItemStack child, ItemStack parent1, ItemStack parent2) {
        this.id = id;
        this.child = child;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        boolean parent1Present = false, parent2Present = false;
        for (int slot = 0; slot < container.getContainerSize(); ++slot) {
            ItemStack itemStack = container.getItem(slot);
            if (!itemStack.isEmpty()) {
                parent1Present = Utils.compareChickenStacks(itemStack, parent1);
                parent2Present = Utils.compareChickenStacks(itemStack, parent2);
            }
        }

        return parent1Present && parent2Present;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container) {
        return child.copy();
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return child.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.BREEDING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.BREEDING.get();
    }

    @Override
    public ItemStack getFirstParent() {
        return parent1.copy();
    }

    @Override
    public ItemStack getSecondParent() {
        return parent2.copy();
    }

    public static class Serializer implements RecipeSerializer<IBreedingRecipe> {
        @Override
        public @NotNull IBreedingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            ItemStack child = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("child"));
            ItemStack parent1 = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("parent1"));
            ItemStack parent2 = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("parent2"));
            return new BreedingRecipe(recipeId, child, parent1, parent2);
        }

        @Override
        public @Nullable IBreedingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            return new BreedingRecipe(recipeId, buffer.readItem(), buffer.readItem(), buffer.readItem());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull IBreedingRecipe recipe) {
            buffer.writeItem(recipe.getResultItem());
            buffer.writeItem(recipe.getFirstParent());
            buffer.writeItem(recipe.getSecondParent());
        }
    }
}

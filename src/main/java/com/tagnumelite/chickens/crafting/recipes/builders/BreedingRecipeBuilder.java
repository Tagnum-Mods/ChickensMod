package com.tagnumelite.chickens.crafting.recipes.builders;

import com.google.gson.JsonObject;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.items.SpawnEggItem;
import com.tagnumelite.chickens.crafting.ModRecipeSerializers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BreedingRecipeBuilder implements RecipeBuilder {
    private final List<ItemStack> parents = Lists.newArrayList();
    private final ItemStack child;

    public BreedingRecipeBuilder(ItemStack child) {
        this.child = child;
    }

    public static BreedingRecipeBuilder breeding(ItemStack child) {
        return new BreedingRecipeBuilder(child);
    }

    public BreedingRecipeBuilder addParent(ItemStack parent) {
        if (parents.size() == 2) throw new IllegalStateException("Chickens can't have more than two parents");
        this.parents.add(parent);
        return this;
    }

    @Override
    public @NotNull BreedingRecipeBuilder unlockedBy(@NotNull String pCriterionName, @NotNull CriterionTriggerInstance pCriterionTrigger) {
        return this;
    }

    @Override
    public @NotNull BreedingRecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return child.getItem();
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer) {
        @NotNull Item result = getResult();
        ResourceLocation itemId = RecipeBuilder.getDefaultRecipeId(result);
        if (result instanceof SpawnEggItem) {
            ResourceLocation chickenType = Utils.getTypeFromStack(child);
            itemId = new ResourceLocation(chickenType.getNamespace(), "chicken_spawn_egg/" + chickenType.getPath());
        }
        this.save(consumer, itemId);
    }

    @Override
    @Deprecated
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull String recipeName) {
        throw new IllegalStateException("Saving recipes using this method is not supported");
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation recipeId) {
        if (parents.size() != 2)
            throw new IllegalStateException("There must be two parents for a child");
        consumer.accept(new Result(recipeId, child, parents));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack child;
        private final List<ItemStack> parents;

        public Result(ResourceLocation id, ItemStack child, List<ItemStack> parents) {
            this.id = id;
            this.child = child;
            this.parents = parents;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("child", Utils.getJsonStack(child));
            json.add("parent1", Utils.getJsonStack(parents.get(0)));
            json.add("parent2", Utils.getJsonStack(parents.get(1)));
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ModRecipeSerializers.BREEDING.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

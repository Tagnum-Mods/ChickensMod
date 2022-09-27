package com.tagnumelite.chickens.crafting;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.recipe.IBreedingRecipe;
import com.tagnumelite.chickens.crafting.recipes.BreedingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Chickens.MOD_ID);

    public static final RegistryObject<RecipeSerializer<IBreedingRecipe>> BREEDING = RECIPE_SERIALIZERS.register("breeding", BreedingRecipe.Serializer::new);
}

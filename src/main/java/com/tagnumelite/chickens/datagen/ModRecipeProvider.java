package com.tagnumelite.chickens.datagen;

import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.blocks.ModBlocks;
import com.tagnumelite.chickens.common.items.ModItems;
import com.tagnumelite.chickens.crafting.recipes.builders.BreedingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModItems.ANALYZER.get())
                .pattern(" C ")
                .pattern(" E ")
                .pattern(" S ")
                .define('C', Items.COMPASS)
                .define('E', Items.EGG)
                .define('S', Items.STICK)
                .unlockedBy("has_egg", has(Items.EGG))
                .save(consumer);

        registerHenhouse(ModBlocks.HENHOUSE_ACACIA.get(), Blocks.ACACIA_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_BIRCH.get(), Blocks.BIRCH_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_CRIMSON.get(), Blocks.CRIMSON_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_DARK_OAK.get(), Blocks.DARK_OAK_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_JUNGLE.get(), Blocks.JUNGLE_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_MANGROVE.get(), Blocks.MANGROVE_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_OAK.get(), Blocks.OAK_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_SPRUCE.get(), Blocks.SPRUCE_PLANKS, consumer);
        registerHenhouse(ModBlocks.HENHOUSE_WARPED.get(), Blocks.WARPED_PLANKS, consumer);

        addParents(consumer);
    }

    protected void addParents(@NotNull Consumer<FinishedRecipe> consumer) {
        // Base
        BreedingRecipeBuilder.breeding(egg("orange")).addParent(egg("red")).addParent(egg("yellow")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("light_blue")).addParent(egg("white")).addParent(egg("blue")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("lime")).addParent(egg("green")).addParent(egg("white")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("cyan")).addParent(egg("blue")).addParent(egg("green")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("light_gray")).addParent(egg("gray")).addParent(egg("white")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("magenta")).addParent(egg("purple")).addParent(egg("pink")).save(consumer);

        // Tier 2
        BreedingRecipeBuilder.breeding(egg("string")).addParent(egg("black")).addParent(egg("oak")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("glowstone")).addParent(egg("quartz")).addParent(egg("yellow")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("gunpowder")).addParent(egg("sand")).addParent(egg("flint")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("redstone")).addParent(egg("red")).addParent(egg("sand")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("glass")).addParent(egg("quartz")).addParent(egg("redstone")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("iron")).addParent(egg("flint")).addParent(egg("white")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("coal")).addParent(egg("flint")).addParent(egg("oak")).save(consumer);

        // Tier 3
        BreedingRecipeBuilder.breeding(egg("gold")).addParent(egg("iron")).addParent(egg("yellow")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("snowball")).addParent(egg("blue")).addParent(egg("oak")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("water")).addParent(egg("gunpowder")).addParent(egg("snowball")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("lava")).addParent(egg("coal")).addParent(egg("quartz")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("clay")).addParent(egg("snowball")).addParent(egg("sand")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("leather")).addParent(egg("string")).addParent(egg("brown")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("netherwart")).addParent(egg("brown")).addParent(egg("glowstone")).save(consumer);

        // Tier 4
        BreedingRecipeBuilder.breeding(egg("diamond")).addParent(egg("glass")).addParent(egg("gold")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("blaze")).addParent(egg("gold")).addParent(egg("lava")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("slime")).addParent(egg("clay")).addParent(egg("green")).save(consumer);

        // Tier 5
        BreedingRecipeBuilder.breeding(egg("ender_pearl")).addParent(egg("diamond")).addParent(egg("netherwart")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("ghast")).addParent(egg("white")).addParent(egg("blaze")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("emerald")).addParent(egg("diamond")).addParent(egg("green")).save(consumer);
        BreedingRecipeBuilder.breeding(egg("magma_cream")).addParent(egg("slime")).addParent(egg("blaze")).save(consumer);
    }

    private ItemStack egg(String name) {
        return ModItems.SPAWN_EGG.get().fromChickenType(Utils.rl(name));
    }

    private void registerHenhouse(Block henhouse, Block woodBlock, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(henhouse)
                .pattern("PPP")
                .pattern("PHP")
                .pattern("PPP")
                .define('P', woodBlock)
                .define('H', Blocks.HAY_BLOCK)
                .unlockedBy("has_wood", has(woodBlock))
                .save(consumer);
    }
}

package com.tagnumelite.chickens.datagen;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.common.blocks.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Chickens.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        henhouse(ModBlocks.HENHOUSE_ACACIA.get(), blockTexture(Blocks.ACACIA_PLANKS));
        henhouse(ModBlocks.HENHOUSE_BIRCH.get(), blockTexture(Blocks.BIRCH_PLANKS));
        henhouse(ModBlocks.HENHOUSE_CRIMSON.get(), blockTexture(Blocks.CRIMSON_PLANKS));
        henhouse(ModBlocks.HENHOUSE_DARK_OAK.get(), blockTexture(Blocks.DARK_OAK_PLANKS));
        henhouse(ModBlocks.HENHOUSE_JUNGLE.get(), blockTexture(Blocks.JUNGLE_PLANKS));
        henhouse(ModBlocks.HENHOUSE_MANGROVE.get(), blockTexture(Blocks.MANGROVE_PLANKS));
        henhouse(ModBlocks.HENHOUSE_OAK.get(), blockTexture(Blocks.OAK_PLANKS));
        henhouse(ModBlocks.HENHOUSE_SPRUCE.get(), blockTexture(Blocks.SPRUCE_PLANKS));
        henhouse(ModBlocks.HENHOUSE_WARPED.get(), blockTexture(Blocks.WARPED_PLANKS));
    }

    private void henhouse(Block block, ResourceLocation blockTexture) {
        @Nullable ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        horizontalBlock(block, blockTexture, modLoc(ModelProvider.BLOCK_FOLDER + "/henhouse_front"), blockTexture);
        //horizontalBlock(block, models().withExistingParent(name.getPath(), modLoc("henhouse")).texture("planks", blockTexture));
    }
}

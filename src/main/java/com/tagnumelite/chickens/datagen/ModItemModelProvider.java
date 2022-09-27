package com.tagnumelite.chickens.datagen;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.common.blocks.ModBlocks;
import com.tagnumelite.chickens.common.items.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Chickens.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.ANALYZER.get());
        basicItem(ModItems.SPAWN_EGG.get()).texture("layer1", "item/spawn_egg_overlay");
        basicItem(ModItems.FLUID_EGG.get()).texture("layer1", "item/fluid_egg_overlay");
        basicItem(ModItems.COLORED_EGG.get());

        sameBlock(ModBlocks.ROOST_ACACIA.get());
        sameBlock(ModBlocks.ROOST_BIRCH.get());
        sameBlock(ModBlocks.ROOST_CRIMSON.get());
        sameBlock(ModBlocks.ROOST_DARK_OAK.get());
        sameBlock(ModBlocks.ROOST_JUNGLE.get());
        sameBlock(ModBlocks.ROOST_MANGROVE.get());
        sameBlock(ModBlocks.ROOST_OAK.get());
        sameBlock(ModBlocks.ROOST_SPRUCE.get());
        sameBlock(ModBlocks.ROOST_WARPED.get());

        sameBlock(ModBlocks.HENHOUSE_ACACIA.get());
        sameBlock(ModBlocks.HENHOUSE_BIRCH.get());
        sameBlock(ModBlocks.HENHOUSE_CRIMSON.get());
        sameBlock(ModBlocks.HENHOUSE_DARK_OAK.get());
        sameBlock(ModBlocks.HENHOUSE_JUNGLE.get());
        sameBlock(ModBlocks.HENHOUSE_MANGROVE.get());
        sameBlock(ModBlocks.HENHOUSE_OAK.get());
        sameBlock(ModBlocks.HENHOUSE_SPRUCE.get());
        sameBlock(ModBlocks.HENHOUSE_WARPED.get());
    }

    private void sameBlock(ItemLike item) {
        sameBlock(Registry.ITEM.getKey(item.asItem()).getPath());
    }

    private void sameBlock(String name) {
        withExistingParent(name, modLoc(BLOCK_FOLDER + '/' + name));
    }

    private void sameItem(String name) {
        withExistingParent(name, modLoc(ITEM_FOLDER + '/' + name));
    }
}

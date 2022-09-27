package com.tagnumelite.chickens.common.blocks;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.client.ModTabs;
import com.tagnumelite.chickens.common.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Chickens.MOD_ID);

    // Henhouses
    public static final RegistryObject<HenhouseBlock> HENHOUSE_ACACIA = registerHenhouse("henhouse_acacia", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_BIRCH = registerHenhouse("henhouse_birch", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_CRIMSON = registerHenhouse("henhouse_crimson", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_DARK_OAK = registerHenhouse("henhouse_dark_oak", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_JUNGLE = registerHenhouse("henhouse_jungle", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_MANGROVE = registerHenhouse("henhouse_mangrove", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_OAK = registerHenhouse("henhouse_oak", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_SPRUCE = registerHenhouse("henhouse_spruce", BlockBehaviour.Properties.of(Material.WOOD));
    public static final RegistryObject<HenhouseBlock> HENHOUSE_WARPED = registerHenhouse("henhouse_warped", BlockBehaviour.Properties.of(Material.WOOD));

    // Utility Methods
    public static RegistryObject<HenhouseBlock> registerHenhouse(String name, BlockBehaviour.Properties blockProperties) {
        return register(name, () -> new HenhouseBlock(blockProperties));
    }

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<B> blockSupplier) {
        return register(name, blockSupplier, ModTabs.GENERAL);
    }

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<B> blockSupplier, CreativeModeTab tab) {
        return register(name, blockSupplier, new Item.Properties().tab(tab));
    }

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<B> blockSupplier, Item.Properties itemProperties) {
        RegistryObject<B> block = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), itemProperties));
        return block;
    }
}

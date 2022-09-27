package com.tagnumelite.chickens.common.blocks;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.common.blocks.entities.HenhouseBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Chickens.MOD_ID);

    public static final RegistryObject<BlockEntityType<HenhouseBlockEntity>> HENHOUSE = BLOCK_ENTITY_TYPES.register("henhouse", () -> BlockEntityType.Builder.of(HenhouseBlockEntity::new,
            ModBlocks.HENHOUSE_BIRCH.get(),
            ModBlocks.HENHOUSE_OAK.get(),
            ModBlocks.HENHOUSE_MANGROVE.get(),
            ModBlocks.HENHOUSE_SPRUCE.get(),
            ModBlocks.HENHOUSE_JUNGLE.get(),
            ModBlocks.HENHOUSE_DARK_OAK.get()).build(null));
}

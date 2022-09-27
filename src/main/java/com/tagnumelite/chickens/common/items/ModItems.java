package com.tagnumelite.chickens.common.items;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.client.ModTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Chickens.MOD_ID);

    public static final RegistryObject<AnalyzerItem> ANALYZER = ITEMS.register("analyzer", () -> new AnalyzerItem(itemProperties()));
    public static final RegistryObject<SpawnEggItem> SPAWN_EGG = ITEMS.register("spawn_egg", () -> new SpawnEggItem(itemProperties()));
    public static final RegistryObject<ColoredEggItem> COLORED_EGG = ITEMS.register("colored_egg", () -> new ColoredEggItem(itemProperties()));
    public static final RegistryObject<FluidEggItem> FLUID_EGG = ITEMS.register("fluid_egg", () -> new FluidEggItem(itemProperties()));


    public static final RegistryObject<Item> CHICKEN = ITEMS.register("chicken", () -> new Item(new Item.Properties()));

    public static Item.Properties itemProperties() {
        return new Item.Properties().tab(ModTabs.GENERAL);
    }
}

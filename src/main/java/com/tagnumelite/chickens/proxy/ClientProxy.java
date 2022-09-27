package com.tagnumelite.chickens.proxy;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.common.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by setyc on 18.02.2016.
 */
public class ClientProxy extends CommonProxy {
    private void registerItemModel(Item item) {
        ModelResourceLocation resourceLocation = new ModelResourceLocation(Chickens.MOD_ID + ":" + ForgeRegistries.ITEMS.getKey(item).getPath(), "inventory");
        Minecraft.getInstance().getItemRenderer().getItemModelShaper().register(item, resourceLocation);
    }

    @Override
    public void registerItemHandlers() {
        registerItemModel(ModItems.FLUID_EGG.get());
        registerItemModel(ModItems.SPAWN_EGG.get());
        registerItemModel(ModItems.COLORED_EGG.get());
    }
}

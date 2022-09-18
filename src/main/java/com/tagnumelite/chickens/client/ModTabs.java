package com.tagnumelite.chickens.client;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.common.blocks.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModTabs {
    public static final CreativeModeTab GENERAL = new CreativeModeTab(Chickens.MOD_ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModBlocks.HENHOUSE_OAK.get());
        }
    };
}

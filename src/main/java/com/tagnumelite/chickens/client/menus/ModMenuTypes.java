package com.tagnumelite.chickens.client.menus;

import com.tagnumelite.chickens.Chickens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Chickens.MOD_ID);

    public static final RegistryObject<MenuType<HenhouseMenu>> HENHOUSE = MENU_TYPES.register("henhouse", () -> new MenuType<>(HenhouseMenu::getClientMenu));
}

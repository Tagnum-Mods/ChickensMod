package com.tagnumelite.chickens.api.utils;

import com.tagnumelite.chickens.Chickens;
import net.minecraft.network.chat.Component;

public class TranslateUtils {
    public static String GUI(String name) {
        return "gui." + Chickens.MOD_ID + "." + name;
    }

    public static Component CGUI(String name) {
        return Component.translatable(GUI(name));
    }

    public static String GUI_CONTAINER(String name) {
        return GUI("container." + name);
    }

    public static Component CGUI_CONTAINER(String name) {
        return Component.translatable(GUI_CONTAINER(name));
    }
}

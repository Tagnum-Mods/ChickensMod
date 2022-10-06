package com.tagnumelite.chickens.api.utils;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class TranslationUtils {
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

    public static Component CHICKEN_NAME(@Nullable ResourceLocation chickenType) {
        if (chickenType == null) return Component.translatable(TranslationConstants.NULL_CHICKEN);
        return Component.translatable("entity." + chickenType.toLanguageKey() + ".name");
    }
}

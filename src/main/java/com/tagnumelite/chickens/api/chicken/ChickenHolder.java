package com.tagnumelite.chickens.api.chicken;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ChickenHolder(ResourceLocation id, ChickenData data) {
    public static ChickenHolder from(Map.Entry<ResourceLocation, ChickenData> entry) {
        return new ChickenHolder(entry.getKey(), entry.getValue());
    }
}

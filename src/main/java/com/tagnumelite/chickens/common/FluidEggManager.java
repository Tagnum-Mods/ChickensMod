package com.tagnumelite.chickens.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.fluid_egg.FluidEggData;
import com.tagnumelite.chickens.common.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FluidEggManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<ResourceLocation, FluidEggData> liquidEggs = new HashMap<>();

    public FluidEggManager() {
        super(GSON, "liquid_eggs");
    }

    public static ItemStack getEgg(ResourceLocation id) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("liquid", id.toString());
        return new ItemStack(ModItems.FLUID_EGG.get(), 1, nbt);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement json = entry.getValue();

            FluidEggData.CODEC.decode(JsonOps.INSTANCE, json).get()
                    .ifLeft(result -> liquidEggs.put(id, result.getFirst()))
                    .ifRight(error -> Chickens.LOGGER.error("Failed to convert {} to liquid egg because {}", id, error.message()));
        }
    }

    public FluidEggData getEggData(ResourceLocation id) {
        return liquidEggs.get(id);
    }

    public Map<ResourceLocation, FluidEggData> getEggs() {
        return ImmutableMap.copyOf(liquidEggs);
    }

    public Set<ResourceLocation> getEggIds() {
        return liquidEggs.keySet();
    }

    public ItemStack getEggFromFluid(FluidStack fluidStack) {
        @Nullable ResourceLocation fluidKey = ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid());
        return ModItems.FLUID_EGG.get().fromFluidType(fluidKey);
    }
}

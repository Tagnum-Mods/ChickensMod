package com.tagnumelite.chickens.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenData;
import com.tagnumelite.chickens.api.chicken.ChickenHolder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChickenManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, ChickenData> chickens = new HashMap<>();

    public ChickenManager() {
        super(GSON, "chickens");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation location = entry.getKey();

            ChickenData.CODEC.decode(JsonOps.INSTANCE, entry.getValue()).get()
                    .ifLeft(chickenData -> chickens.put(location, chickenData.getFirst()))
                    .ifRight(error -> Chickens.LOGGER.error("Failed to parse {} chicken: {}", location, error.message()));
        }

        Chickens.LOGGER.debug("Chickens: {}", chickens);
    }

    public Map<ResourceLocation, ChickenData> getChickens() {
        return ImmutableMap.copyOf(chickens);
    }

    public @NotNull ChickenData getChickenData(ResourceLocation name) {
        return chickens.getOrDefault(name, ChickenData.EMPTY);
    }

    public List<ChickenHolder> getChickensForBiome(Holder<Biome> biome) {
        // TODO: THIS
        //getChickens().entrySet().stream().filter(entry -> {biome.is})
        return getChickens().entrySet().stream().map(entry -> new ChickenHolder(entry.getKey(), entry.getValue())).toList();
    }
}

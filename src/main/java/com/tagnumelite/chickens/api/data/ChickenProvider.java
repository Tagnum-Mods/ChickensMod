package com.tagnumelite.chickens.api.data;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public abstract class ChickenProvider extends JsonCodecProvider<ChickenData> {
    protected static final Logger LOGGER = LogUtils.getLogger();

    /**
     * @param dataGenerator      DataGenerator provided by {@link GatherDataEvent}.
     * @param existingFileHelper ExistingFileHelper provided by {@link GatherDataEvent}
     */
    public ChickenProvider(DataGenerator dataGenerator, String modid, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, existingFileHelper, modid, JsonOps.INSTANCE, PackType.SERVER_DATA, "chickens", ChickenData.CODEC, Collections.emptyMap());
    }

    @Override
    protected void gather(@NotNull BiConsumer<ResourceLocation, ChickenData> consumer) {
        AtomicBoolean failed = new AtomicBoolean(false);
        gatherChickens((id, data) -> {
            if (existingFileHelper.isEnabled()) {
                ResourceLocation textureFile = new ResourceLocation(id.getNamespace(), "textures/entity/chickens/" + id.getPath() + ".png");
                if (!existingFileHelper.exists(textureFile, PackType.CLIENT_RESOURCES)) {
                    LOGGER.error("Failed to find texture for entity {} at {}", id, textureFile);
                    failed.set(true);
                }
            }
            consumer.accept(id, data);
        });
        if (failed.get()) throw new IllegalStateException("Chicken Provider failed to verify all files exist");
    }

    protected abstract void gatherChickens(BiConsumer<ResourceLocation, ChickenData> consumer);

    protected ResourceLocation modRl(String name) {
        return new ResourceLocation(modid, name);
    }

    protected Builder chicken(String name) {
        return chicken(modRl(name));
    }

    protected Builder chicken(ResourceLocation id) {
        return new Builder(id);
    }

    protected Builder dyeChicken(String name, DyeColor color) {
        return dyeChicken(modRl(name), color);
    }

    protected Builder dyeChicken(ResourceLocation id, DyeColor color) {
        return chicken(id).primaryColor(color.getTextColor()).secondaryColor(color.getMaterialColor().col);
    }

    public static class Builder {
        private final ResourceLocation id;
        private final List<ItemStack> drops = new ArrayList<>();
        private final List<Holder<Biome>> supportedBiomes = new ArrayList<>();
        private int primaryColor = 0xFFFFFF;
        private int secondaryColor = 0xFFFFFF;
        private int tier = 1;

        public Builder(ResourceLocation id) {
            this.id = id;
        }

        public Builder color(int primaryColor, int secondaryColor) {
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            return this;
        }

        public Builder primaryColor(int primaryColor) {
            this.primaryColor = primaryColor;
            return this;
        }

        public Builder secondaryColor(int secondaryColor) {
            this.secondaryColor = secondaryColor;
            return this;
        }

        // TODO: Add drop chances
        public Builder addDrop(ItemLike drop) {
            return addDrop(new ItemStack(drop.asItem()));
        }

        public Builder addDrop(ItemStack stack) {
            drops.add(stack);
            return this;
        }

        public Builder addBiome(ResourceKey<Biome> biome) {
            return addBiome(ForgeRegistries.BIOMES.getHolder(biome).get());
        }

        public Builder addBiome(Biome biome) {
            supportedBiomes.add(Holder.direct(biome));
            return this;
        }

        public Builder addBiome(Holder<Biome> biome) {
            supportedBiomes.add(biome);
            return this;
        }

        public Builder tier(int tier) {
            this.tier = tier;
            return this;
        }

        // TODO: Biomes doesn't get added
        public Builder addBiomes(TagKey<Biome> biomeTag) {
            ForgeRegistries.BIOMES.tags().getTag(biomeTag).stream().map(Holder::direct).forEach(supportedBiomes::add);
            return this;
        }

        public ResourceLocation save(BiConsumer<ResourceLocation, ChickenData> consumer) {
            Chickens.LOGGER.debug("{} drops {}", id, drops);
            consumer.accept(id, new ChickenData(tier, primaryColor, secondaryColor, HolderSet.direct(supportedBiomes), drops));
            return id;
        }
    }
}

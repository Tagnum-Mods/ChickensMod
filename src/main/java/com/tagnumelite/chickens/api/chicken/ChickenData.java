package com.tagnumelite.chickens.api.chicken;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

import java.util.Collections;
import java.util.List;

import static com.tagnumelite.chickens.api.utils.constants.DefaultConstants.*;

public record ChickenData(int tier, int primaryColor, int secondaryColor, HolderSet<Biome> availableBiomes,
                          List<ItemStack> drops, int minLayTime, int maxLayTime) {
    public static final Codec<ChickenData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.optionalFieldOf("tier", DEFAULT_TIER).forGetter(ChickenData::tier),
                    Codec.INT.fieldOf("primaryColor").forGetter(ChickenData::primaryColor),
                    Codec.INT.fieldOf("secondaryColor").forGetter(ChickenData::secondaryColor),
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(ChickenData::availableBiomes),
                    ItemStack.CODEC.listOf().fieldOf("drops").forGetter(ChickenData::drops),
                    Codec.INT.optionalFieldOf("minLayTime", MIN_LAY_TIME).forGetter(ChickenData::minLayTime),
                    Codec.INT.optionalFieldOf("maxLayTime", MAX_LAY_TIME).forGetter(ChickenData::maxLayTime)
            ).apply(instance, ChickenData::new)
    );
    public static final ChickenData EMPTY = new ChickenData(DEFAULT_TIER, 0xFFFFFF, 0x000000, null, Collections.emptyList(), MIN_LAY_TIME, MIN_LAY_TIME);

    public ChickenData(int primaryColor, int secondaryColor, HolderSet.Direct<Biome> biomes, List<ItemStack> drops) {
        this(DEFAULT_TIER, primaryColor, secondaryColor, biomes, drops);
    }

    public ChickenData(int tier, int primaryColor, int secondaryColor, HolderSet.Direct<Biome> biomes, List<ItemStack> drops) {
        this(tier, primaryColor, secondaryColor, biomes, drops, MIN_LAY_TIME, MAX_LAY_TIME);
    }
}
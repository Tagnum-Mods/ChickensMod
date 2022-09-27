package com.tagnumelite.chickens.config;

import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // General
    public static final ForgeConfigSpec.IntValue SPAWN_PROBABILITY;
    public static final ForgeConfigSpec.IntValue MIN_BROOD_SIZE;
    public static final ForgeConfigSpec.IntValue MAX_BROOD_SIZE;
    public static final ForgeConfigSpec.DoubleValue NETHER_SPAWN_CHANCE;
    public static final ForgeConfigSpec.DoubleValue EGG_DROP_CHANGE;
    public static final ForgeConfigSpec.DoubleValue ITEM_DROP_CHANGE;

    // Debug
    public static final ForgeConfigSpec.BooleanValue DEBUG_DROPS;


    // Init
    static {
        BUILDER.comment("Config Options for Chickens");

        BUILDER.push("general");
        SPAWN_PROBABILITY = BUILDER.comment("Spawn probability")
                .translation(TranslationConstants.CONFIG_SPAWN_PROBABILITY)
                .defineInRange("spawnProbability", 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
        MIN_BROOD_SIZE = BUILDER.comment("Minimum Brood Size")
                .translation(TranslationConstants.CONFIG_BROOD_SIZE_MIN)
                .defineInRange("minBroodSize", 3, 1, Integer.MAX_VALUE);
        MAX_BROOD_SIZE = BUILDER.comment("Max Brood Size, must be greater than the minimal size")
                .translation(TranslationConstants.CONFIG_BROOD_SIZE_MAX)
                .defineInRange("minBroodSize", 5, 2, Integer.MAX_VALUE);
        NETHER_SPAWN_CHANCE = BUILDER.comment("Nether chicken spawn chance multiplier, e.g. 0=no initial spawn, 2=two times more spawn rate")
                .translation(TranslationConstants.CONFIG_NETHER_SPAWN_CHANCE)
                .defineInRange("netherSpawnChanceMultiplier", 1.0F, 0.0F, Double.MAX_VALUE);
        EGG_DROP_CHANGE = BUILDER.comment("Drop change for eggs")
                .translation(TranslationConstants.CONFIG_DROP_CHANCE_EGG)
                .defineInRange("eggDropChance", 0.125F, 0.001F, Double.MAX_VALUE);
        ITEM_DROP_CHANGE = BUILDER.comment("Drop chance for items")
                .translation(TranslationConstants.CONFIG_DROP_CHANCE_ITEM)
                .defineInRange("itemDropChance", 0.875F, 0.001F, Double.MAX_VALUE);
        BUILDER.pop();

        BUILDER.push("debug");
        DEBUG_DROPS = BUILDER.comment("Makes chickens drop items every 5 seconds").translation("test3")
                .define("debugDrops", false);
        BUILDER.pop();

        //BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

package com.tagnumelite.chickens.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // General
    public static final ForgeConfigSpec.BooleanValue ALWAYS_SHOW_STATS;

    static {
        BUILDER.push("Client config options for Chickens");

        BUILDER.push("general");
        ALWAYS_SHOW_STATS = BUILDER
                .comment("Stats will be always shown in WAILA without the need to analyze chickens first when enabled.")
                .translation("test3")
                .define("alwaysShowStats", false);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

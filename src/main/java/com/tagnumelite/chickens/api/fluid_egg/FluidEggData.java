package com.tagnumelite.chickens.api.fluid_egg;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;

import static com.tagnumelite.chickens.api.utils.constants.DefaultConstants.FLUID_PRIMARY_COLOR;
import static com.tagnumelite.chickens.api.utils.constants.DefaultConstants.FLUID_SECONDARY_COLOR;

public record FluidEggData(int primaryColor, int secondaryColor, ResourceLocation fluid, int capacity) {
    public static final Codec<FluidEggData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.optionalFieldOf("primaryColor", FLUID_PRIMARY_COLOR).forGetter(FluidEggData::primaryColor),
                    Codec.INT.optionalFieldOf("secondaryColor", FLUID_SECONDARY_COLOR).forGetter(FluidEggData::secondaryColor),
                    ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidEggData::fluid),
                    Codec.INT.optionalFieldOf("capacity", FluidType.BUCKET_VOLUME).forGetter(FluidEggData::capacity)
            ).apply(instance, FluidEggData::new)
    );
}

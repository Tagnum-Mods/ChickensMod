package com.tagnumelite.chickens.api.data;

import com.mojang.serialization.JsonOps;
import com.tagnumelite.chickens.api.fluid_egg.FluidEggData;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.BiConsumer;

public abstract class FluidEggProvider extends JsonCodecProvider<FluidEggData> {
    /**
     * @param dataGenerator      DataGenerator provided by {@link GatherDataEvent}.
     * @param modid              The modid of the files
     * @param existingFileHelper ExistingFileHelper provided by {@link GatherDataEvent}
     */
    public FluidEggProvider(DataGenerator dataGenerator, String modid, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, existingFileHelper, modid, JsonOps.INSTANCE, PackType.SERVER_DATA, "liquid_eggs", FluidEggData.CODEC, Collections.emptyMap());
    }

    @Override
    protected abstract void gather(@NotNull BiConsumer<ResourceLocation, FluidEggData> consumer);

    protected Builder newEgg(Fluid fluid) {
        return newEgg(fluid, 1000);
    }

    protected Builder newEgg(Fluid fluid, int capacity) {
        return new Builder(fluid, capacity);
    }

    public class Builder {
        private final Fluid fluid;
        private final int capacity;
        private int primaryColor = 0xFFFFFF;
        private int secondaryColor = 0xFFFFFF;

        public Builder(Fluid fluid, int capacity) {
            this.fluid = fluid;
            this.capacity = capacity;
        }

        public Builder primaryColor(int primaryColor) {
            this.primaryColor = primaryColor;
            return this;
        }

        public Builder secondaryColor(int secondaryColor) {
            this.secondaryColor = secondaryColor;
            return this;
        }

        public void save(ResourceLocation location, BiConsumer<ResourceLocation, FluidEggData> consumer) {
            consumer.accept(location, new FluidEggData(primaryColor, secondaryColor, ForgeRegistries.FLUIDS.getKey(fluid), capacity));
        }

        public void save(String name, BiConsumer<ResourceLocation, FluidEggData> consumer) {
            save(new ResourceLocation(modid, name), consumer);
        }
    }
}

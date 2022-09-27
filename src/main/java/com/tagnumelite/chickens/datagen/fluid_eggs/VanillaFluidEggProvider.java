package com.tagnumelite.chickens.datagen.fluid_eggs;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.data.FluidEggProvider;
import com.tagnumelite.chickens.api.fluid_egg.FluidEggData;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class VanillaFluidEggProvider extends FluidEggProvider {
    public VanillaFluidEggProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Chickens.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gather(@NotNull BiConsumer<ResourceLocation, FluidEggData> consumer) {
        newEgg(Fluids.WATER).primaryColor(0x0000ff).secondaryColor(0x0400f2).save("water", consumer);
        newEgg(Fluids.LAVA).primaryColor(0xff0000).secondaryColor(0xff6600).save("lava", consumer);
    }
}

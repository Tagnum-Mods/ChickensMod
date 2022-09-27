package com.tagnumelite.chickens.api.capability;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.utils.Utils;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Created by setyc on 13.12.2016.
 */
public class FluidEggWrapper implements IFluidHandlerItem, ICapabilityProvider {
    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);
    private ItemStack container;
    private Fluid fluid;

    public FluidEggWrapper(ItemStack container) {
        this.container = container;
        ResourceLocation fluidId = Utils.getFluidFromStack(container);
        this.fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return container;
    }

    private FluidStack getFluid() {
        return new FluidStack(fluid, FluidType.BUCKET_VOLUME);
    }

    protected void setFluid(@NotNull FluidStack fluidStack) {
        container = Chickens.getLiquidEggManager().getEggFromFluid(fluidStack);
        fluid = fluidStack.getFluid();
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return FluidType.BUCKET_VOLUME;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return stack.getFluid() == fluid;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty() && resource.isFluidEqual(fluidStack)) {
            if (action.execute()) {
                setFluid(FluidStack.EMPTY);
            }
            return drain(resource.getAmount(), action);
        }

        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        if (container.getMaxStackSize() < 1 || maxDrain < FluidType.BUCKET_VOLUME) {
            return FluidStack.EMPTY;
        }

        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty()) {
            // TODO
            if (action.execute()) {
                setFluid(FluidStack.EMPTY); // TODO: Make sure we reduce item count
            }
            return fluidStack;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, holder);
    }
}

package com.tagnumelite.chickens.proxy;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.fluid_egg.FluidEggData;
import com.tagnumelite.chickens.common.entities.ThrownColoredEgg;
import com.tagnumelite.chickens.common.items.FluidEggItem;
import com.tagnumelite.chickens.common.items.ModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

/**
 * Created by setyc on 18.02.2016.
 */
public class CommonProxy {
    public void registerItemHandlers() {
        DispenserBlock.registerBehavior(ModItems.FLUID_EGG.get(), new DispenseLiquidEgg());
        DispenserBlock.registerBehavior(ModItems.COLORED_EGG.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected @NotNull Projectile getProjectile(@NotNull Level pLevel, @NotNull Position pPosition, @NotNull ItemStack pStack) {
                return Util.make(new ThrownColoredEgg(pLevel, pPosition.x(), pPosition.y(), pPosition.z()), (thrownColoredEgg -> {
                    thrownColoredEgg.setItem(pStack);
                }));
            }
        });
    }

    static class DispenseLiquidEgg implements DispenseItemBehavior {
        @Override
        public @NotNull ItemStack dispense(BlockSource source, ItemStack stack) {
            FluidEggItem fluidEggItem = (FluidEggItem) stack.getItem();
            BlockPos blockPos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
            FluidEggData liquid = Chickens.getLiquidEggManager().getEggData(new ResourceLocation(stack.getTag().getString("liquid")));
            if (!fluidEggItem.tryPlaceContainedLiquid(null, source.getLevel(), blockPos, ForgeRegistries.BLOCKS.getValue(liquid.fluid()))) {
                //return super.dispense(source, stack);
            }
            stack.setCount(stack.getCount() - 1);
            return stack;
        }
    }
}

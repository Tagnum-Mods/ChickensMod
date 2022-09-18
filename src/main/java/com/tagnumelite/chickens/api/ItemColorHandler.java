package com.tagnumelite.chickens.api;

import com.tagnumelite.chickens.Chickens;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

/**
 * Created by setyc on 25.03.2016.
 */
public class ItemColorHandler implements ItemColor {
    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        if (itemStack.getItem() instanceof IColorSource colorSource)
            return colorSource.getColorFromItemStack(itemStack, tintIndex);

        Chickens.LOGGER.warn("ItemColorHandler was using by an unsupported item {} ({})", itemStack, itemStack.getItem());
        return 0xFFFFFF;
    }
}

package com.tagnumelite.chickens.common.items;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.utils.TranslateUtils;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.entities.ThrownColoredEgg;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by setyc on 13.02.2016.
 */
public class ColoredEggItem extends EggItem implements ItemColor {
    // TODO: Setup colored eggs
    public ColoredEggItem(Properties itemProperties) {
        super(itemProperties);
        //setHasSubtypes(true);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack item, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(item, level, components, isAdvanced);
        components.add(TranslateUtils.CGUI("colored_egg.tooltip"));
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        ResourceLocation type = Utils.getTypeFromTag(stack.getTag());

        int colorId = DyeColor.BLACK.getId();
        if (stack.getTag() != null) {
            colorId = stack.getTag().getInt("color");
        }
        DyeColor color = DyeColor.byId(colorId);

        return Component.translatable(getDescriptionId(stack) + "." + color.getSerializedName());
    }


    @Override
    public void fillItemCategory(@NotNull CreativeModeTab category, @NotNull NonNullList<ItemStack> subItems) {
        if (allowedIn(category)) {
            for (ResourceLocation id : Chickens.getChickenManager().getChickens().keySet()) {
                //CompoundTag compoundTag = new CompoundTag();
                //compoundTag.putString("name", entry.getKey().toString()); // TODO: Replace CompoundTag with actual NBT
                //compoundTag.putInt("color", DyeColor.GREEN.getId());
                subItems.add(Utils.itemWithType(this, id));
            }
            //Chickens.LOGGER.debug("Category {} contains {}", category, subItems);
        }
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        int color = stack.getTag() != null ? stack.getTag().getInt("color") : DyeColor.BLUE.getId();
        return DyeColor.byId(color).getTextColor();
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (!player.isCreative()) {
            //--itemInHand.stackSize; TODO
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            ResourceLocation chickenType = Utils.getTypeFromStack(itemInHand);
            if (!chickenType.getPath().equals("")) {
                ThrownColoredEgg entityIn = new ThrownColoredEgg(level, player);
                entityIn.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                entityIn.setChickenType(chickenType);
                level.addFreshEntity(entityIn);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemInHand);
    }
}

package com.tagnumelite.chickens.common.items;

import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by setyc on 21.12.2016.
 */
public class AnalyzerItem extends Item {
    public AnalyzerItem(Properties itemProperties) {
        super(itemProperties.stacksTo(1).defaultDurability(238));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.translatable(TranslationConstants.ANALYZER_TOOLTIP_1));
        tooltipComponents.add(Component.translatable(TranslationConstants.ANALYZER_TOOLTIP_2));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player playerIn, LivingEntity target, @NotNull InteractionHand hand) {
        if (target.level.isClientSide || !(target instanceof ChickensChicken chicken)) {
            return InteractionResult.SUCCESS;
        }

        chicken.setStatsAnalyzed(true);

        MutableComponent chickenName = chicken.getName().copy();
        chickenName.setStyle(chickenName.getStyle().applyFormats(ChatFormatting.BOLD, ChatFormatting.GOLD));
        // TODO: Replace literal with translatable
        playerIn.sendSystemMessage(Component.literal("Name: ").append(chickenName));

        playerIn.sendSystemMessage(Component.literal("- ").append(Component.translatable(TranslationConstants.CHICKEN_STATS_TIER, chicken.getTier())));
        playerIn.sendSystemMessage(Component.literal("- ").append(Component.translatable(TranslationConstants.CHICKEN_STATS_GROWTH, chicken.getGrowth())));
        playerIn.sendSystemMessage(Component.literal("- ").append(Component.translatable(TranslationConstants.CHICKEN_STATS_GAIN, chicken.getGain())));
        playerIn.sendSystemMessage(Component.literal("- ").append(Component.translatable(TranslationConstants.CHICKEN_STATS_STRENGTH, chicken.getStrength())));

        if (!chicken.isBaby()) {
            int layProgress = chicken.getLayProgress();
            if (layProgress <= 0) {
                playerIn.sendSystemMessage(Component.translatable(TranslationConstants.CHICKEN_NEXT_EGG_SOON));
            } else {
                playerIn.sendSystemMessage(Component.translatable(TranslationConstants.CHICKEN_LAY_PROGRESS, layProgress));
            }
        }

        damageItem(stack, 1, target, (LivingEntity entity) -> {
            entity.playSound(SoundEvents.CHICKEN_AMBIENT);
        });
        return InteractionResult.CONSUME;
    }
}

package com.tagnumelite.chickens.integration.jade;

import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import com.tagnumelite.chickens.config.ClientConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ChickensEntityProvider implements IEntityComponentProvider {
    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getEntity() instanceof ChickensChicken chicken) {
            iTooltip.add(Component.translatable(TranslationConstants.CHICKEN_STATS_TIER, chicken.getTier()));

            if (chicken.getStatsAnalyzed() || ClientConfig.ALWAYS_SHOW_STATS.get()) {
                iTooltip.add(Component.translatable(TranslationConstants.CHICKEN_STATS_GROWTH, chicken.getGrowth()));
                iTooltip.add(Component.translatable(TranslationConstants.CHICKEN_STATS_GAIN, chicken.getGain()));
                iTooltip.add(Component.translatable(TranslationConstants.CHICKEN_STATS_STRENGTH, chicken.getStrength()));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Utils.rl("chicken_provider");
    }
}

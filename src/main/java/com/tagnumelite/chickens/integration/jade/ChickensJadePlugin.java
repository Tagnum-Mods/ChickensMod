package com.tagnumelite.chickens.integration.jade;

import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import com.tagnumelite.chickens.config.ClientConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

/**
 * Created by setyc on 20.02.2016.
 */
@WailaPlugin
public class ChickensJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(new ChickensEntityProvider(), ChickensChicken.class);
    }

    public static class ChickensEntityProvider implements IEntityComponentProvider {
        @Override
        public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
            if (entityAccessor.getEntity() instanceof ChickensChicken chicken) {
                iTooltip.add(Component.translatable("entity.chickens_chicken.tier", chicken.getTier()));

                if (chicken.getStatsAnalyzed() || ClientConfig.ALWAYS_SHOW_STATS.get()) {
                    iTooltip.add(Component.translatable("entity.ChickensChicken.growth", chicken.getGrowth()));
                    iTooltip.add(Component.translatable("entity.ChickensChicken.gain", chicken.getGain()));
                    iTooltip.add(Component.translatable("entity.ChickensChicken.strength", chicken.getStrength()));
                }

                if (!chicken.isBaby()) {
                    int layProgress = chicken.getLayProgress();
                    if (layProgress <= 0) {
                        iTooltip.add(Component.translatable("entity.ChickensChicken.nextEggSoon"));
                    } else {
                        iTooltip.add(Component.translatable("entity.ChickensChicken.layProgress", layProgress));
                    }
                }
            }
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.rl("chicken_provider");
        }
    }
}
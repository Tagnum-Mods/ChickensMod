package com.tagnumelite.chickens.integration.jade;

import com.tagnumelite.chickens.common.blocks.CoopBlock;
import com.tagnumelite.chickens.common.blocks.entities.CoopBlockEntity;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import snownee.jade.api.*;

/**
 * Created by setyc on 20.02.2016.
 */
@WailaPlugin
public class ChickensWailaPlugin implements IWailaPlugin {


    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(new ChickensEntityProvider(), ChickensChicken.class);
        registration.registerBlockComponent(ChickensCoopProvider.INSTANCE, CoopBlock.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ChickensCoopProvider.INSTANCE, CoopBlockEntity.class);
    }
}
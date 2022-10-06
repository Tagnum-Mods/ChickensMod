package com.tagnumelite.chickens.integration.top;

import com.tagnumelite.chickens.Chickens;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ChickensTOPPlugin implements Function<ITheOneProbe, Void> {
    @Nullable
    @Override
    public Void apply(ITheOneProbe registration) {
        Chickens.LOGGER.info("TOP Support for Chickens is enabled");
        //registration.registerProvider(new InfoProvider());
        //registration.registerBlockDisplayOverride(new ChickensCoopProvider());
        //registration.registerEntityProvider(new ChickenEntityProvider());
        return null;
    }
}

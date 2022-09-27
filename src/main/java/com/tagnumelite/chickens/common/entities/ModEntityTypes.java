package com.tagnumelite.chickens.common.entities;

import com.tagnumelite.chickens.Chickens;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Chickens.MOD_ID);

    public static final RegistryObject<EntityType<ChickensChicken>> CHICKEN = ENTITY_TYPES.register("chicken", () ->
            EntityType.Builder.<ChickensChicken>of(ChickensChicken::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.7f).clientTrackingRange(10).build("chicken"));

    public static final RegistryObject<EntityType<ThrownColoredEgg>> COLORED_EGG = ENTITY_TYPES.register("colored_egg", () ->
            EntityType.Builder.<ThrownColoredEgg>of(ThrownColoredEgg::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4).updateInterval(10).build("colored_egg"));
}

package com.tagnumelite.chickens.client.renderers;

import com.tagnumelite.chickens.common.entities.ChickensChicken;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

/**
 * Created by setyc on 12.02.2016.
 */
public class ChickensChickenRenderer extends MobRenderer<ChickensChicken, ChickenModel<ChickensChicken>> {
    public ChickensChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ChickensChicken chicken) {
        // TODO: This is probably really inefficient, must replace
        ResourceLocation id = new ResourceLocation(chicken.getEntityData().get(ChickensChicken.CHICKEN_TYPE));
        return new ResourceLocation(id.getNamespace(), "textures/entity/chickens/" + id.getPath() + ".png");
    }

    @Override
    protected float getBob(@NotNull ChickensChicken pLivingBase, float pPartialTick) {
        float f = Mth.lerp(pPartialTick, pLivingBase.oFlap, pLivingBase.flap);
        float f1 = Mth.lerp(pPartialTick, pLivingBase.oFlapSpeed, pLivingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}

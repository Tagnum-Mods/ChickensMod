package com.tagnumelite.chickens.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tagnumelite.chickens.common.blocks.entities.CoopBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

// TODO: Figure out if this is efficient enough or requires a different method
public class CoopBlockRenderer implements BlockEntityRenderer<CoopBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;

    public CoopBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(@NotNull CoopBlockEntity coop, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);
        Entity entity = coop.getDisplayChicken();

        if (entity != null) {
            float f = 0.8F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) f1 > 1.0D) {
                f /= f1;
            }

            poseStack.translate(0.0D, 0.1F, 0.0D);
            poseStack.scale(f, f, f);
            this.entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTick, poseStack, bufferSource, 255);
        }

        poseStack.popPose();
    }
}

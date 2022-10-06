package com.tagnumelite.chickens.integration.jade;

import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.blocks.entities.CoopBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.BorderStyle;
import snownee.jade.impl.ui.ProgressElement;
import snownee.jade.impl.ui.ProgressStyle;

enum ChickensCoopProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
    INSTANCE;

    static final ProgressStyle PROGRESS_STYLE = (ProgressStyle) new ProgressStyle().color(0xFF0000, 0x0000FF).textColor(0x00FF00);
    static final BorderStyle BORDER_STYLE = (BorderStyle) new BorderStyle().width(10).color(0x000000);

    @Override
    public void appendTooltip(ITooltip toolTips, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof CoopBlockEntity) {
            // TODO: Setup a propper progress element
            toolTips.add(new ProgressElement((float) blockAccessor.getServerData().getInt("Progress") / 100, Component.literal("Progress: " + blockAccessor.getServerData().getInt("Progress") + "%"), (ProgressStyle) PROGRESS_STYLE, BORDER_STYLE));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Utils.rl("coop");
    }

    @Override
    public void appendServerData(CompoundTag tag, ServerPlayer player, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof CoopBlockEntity coop) {
            tag.putInt("Progress", coop.getProgress());
        }
    }
}

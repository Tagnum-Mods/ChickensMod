package com.tagnumelite.chickens.network.message;

import com.tagnumelite.chickens.api.ISyncableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 *
 */
public class UpdateClientEntity {
    public final BlockPos pos;
    public final CompoundTag data;

    /**
     *
     * @param entity
     */
    public UpdateClientEntity(BlockEntity entity) {
        this(entity.getBlockPos(), entity.getUpdateTag());
    }

    /**
     *
     * @param pos
     * @param data
     */
    public UpdateClientEntity(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }

    /**
     *
     * @param buffer
     */
    public UpdateClientEntity(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        data = buffer.readNbt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeNbt(data);
    }

    /**
     *
     * @param contextSupplier
     */
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(pos);
            if (blockEntity instanceof ISyncableBlockEntity sync) {
                sync.onSyncPacket(data);
            }
        });
        context.setPacketHandled(true);
    }
}

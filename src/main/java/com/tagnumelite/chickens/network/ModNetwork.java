package com.tagnumelite.chickens.network;

import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.network.message.UpdateClientEntity;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;

public class ModNetwork {
    public static final ResourceLocation CHANNEL_NAME = Utils.rl("main");
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(CHANNEL_NAME, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private static int idx = 0;

    public static void register() {
        CHANNEL.registerMessage(idx++, UpdateClientEntity.class, UpdateClientEntity::toBytes, UpdateClientEntity::new, UpdateClientEntity::handle);
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToBlockEntity(MSG message, BlockEntity blockEntity) {
        sendToAllTracking(message, blockEntity.getLevel(), blockEntity.getBlockPos());
    }

    private static <MSG> void sendToAllTracking(MSG message, Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> sendToPlayer(message, p));
        } else {
            PacketDistributor.PacketTarget thing = PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())));
            thing.send(thing.getDirection().buildPacket(toBuffer(message), CHANNEL_NAME).getThis());
        }
    }

    private static <MSG> Pair<FriendlyByteBuf, Integer> toBuffer(MSG message) {
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        int index = CHANNEL.encodeMessage(message, buffer);
        return Pair.of(buffer, index);
    }
}

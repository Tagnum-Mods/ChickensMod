package com.tagnumelite.chickens.api;

import net.minecraft.nbt.CompoundTag;

public interface ISyncableBlockEntity {
    void onSyncPacket(CompoundTag data);
}

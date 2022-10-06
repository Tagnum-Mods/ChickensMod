package com.tagnumelite.chickens.common.blocks.entities;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.ISyncableBlockEntity;
import com.tagnumelite.chickens.api.chicken.ChickenData;
import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.client.menus.CoopMenu;
import com.tagnumelite.chickens.common.blocks.ModBlockEntityTypes;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import com.tagnumelite.chickens.network.ModNetwork;
import com.tagnumelite.chickens.network.message.UpdateClientEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// TODO: Refactor and refine
public class CoopBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, ISyncableBlockEntity {
    public static final int SLOT_INPUT = 0;
    public static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT};
    public static final int[] SLOTS_FOR_OTHER = new int[]{1, 2, 3, 4, 5};
    public static final int DATA_PROGRESS = 0;
    static final Component CONTAINER_TITLE = TranslationUtils.CGUI_CONTAINER("coop");

    protected NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    int progress;
    int timeElapsed = 0;
    int timeUntilNextLay = 0;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return index == DATA_PROGRESS ? CoopBlockEntity.this.progress : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == DATA_PROGRESS) CoopBlockEntity.this.progress = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    final List<ItemStack> DROP_CACHE = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    private ResourceLocation prevChicken;
    @OnlyIn(Dist.CLIENT)
    @Nullable
    private Entity displayEntity;

    public CoopBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.COOP.get(), pPos, pBlockState);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return CONTAINER_TITLE;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory playerInventory) {
        return new CoopMenu(pContainerId, playerInventory, this, this.dataAccess);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt("Progress");
        this.timeElapsed = tag.getInt("TimeElapsed");
        this.timeUntilNextLay = tag.getInt("TimeUntilNextLay");
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", this.progress);
        tag.putInt("TimeElapsed", this.timeElapsed);
        tag.putInt("TimeUntilNextLay", this.timeUntilNextLay);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction face) {
        return face == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_OTHER;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, @Nullable Direction face) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction face) {
        return true; // TODO: Make sure this is okay
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack newStack) {
        ItemStack currentStack = this.items.get(slot);
        boolean newStackNotEmptyAndEqual = !newStack.isEmpty() && newStack.sameItem(currentStack) && ItemStack.tagMatches(newStack, currentStack); // TODO: Make sure we want tagMatches
        this.items.set(slot, newStack);
        if (newStack.getCount() > this.getMaxStackSize()) {
            newStack.setCount(this.getMaxStackSize());
        }

        if (slot == SLOT_INPUT && !newStackNotEmptyAndEqual) {
            DROP_CACHE.clear();
            ChickenData chickenData = getChickenData(getChickenType());
            this.timeUntilNextLay = chickenData.getRandomLayTime(level.random);
            this.progress = 0;
            this.setChanged();

            DROP_CACHE.addAll(chickenData.drops());
        } else if(slot == SLOT_INPUT && newStack.isEmpty()) {
            DROP_CACHE.clear();
            this.progress = 0;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean canPlaceItem(int pIndex, @NotNull ItemStack stack) { // TODO: THIS
        if (pIndex == 2) {
            return false;
        } else if (pIndex != 1) {
            return true;
        } else {
            ItemStack itemstack = this.items.get(1);
            return stack.is(Items.BUCKET) && !itemstack.is(Items.BUCKET);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemStacks) {
            this.items = itemStacks;
    }

    @Override
    public void fillStackedContents(@NotNull StackedContents stackHelper) {
        for (ItemStack itemstack : this.items) {
            stackHelper.accountStack(itemstack);
        }
    }

    //@OnlyIn(Dist.DEDICATED_SERVER)
    @Nullable
    public ResourceLocation getChickenType() {
        ItemStack spawnEgg = getInput();
        if (spawnEgg.isEmpty()) return null;
        return Utils.getTypeFromStack(spawnEgg);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        if (getChickenType() != null) {
            tag.putString("Type", getChickenType().toString());
        } else {
            tag.putString("Type", "");
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        onSyncPacket(tag);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public Entity getDisplayChicken() {
        if (prevChicken == null || prevChicken.toString().equals("")) return null;
        if (this.displayEntity == null) {
            this.displayEntity = EntityType.loadEntityRecursive(Util.make(new CompoundTag(), tag -> {
                tag.putString("id", "chickens:chicken");
                tag.putString(ChickensChicken.TYPE_NBT, prevChicken.toString());
            }), this.level, Function.identity());
        }

        return this.displayEntity;
    }

    public void tickServer() { // TODO: Resource Generation
        @Nullable ResourceLocation currentChicken = getChickenType();
        if (!Utils.compareRL(currentChicken, prevChicken)) {
            prevChicken = currentChicken;
            sendUpdatePacket();
        }
        updateProgress();
        updateTimer();
        addPotentialDrops();
    }

    private static ChickenData getChickenData(ResourceLocation chickenId) {
        return Chickens.getChickenManager().getChickenData(chickenId);
    }

    private void updateTimer() {
        if (prevChicken != null && !outputIsFull()) {
            timeElapsed += 1;
            setChanged();
        }
    }

    private void resetTimer() {
        @NotNull ChickenData chickenData = getChickenData(prevChicken);
        timeElapsed = 0;
        timeUntilNextLay = chickenData.getRandomLayTime(level.random);
        setChanged();
    }

    private void addPotentialDrops() {
        if (timeElapsed >= timeUntilNextLay) {
            if (level != null && addDrops()) {
                level.playSound(null, getBlockPos(), SoundEvents.CHICKEN_EGG, SoundSource.NEUTRAL, 0.5F, 0.8F);
                // TODO SPAWN PARTICALS ANd maybe anything else I want
            }
            resetTimer();
        }
    }

    private boolean addDrops() {
        if(prevChicken == null) return false;
        if (DROP_CACHE.isEmpty()) DROP_CACHE.addAll(getChickenData(getChickenType()).drops());
        //final int gain = getInput().getOrCreateTag().getInt("ChickenGain"); TODO: Implement gain
        List<ItemStack> itemDrops = Utils.copyItemStacks(DROP_CACHE);
        boolean ret = false;

        for (ItemStack drop : itemDrops) {
            for (int slotIdx = 1; slotIdx < items.size(); slotIdx++) {
                if (drop.isEmpty()) break;

                ItemStack slot = items.get(slotIdx);
                int slotCount = slot.getCount();
                int slotCountMax = slot.getMaxStackSize();
                //Chickens.LOGGER.debug("Chicken: {}; slot {}: {}; slotCount: {}/{}; drop: {}", prevChicken, slotIdx, slot, slotCount, slotCountMax, drop);
                if (slot.isEmpty() || (slotCount < slotCountMax && slot.is(drop.getItem()))) {
                    ItemStack layItem = drop;
                    if (!slot.isEmpty()) { // TODO: Make sure this works
                        int remainder = slotCountMax - slotCount;
                        int count = Math.min(drop.getCount(), remainder);
                        //Chickens.LOGGER.debug("Chicken: {}; remainder {}; count: {}", prevChicken, remainder, count);
                        drop.shrink(count);

                        layItem = slot;
                        slot.grow(count);
                    }
                    //layItem.setCount(layItem.getCount() * gain);
                    items.set(slotIdx, layItem.copy());
                    layItem.setCount(0);
                    ret =  true;
                }
            }
        }
        return ret;
    }

    public ItemStack getInput() {
        return items.get(SLOT_INPUT);
    }

    private void updateProgress() {
        if (prevChicken != null && timeElapsed != 0 && timeUntilNextLay != 0) {
            this.progress = (int) (100 * Math.max((float) timeElapsed / timeUntilNextLay, 0));
        } else {
            this.progress = 0;
        }
    }

    public int getProgress() {
        return progress;
    }

    public boolean outputIsFull() {
        for (int i = 1; i < items.size(); i++) {
            ItemStack item = items.get(i);
            if (item.isEmpty() || item.getCount() < item.getMaxStackSize()) return false;
        }
        return true;
    }

    private void sendUpdatePacket() {
        ModNetwork.sendToBlockEntity(new UpdateClientEntity(this), this);
    }

    @Override
    public void onSyncPacket(CompoundTag data) {
        if (data.contains("Type")) {
            String type = data.getString("Type");
            prevChicken = type.equals("") ? null : new ResourceLocation(type);
            displayEntity = null;
        }
    }

    public boolean hasChicken() {
        return getChickenType() != null;
    }
}

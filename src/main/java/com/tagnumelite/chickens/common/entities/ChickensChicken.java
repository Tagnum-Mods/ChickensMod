package com.tagnumelite.chickens.common.entities;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenData;
import com.tagnumelite.chickens.api.chicken.ChickenHolder;
import com.tagnumelite.chickens.api.recipe.IBreedingRecipe;
import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.items.ModItems;
import com.tagnumelite.chickens.config.ServerConfig;
import com.tagnumelite.chickens.crafting.ModRecipeTypes;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by setyc on 12.02.2016.
 */
public class ChickensChicken extends Chicken implements IEntityAdditionalSpawnData, IProbeInfoEntityAccessor {
    public static final EntityDataAccessor<String> CHICKEN_TYPE = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> CHICKEN_STATS_ANALYZED = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> CHICKEN_GROWTH = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> CHICKEN_GAIN = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> CHICKEN_STRENGTH = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> LAY_PROGRESS = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);

    public static final String TYPE_NBT = "Type";
    public static final String CHICKEN_STATS_ANALYZED_NBT = "Analyzed";
    public static final String CHICKEN_GROWTH_NBT = "Growth";
    public static final String CHICKEN_GAIN_NBT = "Gain";
    public static final String CHICKEN_STRENGTH_NBT = "Strength";
    private int totalEggTime;

    public ChickensChicken(Level level) {
        this(ModEntityTypes.CHICKEN.get(), level);
    }

    public ChickensChicken(EntityType<? extends ChickensChicken> entityType, Level level) {
        super(entityType, level);
    }

    // Stats

    private static void inheritStats(ChickensChicken offspring, ChickensChicken parent) {
        offspring.setGrowth(parent.getGrowth());
        offspring.setGain(parent.getGain());
        offspring.setStrength(parent.getStrength());
    }

    private static void increaseStats(ChickensChicken offspring, ChickensChicken parent1, ChickensChicken parent2, RandomSource rand) {
        int parent1Strength = parent1.getStrength();
        int parent2Strength = parent2.getStrength();
        offspring.setGrowth(calculateNewStat(parent1Strength, parent2Strength, parent1.getGrowth(), parent2.getGrowth(), rand));
        offspring.setGain(calculateNewStat(parent1Strength, parent2Strength, parent2.getGain(), parent2.getGain(), rand));
        offspring.setStrength(calculateNewStat(parent1Strength, parent2Strength, parent1Strength, parent2Strength, rand));
    }

    private static int calculateNewStat(int thisStrength, int mateStrength, int stat1, int stat2, RandomSource rand) {
        int mutation = rand.nextInt(2) + 1;
        int newStatValue = (stat1 * thisStrength + stat2 * mateStrength) / (thisStrength + mateStrength) + mutation;
        if (newStatValue <= 1) return 1;
        return Math.min(newStatValue, 10);
    }

    public static boolean canSpawn(EntityType<ChickensChicken> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        return checkAnimalSpawnRules(entityType, level, spawnType, blockPos, random);
        //return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHICKEN_TYPE, "");
        this.entityData.define(CHICKEN_STATS_ANALYZED, false);
        this.entityData.define(CHICKEN_GROWTH, 1);
        this.entityData.define(CHICKEN_GAIN, 1);
        this.entityData.define(CHICKEN_STRENGTH, 1);
        this.entityData.define(LAY_PROGRESS, 0);
    }

    public boolean getStatsAnalyzed() {
        return entityData.get(CHICKEN_STATS_ANALYZED);
    }

    public void setStatsAnalyzed(boolean statsAnalyzed) {
        entityData.set(CHICKEN_STATS_ANALYZED, statsAnalyzed);
    }

    public int getGain() {
        return entityData.get(CHICKEN_GAIN);
    }

    private void setGain(int gain) {
        entityData.set(CHICKEN_GAIN, gain);
    }

    public int getGrowth() {
        return entityData.get(CHICKEN_GROWTH);
    }

    private void setGrowth(int growth) {
        entityData.set(CHICKEN_GROWTH, growth);
    }

    public int getStrength() {
        return entityData.get(CHICKEN_STRENGTH);
    }

    private void setStrength(int strength) {
        entityData.set(CHICKEN_STRENGTH, strength);
    }

    // Other

    public ResourceLocation getChickenType() {
        return new ResourceLocation(entityData.get(CHICKEN_TYPE));
    }

    // Spawning & Offspring

    public void setChickenType(ResourceLocation type) {
        entityData.set(CHICKEN_TYPE, type.toString());
        //isImmuneToFire = getChickenDescription().isImmuneToFire();
        resetTimeUntilNextEgg();
    }

    public ChickenData getChickenData() {
        return Chickens.getChickenManager().getChickenData(getChickenType());
    }

    public int getTier() {
        return getChickenData().tier();
    }

    @Override
    public @NotNull Component getName() {
        if (this.hasCustomName()) {
            return super.getName();
        }

        return TranslationUtils.CHICKEN_NAME(getChickenType());
    }

    @Override
    public Chicken getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        ChickensChicken mateChicken = (ChickensChicken) mate;

        SimpleContainer container = new SimpleContainer(2);
        container.addItem(ModItems.SPAWN_EGG.get().fromChickenType(getChickenType()));
        container.addItem(ModItems.SPAWN_EGG.get().fromChickenType(mateChicken.getChickenType()));
        List<IBreedingRecipe> breeding = level.getRecipeManager().getRecipesFor(ModRecipeTypes.BREEDING.get(), container, level);

        ResourceLocation offSpringType = getChickenType();
        boolean equalParents = getChickenType().equals(mateChicken.getChickenType());
        if (!equalParents) {
            List<ResourceLocation> choices = new ArrayList<>(2);
            choices.add(getChickenType());
            choices.add(mateChicken.getChickenType());
            breeding.forEach(recipe -> choices.add(Utils.getTypeFromStack(recipe.getResultItem())));

            offSpringType = Utils.randChoice(choices);
        }

        ChickensChicken offspring = ModEntityTypes.CHICKEN.get().create(level, Utils.typeTag(offSpringType), null, null, getOnPos(), MobSpawnType.BREEDING, true, true);

        if (offspring != null) {
            offspring.setChickenType(offSpringType); // Double Ensure because that's who I am
            boolean mutatingStats = equalParents && offspring.getChickenType().equals(mateChicken.getChickenType());
            if (mutatingStats) {
                // Mutate stats as parents are the same
                increaseStats(offspring, this, mateChicken, random);
            } else if (getChickenType() == offspring.getChickenType()) {
                // Child inherits this parents traits as they are the same
                inheritStats(offspring, this);
            } else if (mateChicken.getChickenType() == offspring.getChickenType()) {
                // Offspring inherits mates traits as they are the same
                inheritStats(offspring, mateChicken);
            }
        }

        return offspring;
    }

    @Override
    public void aiStep() {
        // We must first playAround with eggTime ourselves instead of the Chicken class
        updateLayProgress(); // TODO: Some drops aren't dropping
        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && this.eggTime - 1 <= 0) {
            // TODO: Implement Henhouses Properly
            //drops = HenhouseBlockEntity.pushItemStack(drops, level, new Vec3(getX(), getY(), getZ()));

            ItemStack spawnEgg = ModItems.SPAWN_EGG.get().fromChickenType(getChickenType());

            if (spawnEgg != null) {
                ItemStack drop = Utils.getStackWithChance(spawnEgg, ServerConfig.EGG_DROP_CHANGE.get());
                int gain = getGain();
                if (gain >= 5) {
                    drop.setCount(drop.getCount() + 1);
                }
                if (gain >= 10) {
                    drop.setCount(drop.getCount() + 1);
                }
                if (!drop.isEmpty()) {
                    dropItem(drop);
                    playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }
            }
            List<ItemStack> drops = new ArrayList<>(getChickenData().drops());
            if (drops.size() > 0) {
                Double dropChance = ServerConfig.ITEM_DROP_CHANGE.get();
                for (ItemStack drop : drops) {
                    if (!Utils.getStackWithChance(drop, dropChance).isEmpty()) {
                        dropItem(drop.copy());
                    }
                }
                playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            resetTimeUntilNextEgg();
        }
        super.aiStep();
    }

    private void dropItem(ItemStack itemStack) {
        spawnAtLocation(itemStack);
        gameEvent(GameEvent.ENTITY_PLACE);
    }

    private void setTimeUntilNextEgg(int value) {
        eggTime = value;
        totalEggTime = value;
        updateLayProgress();
    }

    public int getLayProgress() {
        return entityData.get(LAY_PROGRESS);
    }

    private void updateLayProgress() {
        entityData.set(LAY_PROGRESS, eggTime / 60 / 20 / 2);
    }

    private void resetTimeUntilNextEgg() {
        setTimeUntilNextEgg(getChickenData().getRandomLayTime(random));
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return ModItems.SPAWN_EGG.get().fromChickenType(getChickenType());
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag) {
        SpawnGroupData livingData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (livingData instanceof GroupData groupData) {
            setChickenType(groupData.chickenID());
        } else {
            if (pReason.equals(MobSpawnType.SPAWNER)) {
                setChickenType(Utils.getTypeFromTag(pDataTag));
                // TODO: This
            } else if (pReason.equals(MobSpawnType.SPAWN_EGG)) {
                setChickenType(Utils.getTypeFromTag(pDataTag));
            } else {
                List<ChickenHolder> possibleChickens = Chickens.getChickenManager().getChickensForBiome(pLevel.getBiome(blockPosition()));
                if (possibleChickens.size() > 0) {
                    ChickenHolder chickenToSpawn = possibleChickens.get(random.nextInt(possibleChickens.size()));

                    ResourceLocation chickenId = chickenToSpawn.id();
                    setChickenType(chickenId);
                    livingData = new GroupData(chickenId);
                } else {
                    Chickens.LOGGER.error("");
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }

        if (random.nextInt(5) == 0) {
            setAge(-24000);
        }

        return livingData;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putString(TYPE_NBT, getChickenType().toString());
        tagCompound.putBoolean(CHICKEN_STATS_ANALYZED_NBT, getStatsAnalyzed());
        tagCompound.putInt(CHICKEN_GROWTH_NBT, getGrowth());
        tagCompound.putInt(CHICKEN_GAIN_NBT, getGain());
        tagCompound.putInt(CHICKEN_STRENGTH_NBT, getStrength());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tagCompound) {
        super.readAdditionalSaveData(tagCompound);
        setChickenType(new ResourceLocation(tagCompound.getString(TYPE_NBT)));
        setStatsAnalyzed(tagCompound.getBoolean(CHICKEN_STATS_ANALYZED_NBT));
        setGrowth(getStatusValue(tagCompound, CHICKEN_GROWTH_NBT));
        setGain(getStatusValue(tagCompound, CHICKEN_GAIN_NBT));
        setStrength(getStatusValue(tagCompound, CHICKEN_STRENGTH_NBT));
        updateLayProgress();
    }

    private int getStatusValue(CompoundTag compound, String statusName) {
        return compound.contains(statusName) ? compound.getInt(statusName) : 1;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 20 * 60;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pBlock) {
        if (this.random.nextFloat() > 0.1) {
            return;
        }
        super.playStepSound(pPos, pBlock);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int lootingModifier, boolean recentlyHit) {
        for (ItemStack itemToDrop : getChickenData().drops()) {
            int count = 1 + random.nextInt(1 + lootingModifier);
            itemToDrop.setCount(itemToDrop.getCount() * count);
            spawnAtLocation(itemToDrop);
        }

        if (this.isOnFire()) {
            spawnAtLocation(new ItemStack(Items.COOKED_CHICKEN));
        } else {
            spawnAtLocation(new ItemStack(Items.CHICKEN));
        }
    }

    @Override
    public void setAge(int age) {
        int childAge = -24000;
        boolean resetToChild = age == childAge;
        if (resetToChild) {
            age = Math.min(-1, (childAge * (10 - getGrowth() + 1)) / 10);
        }

        int loveAge = 6000;
        boolean resetLoveAfterBreeding = age == loveAge;
        if (resetLoveAfterBreeding) {
            age = Math.max(1, (loveAge * (10 - getGrowth() + 1)) / 10);
        }

        super.setAge(age);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeUtf(entityData.get(CHICKEN_TYPE));
        buffer.writeBoolean(entityData.get(CHICKEN_STATS_ANALYZED));
        buffer.writeInt(entityData.get(CHICKEN_GROWTH));
        buffer.writeInt(entityData.get(CHICKEN_GAIN));
        buffer.writeInt(entityData.get(CHICKEN_STRENGTH));
        buffer.writeInt(entityData.get(LAY_PROGRESS));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        entityData.set(CHICKEN_TYPE, buffer.readUtf());
        entityData.set(CHICKEN_STATS_ANALYZED, buffer.readBoolean());
        entityData.set(CHICKEN_GROWTH, buffer.readInt());
        entityData.set(CHICKEN_GAIN, buffer.readInt());
        entityData.set(CHICKEN_STRENGTH, buffer.readInt());
        entityData.set(LAY_PROGRESS, buffer.readInt());
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        probeInfo.text("Tier: " + getTier());
        probeInfo.horizontal().progress(eggTime, totalEggTime);
        probeInfo.text("Gain: " + getGain());
        probeInfo.text("Growth: " + getGrowth());
        probeInfo.text("Strength: " + getStrength());
    }

    private record GroupData(ResourceLocation chickenID) implements SpawnGroupData {
    }
}

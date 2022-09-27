package com.tagnumelite.chickens.common.entities;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenData;
import com.tagnumelite.chickens.api.recipe.IBreedingRecipe;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.items.ModItems;
import com.tagnumelite.chickens.config.ServerConfig;
import com.tagnumelite.chickens.crafting.ModRecipeTypes;
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
import net.minecraft.util.Tuple;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
public class ChickensChicken extends Chicken implements IEntityAdditionalSpawnData {
    public static final EntityDataAccessor<String> CHICKEN_TYPE = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> CHICKEN_STATS_ANALYZED = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> CHICKEN_GROWTH = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> CHICKEN_GAIN = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> CHICKEN_STRENGTH = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> LAY_PROGRESS = SynchedEntityData.defineId(ChickensChicken.class, EntityDataSerializers.INT);

    private static final String TYPE_NBT = "Type";
    private static final String CHICKEN_STATS_ANALYZED_NBT = "Analyzed";
    private static final String CHICKEN_GROWTH_NBT = "Growth";
    private static final String CHICKEN_GAIN_NBT = "Gain";
    private static final String CHICKEN_STRENGTH_NBT = "Strength";

    public ChickensChicken(Level level) {
        this(ModEntityTypes.CHICKEN.get(), level);
    }

    public ChickensChicken(EntityType<? extends ChickensChicken> entityType, Level level) {
        super(entityType, level);
    }

    private static void inheritStats(ChickensChicken newChicken, ChickensChicken parent) {
        newChicken.setGrowth(parent.getGrowth());
        newChicken.setGain(parent.getGain());
        newChicken.setStrength(parent.getStrength());
    }

    private static void increaseStats(ChickensChicken newChicken, ChickensChicken parent1, ChickensChicken parent2, RandomSource rand) {
        int parent1Strength = parent1.getStrength();
        int parent2Strength = parent2.getStrength();
        newChicken.setGrowth(calculateNewStat(parent1Strength, parent2Strength, parent1.getGrowth(), parent2.getGrowth(), rand));
        newChicken.setGain(calculateNewStat(parent1Strength, parent2Strength, parent2.getGain(), parent2.getGain(), rand));
        newChicken.setStrength(calculateNewStat(parent1Strength, parent2Strength, parent1Strength, parent2Strength, rand));
    }

    private static int calculateNewStat(int thisStrength, int mateStrength, int stat1, int stat2, RandomSource rand) {
        int mutation = rand.nextInt(2) + 1;
        int newStatValue = (stat1 * thisStrength + stat2 * mateStrength) / (thisStrength + mateStrength) + mutation;
        if (newStatValue <= 1) return 1;
        return Math.min(newStatValue, 10);
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

    public ResourceLocation getChickenType() {
        return new ResourceLocation(entityData.get(CHICKEN_TYPE));
    }

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

        return Component.translatable("entity." + getChickenType().toLanguageKey() + ".name");
    }

    @Override
    public Chicken getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        ChickensChicken mateChicken = (ChickensChicken) mate;

        // TODO: Replace null with actual container
        SimpleContainer container = new SimpleContainer(2);
        container.addItem(ModItems.SPAWN_EGG.get().fromChickenType(getChickenType()));
        container.addItem(ModItems.SPAWN_EGG.get().fromChickenType(mateChicken.getChickenType()));
        List<IBreedingRecipe> breeding = level.getRecipeManager().getRecipesFor(ModRecipeTypes.BREEDING.get(), container, level);

        ChickensChicken offspring = ModEntityTypes.CHICKEN.get().create(level);
        //offspring.setChickenType(childToBeBorn.getId());

        //boolean mutatingStats = chickenDescription.getId() == mateChickenDescription.getId() && childToBeBorn.getId() == chickenDescription.getId();
        //if (mutatingStats) {
        //    increaseStats(offspring, this, mateChicken, random);
        //} else if (chickenDescription.getId() == childToBeBorn.getId()) {
        //    inheritStats(offspring, this);
        //} else if (mateChickenDescription.getId() == childToBeBorn.getId()) {
        //    inheritStats(offspring, mateChicken);
        //}
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
        updateLayProgress();
    }

    public int getLayProgress() {
        return entityData.get(LAY_PROGRESS);
    }

    private void updateLayProgress() {
        entityData.set(LAY_PROGRESS, eggTime / 60 / 20 / 2);
    }

    private void resetTimeUntilNextEgg() {
        ChickenData chickenData = getChickenData();
        int newEggTime = random.nextInt(chickenData.maxLayTime()) + chickenData.minLayTime();
        if (ServerConfig.DEBUG_DROPS.get())
            setTimeUntilNextEgg(20 * 5);
        else
            setTimeUntilNextEgg(Math.max(newEggTime, 20)); // Minimum of 20
    }

    @Override
    public boolean checkSpawnRules(@NotNull LevelAccessor pLevel, @NotNull MobSpawnType pSpawnReason) {
        //boolean anyInNether = ChickensRegistry.isAnyIn(SpawnType.HELL); TODO
        //boolean anyInOverworld = ChickensRegistry.isAnyIn(SpawnType.NORMAL) || ChickensRegistry.isAnyIn(SpawnType.SNOW);
        //Holder<Biome> biome = level.getBiome(getOnPos());
        return super.checkSpawnRules(pLevel, pSpawnReason);// || anyInNether && biome.is(BiomeTags.IS_NETHER) || anyInOverworld && super.checkSpawnRules(pLevel, pSpawnReason);
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
            } else if (!pReason.equals(MobSpawnType.SPAWN_EGG)) {
                List<Tuple<ResourceLocation, ChickenData>> possibleChickens = Chickens.getChickenManager().getChickensForBiome(pLevel.getBiome(blockPosition()));
                if (possibleChickens.size() > 0) {
                    Tuple<ResourceLocation, ChickenData> chickenToSpawn = possibleChickens.get(random.nextInt(possibleChickens.size()));

                    ResourceLocation chickenId = chickenToSpawn.getA();
                    setChickenType(chickenId);
                    livingData = new GroupData(chickenId);
                }
            } else {
                setChickenType(Utils.getTypeFromTag(pDataTag));
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
    protected @NotNull ResourceLocation getDefaultLootTable() {
        //return null;
        return super.getDefaultLootTable();
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
        Chickens.LOGGER.error("Write Spawn Data");
        buffer.writeUtf(entityData.get(CHICKEN_TYPE));
        //buffer.writeBoolean(entityData.get(CHICKEN_STATS_ANALYZED));
        //buffer.writeInt(entityData.get(CHICKEN_GROWTH));
        //buffer.writeInt(entityData.get(CHICKEN_GAIN));
        //buffer.writeInt(entityData.get(CHICKEN_STRENGTH));
        //buffer.writeInt(entityData.get(LAY_PROGRESS));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        Chickens.LOGGER.error("Read Spawn Data");
        entityData.set(CHICKEN_TYPE, buffer.readUtf());
        //entityData.set(CHICKEN_STATS_ANALYZED, buffer.readBoolean());
        //entityData.set(CHICKEN_GROWTH, buffer.readInt());
        //entityData.set(CHICKEN_GAIN, buffer.readInt());
        //entityData.set(CHICKEN_STRENGTH, buffer.readInt());
        //entityData.set(LAY_PROGRESS, buffer.readInt());
    }

    private record GroupData(ResourceLocation chickenID) implements SpawnGroupData {
    }
}

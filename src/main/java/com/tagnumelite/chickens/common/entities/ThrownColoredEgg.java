package com.tagnumelite.chickens.common.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Created by setyc on 13.02.2016.
 */
public class ThrownColoredEgg extends ThrownEgg {
    public static final String TYPE_NBT = "Type";
    private static final EntityDataAccessor<String> CHICKEN_TYPE = SynchedEntityData.defineId(ThrownColoredEgg.class, EntityDataSerializers.STRING);

    public ThrownColoredEgg(EntityType<? extends ThrownEgg> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownColoredEgg(Level pLevel, LivingEntity pShooter) {
        super(pLevel, pShooter);
    }

    public ThrownColoredEgg(Level worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    private ResourceLocation getChickenType() {
        return new ResourceLocation(this.entityData.get(CHICKEN_TYPE));
    }

    public void setChickenType(ResourceLocation type) {
        this.entityData.set(CHICKEN_TYPE, type.toString());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHICKEN_TYPE, "");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putString(TYPE_NBT, getChickenType().toString());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tagCompound) {
        super.readAdditionalSaveData(tagCompound);
        setChickenType(new ResourceLocation(tagCompound.getString(TYPE_NBT)));
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);

        if (!this.level.isClientSide && this.random.nextInt(8) == 0) {
            int i = 1;

            if (this.random.nextInt(32) == 0) {
                i = 4;
            }

            for (int j = 0; j < i; ++j) {
                ChickensChicken entityChicken = new ChickensChicken(this.level);
                entityChicken.setChickenType(getChickenType());
                entityChicken.setAge(-24000);
                entityChicken.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                this.level.addFreshEntity(entityChicken);
            }
        }

        for (int k = 0; k < 8; ++k) {
            this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D);
        }

        if (!this.level.isClientSide) {
            this.setRemoved(RemovalReason.DISCARDED);
        }
    }
}

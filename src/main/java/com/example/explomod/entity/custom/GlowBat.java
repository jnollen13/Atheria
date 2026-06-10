package com.example.explomod.entity.custom;

import com.example.explomod.ExploMod;
import net.jpountz.lz4.LZ4FrameOutputStream;
import net.minecraft.advancements.critereon.LightPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GlowBat extends AmbientCreature {
    private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING;
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS;
    private static final TargetingConditions BAT_RESTING_TARGETING;
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState restAnimationState = new AnimationState();
    @Nullable
    private BlockPos targetPosition;

    public GlowBat(EntityType<? extends GlowBat> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            this.setResting(true);
        }
    }

    public boolean isFlapping() {
        return !this.isResting() && (float)this.tickCount % 10.0F == 0.0F;
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_FLAGS, (byte)0);
        builder.define(DATA_DARK_TICKS_REMAINING, 0);
    }

    protected float getSoundVolume() {
        return 0.1F;
    }

    public float getVoicePitch() {
        return super.getVoicePitch() * 0.95F;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return this.isResting() && this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
    }

    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.BAT_HURT;
    }

    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    public boolean isPushable() {
        return false;
    }

    protected void doPush(@NotNull Entity entity) {

    }

    protected void pushEntities() {
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0F)
                .add(Attributes.ARMOR, 4d).add(Attributes.BURNING_TIME, 1d);
    }

    public boolean isResting() {
        return (this.entityData.get(DATA_ID_FLAGS) & 1) != 0;
    }

    public void setResting(boolean isResting) {
        byte b0 = this.entityData.get(DATA_ID_FLAGS);
        if (isResting) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 | 1));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 & -2));
        }

    }

    public void tick() {
        super.tick();
        if (this.isResting()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPosRaw(this.getX(), (double) Mth.floor(this.getY()) + (double)1.0F - (double)this.getBbHeight(), this.getZ());
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.6, 1.0F));
        }
        this.setupAnimationStates();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        BlockPos blockpos = this.blockPosition();
        BlockState glow = ExploMod.AIR.get().defaultBlockState();
        BlockPos blockpos1 = blockpos.above();
        BlockPos blockpos2 = blockpos1.east();
        if(getInBlockState()==Blocks.AIR.defaultBlockState()){
            level().setBlock(blockpos, ExploMod.AIR.get().defaultBlockState(), 2);
        }
        if (this.isResting()) {
            boolean flag = this.isSilent();
            if (this.level().getBlockState(blockpos1).isRedstoneConductor(this.level(), blockpos)) {
                if (this.random.nextInt(200) == 0) {
                    this.yHeadRot = (float)this.random.nextInt(360);
                }

                if (this.level().getNearestPlayer(BAT_RESTING_TARGETING, this) != null) {
                    this.setResting(false);
                    if (!flag) {
                        this.level().levelEvent(null, 1025, blockpos, 0);
                    }
                }
            } else {
                this.setResting(false);
                if (!flag) {
                    this.level().levelEvent(null, 1025, blockpos, 0);
                }
            }
        } else {
            if (this.targetPosition != null && (!this.level().isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= this.level().getMinBuildHeight())) {
                this.targetPosition = null;
            }

            if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan(this.position(), 2.0F)) {
                this.targetPosition = BlockPos.containing(this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7), this.getY() + (double)this.random.nextInt(6) - (double)2.0F, this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
            }

            Vec3 vec31 = getVec3();
            this.setDeltaMovement(vec31);
            float f = (float)(Mth.atan2(vec31.z, vec31.x) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
            float f1 = Mth.wrapDegrees(f - this.getYRot());
            this.zza = 0.5F;
            this.setYRot(this.getYRot() + f1);
            if (this.random.nextInt(100) == 0 && this.level().getBlockState(blockpos1).isRedstoneConductor(this.level(), blockpos1)) {
                this.setResting(true);
            }
        }

    }

    private @NotNull Vec3 getVec3() {
        assert this.targetPosition != null;
        double d2 = (double)this.targetPosition.getX() + (double)0.5F - this.getX();
        assert this.targetPosition != null;
        double d0 = (double)this.targetPosition.getY() + 0.1 - this.getY();
        assert this.targetPosition != null;
        double d1 = (double)this.targetPosition.getZ() + (double)0.5F - this.getZ();
        Vec3 vec3 = this.getDeltaMovement();
        return vec3.add((Math.signum(d2) * (double)0.5F - vec3.x) * (double)0.1F, (Math.signum(d0) * (double)0.7F - vec3.y) * (double)0.1F, (Math.signum(d1) * (double)0.5F - vec3.z) * (double)0.1F);
    }

    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.level().isClientSide && this.isResting()) {
                this.setResting(false);
            }

            return super.hurt(source, amount);
        }
    }


    private void setDarkTicks(int darkTicks) {
        this.entityData.set(DATA_DARK_TICKS_REMAINING, darkTicks);
    }

    public int getDarkTicksRemaining() {
        return this.entityData.get(DATA_DARK_TICKS_REMAINING);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_ID_FLAGS, compound.getByte("BatFlags"));
        this.setDarkTicks(compound.getInt("DarkTicksRemaining"));
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("BatFlags", this.entityData.get(DATA_ID_FLAGS));
        compound.putInt("DarkTicksRemaining", this.getDarkTicksRemaining());
    }


    private void setupAnimationStates() {
        if (this.isResting()) {
            this.flyAnimationState.stop();
            this.restAnimationState.startIfStopped(this.tickCount);
        } else {
            this.restAnimationState.stop();
            this.flyAnimationState.startIfStopped(this.tickCount);
        }

    }

    static {
        DATA_ID_FLAGS = SynchedEntityData.defineId(GlowBat.class, EntityDataSerializers.BYTE);
        DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(GlowBat.class, EntityDataSerializers.INT);
        BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0F);
    }
}

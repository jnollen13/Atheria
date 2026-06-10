package com.example.explomod.entity.custom;

import com.example.explomod.entity.npc.GrassGolemAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrassGolem extends Animal {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private int attackAnimationTick;

    public GrassGolem(EntityType<? extends GrassGolem> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GrassGolemAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zombie.class, false, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false, false));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
    }

    public static AttributeSupplier.Builder createAttributes() {
            return Mob.createMobAttributes()
                    .add(Attributes.MAX_HEALTH, 10.0F)
                    .add(Attributes.MOVEMENT_SPEED, 0.1d)
                    .add(Attributes.ATTACK_DAMAGE, 2.0F)
                    .add(Attributes.STEP_HEIGHT, 0.95F);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    protected void doPush(@NotNull Entity entity) {
        if (entity instanceof Enemy && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)entity);
        }
        super.doPush(entity);
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackAnimationTick > 0) {
            --this.attackAnimationTick;
        }
    }

    public boolean canAttackType(@NotNull EntityType<?> type) {
        return !this.isPlayerCreated() || type != EntityType.PLAYER;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("PlayerCreated", this.isPlayerCreated());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setPlayerCreated(compound.getBoolean("PlayerCreated"));
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return false;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean doHurtTarget(Entity entity) {
        this.attackAnimationTick = 10;
        this.level().broadcastEntityEvent(this, (byte)4);
        float f = this.getAttackDamage();
        float f1 = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        DamageSource damagesource = this.damageSources().mobAttack(this);
        boolean flag = entity.hurt(damagesource, f1);
        if (flag) {
            Level var11 = this.level();
            if (var11 instanceof ServerLevel serverlevel) {
                EnchantmentHelper.doPostAttackEffects(serverlevel, entity, damagesource);
            }
        }

        this.playSound(SoundEvents.WET_GRASS_HIT, 1.0F, 1.0F);
        return flag;
    }

    public boolean hurt(@NotNull DamageSource source, float amount) {
        return super.hurt(source, amount);
    }

    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackAnimationTick = 10;
            this.playSound(SoundEvents.WET_GRASS_HIT, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(id);
        }

    }

    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.GRASS_HIT;
    }

    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GRASS_BREAK;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!itemstack.is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else {
            float f = this.getHealth();
            this.heal(10.0F);
            if (this.getHealth() == f) {
                return InteractionResult.PASS;
            } else {
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.GRASS_PLACE, 1.0F, f1);
                itemstack.consume(1, player);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
        this.playSound(SoundEvents.GRASS_STEP, 1.0F, 1.0F);
    }

    public boolean isPlayerCreated() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setPlayerCreated(boolean playerCreated) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (playerCreated) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 & -2));
        }

    }

    public void die(@NotNull DamageSource cause) {
        super.die(cause);
    }

    public boolean checkSpawnObstruction(LevelReader level) {
        BlockPos blockpos = this.blockPosition();
        BlockPos blocks1 = blockpos.below();
        BlockState blockstate = level.getBlockState(blocks1);
        if (!blockstate.entityCanStandOn(level, blocks1, this)) {
            return false;
        } else {
            for(int i = 1; i < 3; ++i) {
                BlockPos blocks2 = blockpos.above(i);
                BlockState blockbuster1 = level.getBlockState(blocks2);
                if (!NaturalSpawner.isValidEmptySpawnBlock(level, blocks2, blockbuster1, blockbuster1.getFluidState(), EntityType.IRON_GOLEM)) {
                    return false;
                }
            }

            return NaturalSpawner.isValidEmptySpawnBlock(level, blockpos, level.getBlockState(blockpos), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && level.isUnobstructed(this);
        }
    }

    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0F, 0.875F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(GrassGolem.class, EntityDataSerializers.BYTE);
    }

}

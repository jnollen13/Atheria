package com.example.explomod.entity.custom;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class StrangeArrowEntity extends AbstractArrow {
    private int duration = 2300;

    public StrangeArrowEntity(EntityType<? extends StrangeArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public StrangeArrowEntity(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.STRANGE_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    public StrangeArrowEntity(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.STRANGE_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(ParticleTypes.ASH, this.getX(), this.getY(), this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
        }

    }

    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        UniformInt RAND = UniformInt.of(1, 6);
        Integer EFFECT = RAND.sample(RandomSource.create());
        if(EFFECT==1) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.WITHER, this.duration, 4);
            living.addEffect(mobeffectinstance, this.getEffectSource());
        } else if (EFFECT==2) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.LEVITATION, this.duration, 0);
            living.addEffect(mobeffectinstance, this.getEffectSource());
        } else if (EFFECT==3) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
            living.addEffect(mobeffectinstance, this.getEffectSource());
        } else if (EFFECT==4) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.HUNGER, this.duration, 1);
            living.addEffect(mobeffectinstance, this.getEffectSource());
        } else if (EFFECT==5) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, this.duration, 2);
            living.addEffect(mobeffectinstance, this.getEffectSource());
        }else if (EFFECT==6) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.WEAKNESS, this.duration, 3);
            living.addEffect(mobeffectinstance, this.getEffectSource());
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Duration")) {
            this.duration = compound.getInt("Duration");
        }

    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Duration", this.duration);
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ExploMod.STRANGE_ARROW.get());
    }
}

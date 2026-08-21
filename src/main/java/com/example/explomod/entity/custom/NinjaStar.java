package com.example.explomod.entity.custom;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.ModEntities;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class NinjaStar extends AbstractArrow {
    private int duration = 2300;
    private boolean destroy = true;
    protected boolean willCrit = RandomSource.create().nextIntBetweenInclusive(1, 1000000)<=342901;
    protected ItemStack mainStack;

    public NinjaStar(EntityType<? extends NinjaStar> entityType, Level level) {
        super(entityType, level);
    }

    public NinjaStar(Level level, LivingEntity owner, ItemStack firedFromWeapon) {
        super(ModEntities.FULL_MOON.get(), owner, level, firedFromWeapon, firedFromWeapon);
        this.mainStack=firedFromWeapon;
    }

    public NinjaStar(Level level, double x, double y, double z, ItemStack firedFromWeapon) {
        super(ModEntities.FULL_MOON.get(), x, y, z, level, firedFromWeapon, firedFromWeapon);
        this.mainStack=firedFromWeapon;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            if(this.willCrit) {
                this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }
        if(this.inGround){
            if (!this.destroy) {
                if(this.getOwner() instanceof Player player) {
                    ItemStack stack = new ItemStack(ExploMod.FULL_MOON.asItem());
                    stack.set(DataComponents.ENCHANTMENTS, this.mainStack.getTagEnchantments());
                    player.addItem(stack);
                    this.discard();
                }
            }else{
                this.discard();
            }
        }
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Duration")) {
            this.duration = compound.getInt("Duration");
        }
        if(compound.contains("Destroy")){
            this.destroy = compound.getBoolean("Destroy");
        }
        if (compound.contains("weapon", 10)) {
            this.mainStack = (ItemStack)ItemStack.parse(this.registryAccess(), compound.getCompound("weapon")).orElse((ItemStack) null);
        } else {
            this.mainStack = null;
        }

    }

    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Duration", this.duration);
        compound.putBoolean("Destroy", this.destroy);
    }

    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(ExploMod.FULL_MOON.get());
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        float f = (float)this.getDeltaMovement().length();
        double d0 = RandomSource.create().nextInt(2,6);
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().mobProjectile(this, (LivingEntity)this.getOwner());
        Level var9 = this.level();
        if (var9 instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)var9;
            d0 = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, (float)d0);
        }

        int j = Mth.ceil(Mth.clamp((double)f * d0, 0.0F, Integer.MAX_VALUE));
        if (this.willCrit) {
            long k = this.random.nextInt(j / 2 + 2);
            j = (int)Math.min(k + (long)j, 2147483647L);
            for (int i = 0; i < 7; i++) {
                int Xspread = RandomSource.create().nextInt(1,3);
                int Yspread = RandomSource.create().nextInt(1,3);
                int Zspread = RandomSource.create().nextInt(1,3);
                double Xmod = RandomSource.create().nextDouble();
                double Ymod = RandomSource.create().nextDouble();
                double Zmod = RandomSource.create().nextDouble();
                if(Xspread==2){
                    Xmod*=-1;
                }
                if(Yspread==2){
                    Ymod*=-1;
                }
                if(Zspread==2){
                    Zmod*=-1;
                }
                level().addParticle(ParticleTypes.CRIT, this.getX()+Xmod, this.getY()+Zmod, this.getZ()+Zmod, 0, 1, 0);
            }
            if(RandomSource.create().nextIntBetweenInclusive(1, 1000)<=352){
                j = j +this.random.nextIntBetweenInclusive(1, 5);
            }
        }

        if (entity1 instanceof LivingEntity livingentity1) {
            livingentity1.setLastHurtMob(entity);
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int i = entity.getRemainingFireTicks();
        if (this.isOnFire() && !flag) {
            entity.igniteForSeconds(5.0F);
        }

        if (entity.hurt(damagesource, (float)j)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                this.doKnockback(livingentity, damagesource);
                Level var13 = this.level();
                if (var13 instanceof ServerLevel) {
                    ServerLevel serverlevel1 = (ServerLevel)var13;
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, livingentity, damagesource, this.getWeaponItem());
                }

                this.doPostHurtEffects(livingentity);
            }
            if(!this.destroy){
                if(this.getOwner() instanceof Player player){
                    player.addItem(mainStack);
                }else{

                }
            }
            this.discard();
            this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        } else {
            entity.setRemainingFireTicks(i);
            this.deflect(ProjectileDeflection.REVERSE, entity, this.getOwner(), false);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED&&!destroy) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            }
        }

    }
}

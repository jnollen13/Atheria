package com.example.explomod.entity.custom;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

public class BlockBird extends TamableAnimal implements VariantHolder<BlockBird.Variant>, FlyingAnimal {
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID;
    public AnimationState flying = new AnimationState();
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public BlockBird(EntityType<? extends BlockBird> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setVariant((BlockBird.Variant) Util.getRandom(BlockBird.Variant.values(), level.getRandom()));
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(false);
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public boolean isBaby() {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TamableAnimal.TamableAnimalPanicGoal(1.25d, DamageTypeTags.PANIC_CAUSES));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BlockBird.BlockBirdWanderGoal(this, 1.0F));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 16f, 1f, 1f));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0F, 3.0F, 7.0F));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Ocelot.class, 8f, 1f, 1.5f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0F)
                .add(Attributes.FLYING_SPEED, 0.4d)
                .add(Attributes.MOVEMENT_SPEED, 0.2d)
                .add(Attributes.ARMOR, 0.1d)
                .add(Attributes.FOLLOW_RANGE, 74.3627538d)
                .add(Attributes.ATTACK_DAMAGE, 0.5d);
    }

    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < (double)0.0F) {
            this.setDeltaMovement(vec3.multiply(1.0F, 0.6, 1.0F));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(player.getItemInHand(hand).is(Items.WHEAT_SEEDS)){
            for (int i = 0; i < 7; i++) {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY() + this.getEyeHeight(), this.getZ(), RandomSource.create().nextDouble(), RandomSource.create().nextDouble(), RandomSource.create().nextDouble());
            }
            return InteractionResult.CONSUME_PARTIAL;
        }else {
            return super.mobInteract(player, hand);
        }
    }

    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    public static boolean checkBlockBirdSpawnRules(EntityType<BlockBird> lockBird, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    public boolean canMate(@NotNull Animal otherAnimal) {
        return false;
    }

    @Nullable
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return null;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return getAmbient(this.level(), this.level().random);
    }

    public static SoundEvent getAmbient(Level level, RandomSource random) {
        return SoundEvents.BAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.PARROT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    public float getVoicePitch() {
        return getPitch(this.random);
    }

    public static float getPitch(RandomSource random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    public @NotNull SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    public boolean isPushable() {
        return true;
    }

    protected void doPush(@NotNull Entity entity) {
        if (!(entity instanceof Player)) {
            super.doPush(entity);
        }

    }

    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    public BlockBird.@NotNull Variant getVariant() {
        return BlockBird.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariant(BlockBird.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, 0);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(BlockBird.Variant.byId(compound.getInt("Variant")));
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    protected boolean canFlyToOwner() {
        return false;
    }

    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0F, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    static {
        DATA_VARIANT_ID = SynchedEntityData.defineId(BlockBird.class, EntityDataSerializers.INT);
    }

    static class BlockBirdWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public BlockBirdWanderGoal(PathfinderMob p_186224_, double p_186225_) {
            super(p_186224_, p_186225_);
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos blockpos = this.mob.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - (double)3.0F), Mth.floor(this.mob.getY() - (double)6.0F), Mth.floor(this.mob.getZ() - (double)3.0F), Mth.floor(this.mob.getX() + (double)3.0F), Mth.floor(this.mob.getY() + (double)6.0F), Mth.floor(this.mob.getZ() + (double)3.0F))) {
                if (!blockpos.equals(blockpos1)) {
                    BlockState blockstate = this.mob.level().getBlockState(blockpos$mutableblockpos1.setWithOffset(blockpos1, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
                    if (flag && this.mob.level().isEmptyBlock(blockpos1) && this.mob.level().isEmptyBlock(blockpos$mutableblockpos.setWithOffset(blockpos1, Direction.UP))) {
                        return Vec3.atBottomCenterOf(blockpos1);
                    }
                }
            }

            return null;
        }
    }

    public static enum Variant implements StringRepresentable {
        RED_BLUE(0, "red"),
        BLUE(1, "blue"),
        GREEN(2, "green"),
        YELLOW_BLUE(3, "yellow"),
        GRAY(4, "gray"),
        WHITE(5, "white");

        public static final Codec<BlockBird.Variant> CODEC = StringRepresentable.fromEnum(BlockBird.Variant::values);
        private static final IntFunction<BlockBird.Variant> BY_ID = ByIdMap.continuous(BlockBird.Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
        final int id;
        private final String name;

        private Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public static BlockBird.Variant byId(int id) {
            return (BlockBird.Variant)BY_ID.apply(id);
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}

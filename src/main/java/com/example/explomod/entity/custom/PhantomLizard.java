package com.example.explomod.entity.custom;

import com.example.explomod.ExploMod;
import com.google.common.collect.UnmodifiableIterator;
import com.sun.jna.platform.unix.X11;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;


public class PhantomLizard extends FlyingMob implements PlayerRideableJumping, PlayerRideable, OwnableEntity{
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState swimAnimationState = new AnimationState();

    public PhantomLizard(EntityType<? extends FlyingMob> entityType, Level level) {
        super(entityType, level);
    }

    public void aboveGround() {
        double location = getY()-0.2;
        if(this.level().getBlockCollisions(this, this.getBoundingBox().move(0, -0.3, 0)).iterator().hasNext()){
            this.setPos(getX(), getY()-0.201, getZ());
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 7.2F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1, 6f, 24f));
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.GRAVITY, 0.5)
                .add(Attributes.FLYING_SPEED, 19.5)
                .add(Attributes.MOVEMENT_SPEED, 6.2)
                .add(Attributes.FOLLOW_RANGE, 50);
    }
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            player.startRiding(this);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {
            if (travelVector.z <= (double)0.0F) {
            }
        }
        if(player.isHolding(ExploMod.STONED_ITEM.get())){
                    this.setPos(getX(), getY()-0.15, getZ());
        }

    }
    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }

    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
            float f = player.xxa * 0.5F;
            float f1 = player.zza;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }
            return new Vec3((double)f, (double)0.0F, (double)f1);

    }

    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int i) {

    }

    @Override
    public void handleStopJump() {

    }

    @Override
    public int getJumpCooldown() {
        return PlayerRideableJumping.super.getJumpCooldown();
    }

    public void onPlayerJump(int jumpPower) {
                if (isShiftKeyDown()){
                    Vec3 initialVec = this.getDeltaMovement();
                    Vec3 climbVec = new Vec3(initialVec.x, -2.6D, initialVec.z);
                    this.setDeltaMovement(climbVec.scale(1.0D));
                } else {
                    Vec3 initialVec = this.getDeltaMovement();
                    Vec3 climbVec = new Vec3(initialVec.x, 0.2D, initialVec.z);
                    this.setDeltaMovement(climbVec.scale(1.0D));
                }
            }


            protected float getRiddenSpeed(Player player) {
        return (float)this.getAttributeValue(Attributes.FLYING_SPEED);
    }


    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof Player) {
                return (Player)entity;
            }
        return super.getControllingPassenger();
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 direction, LivingEntity passenger) {
        double d0 = this.getX() + direction.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + direction.z;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        UnmodifiableIterator var10 = passenger.getDismountPoses().iterator();
        while(var10.hasNext()) {
            Pose pose = (Pose)var10.next();
            blockpos$mutableblockpos.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + (double)0.75F;

            while(true) {
                double d4 = this.level().getBlockFloorHeight(blockpos$mutableblockpos);
                if ((double)blockpos$mutableblockpos.getY() + d4 > d3) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = passenger.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, (double)blockpos$mutableblockpos.getY() + d4, d2);
                    if (DismountHelper.canDismountTo(this.level(), passenger, aabb.move(vec3))) {
                        passenger.setPose(pose);
                        return vec3;
                    }
                }

                blockpos$mutableblockpos.move(Direction.UP);
                if ((double)blockpos$mutableblockpos.getY() < d3) {
                    break;
                }
            }
        }

        return null;
    }



    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)livingEntity.getBbWidth(), this.getYRot() + (livingEntity.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 vec31 = this.getDismountLocationInDirection(vec3, livingEntity);
        if (vec31 != null) {
            return vec31;
        } else {
            Vec3 vec32 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)livingEntity.getBbWidth(), this.getYRot() + (livingEntity.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
            Vec3 vec33 = this.getDismountLocationInDirection(vec32, livingEntity);
            return vec33 != null ? vec33 : this.position();
        }

    }

    @Override
    public @org.jetbrains.annotations.Nullable UUID getOwnerUUID() {
        return null;
    }
}

package com.example.explomod.item.custom;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.RadiationBall;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagicRadiationStaffItem extends Item implements ProjectileItem {
    public MagicRadiationStaffItem(Properties properties) {
        super(properties);
    }

    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        return true;
    }

    public static ItemAttributeModifiers createAttributes(int attackDamage, float attackSpeed) {
        return createAttributes();
    }

    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    public boolean isValidRepairItem(@NotNull ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(ExploMod.RADIUM_INGOT.get());
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();}


    public static Tool createToolProperties() {
        return new Tool(List.of(), 20.0F, 10);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public int getEnchantmentValue() {
        return 12;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
            player.startUsingItem(hand);
            return InteractionResultHolder.success(itemstack);
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            if(i>6){
            RadiationBall snowball = new RadiationBall(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
                if (i==9||i==8||i==7){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.5F, 1.5F);
                }else if(i==10) {
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                }else if (i == 11 || i == 12 || i == 13 || i == 14 || i ==15){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 0.9F);
                }else if (i==16 || i ==17 ||i==18){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 0.8F);
                }else if (i==19||i==20){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 0.7F);
                }else if (i==21||i==22||i==23||i==24||i==25){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.5F, 0.6F);
                }else if(i<40){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4.0F, 0.5F);
                }else if (i<100){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.1F, 5.0F, 0.25F);
                }else if (i<200){
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.1F, 7.5F, 0.1F);
                }else {
                    snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 10.3141F, 0F);
                }
                level.addFreshEntity(snowball);
                stack.hurtAndBreak(4, player, EquipmentSlot.MAINHAND);
        }}
    }

    public @NotNull Projectile asProjectile(Level level, Position pos, @NotNull ItemStack stack, Direction direction) {
        RandomSource randomsource = level.getRandom();
        double d0 = randomsource.triangle(direction.getStepX(), 0.11485000000000001);
        double d1 = randomsource.triangle(direction.getStepY(), 0.11485000000000001);
        double d2 = randomsource.triangle(direction.getStepZ(), 0.11485000000000001);
        Vec3 vec3 = new Vec3(d0, d1, d2);
        WindCharge windcharge = new WindCharge(level, pos.x(), pos.y(), pos.z(), vec3);
        windcharge.setDeltaMovement(vec3);
        return windcharge;
    }
}

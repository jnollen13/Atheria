package item.custom;

import com.example.explomod.entity.custom.StrangeArrowEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Properties;

public class Throwable extends ArrowItem {
    public Throwable(Item.Properties p_43235_) {
        super(p_43235_);
    }

    @Override
    public AbstractArrow createArrow(Level p_43237_, ItemStack p_43238_, LivingEntity p_43239_, @Nullable ItemStack p_345773_) {
        return new StrangeArrowEntity(p_43237_, p_43239_, p_43238_.copyWithCount(1), p_345773_);
    }

    @Override
    public Projectile asProjectile(Level p_338332_, Position p_338313_, ItemStack p_338304_, Direction p_338842_) {
        StrangeArrowEntity spectralarrow = new StrangeArrowEntity(p_338332_, p_338313_.x(), p_338313_.y(), p_338313_.z(), p_338304_.copyWithCount(1), null);
        spectralarrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return spectralarrow;
    }

    public static ItemAttributeModifiers createAttributes(int attackDamage, float attackSpeed) {
        return createAttributes((float)attackDamage, attackSpeed);
    }

    public static ItemAttributeModifiers createAttributes(float p_331976_, float p_332104_) {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)(p_331976_ + 1), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)p_332104_, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(50, attacker, EquipmentSlot.MAINHAND);
        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 38));
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 526));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 132, 1));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 72, 2));
    }
}

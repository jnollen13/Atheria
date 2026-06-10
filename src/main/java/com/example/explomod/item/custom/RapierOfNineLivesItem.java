package item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RapierOfNineLivesItem extends TieredItem {
    public RapierOfNineLivesItem(Tier tier, Item.Properties properties) {
            super(tier, properties.component(DataComponents.TOOL, createToolProperties()));
        }

    public RapierOfNineLivesItem(Tier p_tier, Item.Properties p_properties, Tool toolComponentData) {
            super(p_tier, p_properties.component(DataComponents.TOOL, toolComponentData));
        }

        public static Tool createToolProperties() {
            return new Tool(List.of(Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 12.0F), Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.2F)), 0.8F, 5);
        }

        public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
            return createAttributes(tier, (float)attackDamage, attackSpeed);
        }

        public static ItemAttributeModifiers createAttributes(Tier p_330371_, float p_331976_, float p_332104_) {
            return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, p_331976_ + p_330371_.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, p_332104_, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
        }

        public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player) {
            return !player.isCreative();
        }

        public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
            return true;
        }

        public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
            stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        }

        public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
            return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
        }
}

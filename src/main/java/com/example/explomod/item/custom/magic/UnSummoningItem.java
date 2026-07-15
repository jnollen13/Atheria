package com.example.explomod.item.custom.magic;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.registries.Spell;
import com.example.explomod.registries.SpellRegistries;
import com.example.explomod.utill.MoreMath;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UnSummoningItem extends Item {
    private final Method method;
    public UnSummoningItem(Method method, Properties properties) {
        super(properties);
        this.method = method;
    }

    @Override
    public boolean onLeftClickEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity entity) {
        if(entity instanceof LivingEntity livingEntity) {
            if (isMethod(Method.INTERACT)) {
                if (spell().tryCastSpell(player.level(), player, Spell.CooldownType.ITEM, livingEntity, 0.95,0.57f)) {
                    player.getCooldowns().addCooldown(stack.getItem(), spell().getCooldown());
                    stack.hurtAndBreak(5, player, EquipmentSlot.MAINHAND);
                    return true;
                }else{
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    player.displayClientMessage(Component.translatable("magic.explomod.nem.item"), true);
                    return false;
                }
            } else {
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                return false;
            }
        }else{
            player.displayClientMessage(Component.translatable("message.explomod.fail.dead"), true);
            return false;
        }
    }


    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if(isMethod(Method.HIT)){
            if(spell().tryCastSpell(attacker.level(), attacker, Spell.CooldownType.ITEM, target)){
                stack.hurtAndBreak(4, attacker, EquipmentSlot.MAINHAND);
            }else{
                stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
            }
        }
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    public Spell spell(){
        return SpellRegistries.UNSUMMONING_SPELL;
    }

    private boolean isMethod(Method method1){
        if(this.method==Method.ALL){
            return true;
        }else {
            return method1 == this.method;
        }
    }

    public enum Method {
        HIT,
        INTERACT,
        ALL
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal(" "));
        tooltipComponents.add(spell().displayName(true));
        tooltipComponents.add(Component.literal("cooldown: "+spell().getCooldown()).withStyle(ChatFormatting.DARK_AQUA));
        tooltipComponents.add(Component.literal("mana: "+spell().manaUsage()).withStyle(ChatFormatting.DARK_AQUA));
        assert Minecraft.getInstance().player != null;
        Mana mana = Minecraft.getInstance().player.getData(AtheriaDataAttachments.MANA);
        float manaUsage = spell().manaUsage();
        tooltipComponents.add(spell().spellDescription(true));
        tooltipComponents.add(Component.literal(" "));
        if(mana.mana()<manaUsage){
            tooltipComponents.add(Component.literal(MoreMath.round(1, mana.mana())+"/"+manaUsage).append(Component.translatable("message.explomod.mtc")).withStyle(ChatFormatting.RED));
        }else{
            tooltipComponents.add(Component.literal(MoreMath.round(1, mana.mana())+"/"+manaUsage).append(Component.translatable("message.explomod.mtc")).withStyle(ChatFormatting.ITALIC, ChatFormatting.GREEN));
        }
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        assert Minecraft.getInstance().player != null;
        Mana mana = Minecraft.getInstance().player.getData(AtheriaDataAttachments.MANA);
        float manaUsage = spell().manaUsage();
        if(mana.mana()<manaUsage){
            return this.getDescriptionId()+".fail";
        }else{
            return super.getDescriptionId();
        }
    }
}

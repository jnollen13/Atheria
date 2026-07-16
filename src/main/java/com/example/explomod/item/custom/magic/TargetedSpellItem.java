package com.example.explomod.item.custom.magic;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.registries.Spell;
import com.example.explomod.utill.MoreMath;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class TargetedSpellItem extends Item {
    private Spell spell;
    private CastType type;

    public TargetedSpellItem(Spell spell, CastType type, Properties properties) {
        super(properties);
        this.spell = spell;
        this.type = type;
    }

    public Item withSpell(Spell spell){
        this.spell = spell;
        return this;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if(this.type==CastType.INTEGRATED) {
            if (spell.tryCastSpellIntegrated(Objects.requireNonNull(attacker.level().getServer()).getLevel(attacker.level().dimension()), attacker, Spell.CooldownType.ITEM, target, 0.9f)) {
                stack.hurtAndBreak(5, attacker, EquipmentSlot.MAINHAND);
            } else {
                stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
            }
        }else{
            spell.tryCastSpell(attacker.level(), attacker, Spell.CooldownType.ITEM, target);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal(" "));
        tooltipComponents.add(spell.displayName(true));
        tooltipComponents.add(Component.literal("cooldown: "+spell.getCooldown()).withStyle(ChatFormatting.DARK_AQUA));
        tooltipComponents.add(Component.literal("mana: "+spell.manaUsage()).withStyle(ChatFormatting.DARK_AQUA));
        tooltipComponents.add(Component.literal("item cooldown reduction: 10%").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("total cooldown: "+spell.getCooldown()*0.9).withStyle(ChatFormatting.DARK_GREEN));
        assert Minecraft.getInstance().player != null;
        Mana mana = Minecraft.getInstance().player.getData(AtheriaDataAttachments.MANA);
        float manaUsage = spell.manaUsage();
        tooltipComponents.add(spell.spellDescription(true));
        tooltipComponents.add(Component.literal(" "));
        if(mana.mana()<manaUsage){
            tooltipComponents.add(Component.literal(MoreMath.round(1, mana.mana())+"/"+manaUsage).append(Component.translatable("message.explomod.mtc")).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }else{
            tooltipComponents.add(Component.literal(MoreMath.round(1, mana.mana())+"/"+manaUsage).append(Component.translatable("message.explomod.mtc")).withStyle(ChatFormatting.ITALIC, ChatFormatting.GREEN));
        }
        tooltipComponents.add(Component.translatable("tooltip.explomod.charges").append(Component.literal(String.valueOf(this.getMaxDamage(stack)/5))));
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        assert Minecraft.getInstance().player != null;
        Mana mana = Minecraft.getInstance().player.getData(AtheriaDataAttachments.MANA);
        float manaUsage = spell.manaUsage();
        if(mana.mana()<manaUsage){
            return this.getDescriptionId()+".fail";
        }else{
            return super.getDescriptionId();
        }
    }

    public enum CastType{
        INTEGRATED,
        DEFAULT
    }
}

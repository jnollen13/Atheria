package com.example.explomod.item.custom.magic;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.registries.Spell;
import com.example.explomod.utill.MoreMath;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleSpellItem extends Item {
    private Spell spell;

    public SimpleSpellItem(Spell spell, Properties properties) {
        super(properties);
        this.spell = spell;
    }

    public Item withSpell(Spell spell){
        this.spell = spell;
        return this;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if(spell.tryCastSpell(level, player, Spell.CooldownType.ITEM, 0.5f)){
            if(usedHand==InteractionHand.MAIN_HAND) {
                player.getItemInHand(usedHand).hurtAndBreak(5, player, EquipmentSlot.MAINHAND);
            }else{
                player.getItemInHand(usedHand).hurtAndBreak(5, player, EquipmentSlot.OFFHAND);
            }
        }else{
            if(usedHand==InteractionHand.MAIN_HAND) {
                player.getItemInHand(usedHand).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }else{
                player.getItemInHand(usedHand).hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal(" "));
        tooltipComponents.add(spell.displayName(true));
        tooltipComponents.add(Component.literal("cooldown: "+spell.getCooldown()).withStyle(ChatFormatting.DARK_AQUA));
        tooltipComponents.add(Component.literal("mana: "+spell.manaUsage()).withStyle(ChatFormatting.DARK_AQUA));
        tooltipComponents.add(Component.literal("item mana coverage: 50%").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("total mana: "+spell.manaUsage()/2).withStyle(ChatFormatting.DARK_GREEN));
        assert Minecraft.getInstance().player != null;
        Mana mana = Minecraft.getInstance().player.getData(AtheriaDataAttachments.MANA);
        float manaUsage = spell.manaUsage()/2;
        tooltipComponents.add(spell.spellDescription(true));
        tooltipComponents.add(Component.literal(" "));
        if(mana.mana()<manaUsage){
            tooltipComponents.add(Component.literal(MoreMath.round(1, mana.mana())+"/"+manaUsage).append(Component.translatable("message.explomod.mtc")).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }else{
            tooltipComponents.add(Component.literal(MoreMath.round(1, mana.mana())+"/"+manaUsage).append(Component.translatable("message.explomod.mtc")).withStyle(ChatFormatting.ITALIC, ChatFormatting.GREEN));
        }
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        assert Minecraft.getInstance().player != null;
        Mana mana = Minecraft.getInstance().player.getData(AtheriaDataAttachments.MANA);
        float manaUsage = spell.manaUsage()/2;
        if(mana.mana()<manaUsage){
            return this.getDescriptionId()+".fail";
        }else{
            return super.getDescriptionId();
        }
    }
}

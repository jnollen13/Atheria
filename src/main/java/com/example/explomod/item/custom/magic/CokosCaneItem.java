package com.example.explomod.item.custom.magic;

import com.example.explomod.item.custom.VisualEnchantmentTooltipItem;
import com.example.explomod.utill.PropertiesDescription;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CokosCaneItem extends VisualEnchantmentTooltipItem {

    public CokosCaneItem(Properties properties) {
        super(properties);
    }

    @Override
    public List<String> materials() {
        return List.of("Moon-Fire Wood");
    }

    @Override
    protected String itemType() {
        return "Staff";
    }

    @Override
    protected String itemSubType() {
        return "";
    }

    @Override
    protected List<PropertiesDescription> additionalProperties() {
        return List.of(new PropertiesDescription("Moon-Fire Wood", "this item is immune to fire."));
    }

    @Override
    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
}

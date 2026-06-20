package com.example.explomod.item.custom;

import com.example.explomod.component.AtheriaDataComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NiceStick extends Item{
    public NiceStick(Properties properties) {
        super(properties);
    }
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.explomod.nice_stick.shift_down"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.explomod.nice_stick"));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}

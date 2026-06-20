package com.example.explomod.item.custom;

import com.example.explomod.component.AtheriaDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LocationSaverItem extends Item {
    public LocationSaverItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();
        if(!level.isClientSide()) {
            level.playSound(null, context.getClickedPos(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS);

            context.getItemInHand().set(AtheriaDataComponents.COORDINATES, context.getClickedPos());
            context.getItemInHand().set(AtheriaDataComponents.SAVED_DIMENSION, context.getLevel().dimension());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack stack, @NotNull ItemStack other, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            stack.set(AtheriaDataComponents.DESCRIPTION, other.get(DataComponents.CUSTOM_NAME));
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
        if(stack.get(AtheriaDataComponents.COORDINATES) != null) {
            if(stack.get(AtheriaDataComponents.DESCRIPTION) == null) {
                tooltipComponents.add(Component.literal("Saved Location is: " + stack.get(AtheriaDataComponents.COORDINATES)+" and is in "+stack.get(AtheriaDataComponents.SAVED_DIMENSION)));
            }else{
                tooltipComponents.add(Component.literal(Objects.requireNonNull(stack.get(AtheriaDataComponents.DESCRIPTION)).getString()+" is at " + Objects.requireNonNull(stack.get(AtheriaDataComponents.COORDINATES)).getX()+", "+Objects.requireNonNull(stack.get(AtheriaDataComponents.COORDINATES)).getY()+", "+Objects.requireNonNull(stack.get(AtheriaDataComponents.COORDINATES)).getZ()+" and is in "+ Objects.requireNonNull(stack.get(AtheriaDataComponents.SAVED_DIMENSION)).location()).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
            }
        }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}

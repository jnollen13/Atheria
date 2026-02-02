package item.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class nice_stick extends Item{
    public nice_stick(Properties properties) {
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

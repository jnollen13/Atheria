package com.example.explomod.item.custom;

import com.example.explomod.utill.PropertiesDescription;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VisualEnchantmentTooltipItem extends Item {
    public VisualEnchantmentTooltipItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        addVisualEnchantmentTooltip(tooltipComponents);
    }

    protected void addVisualEnchantmentTooltip(List<Component> tooltipComponents){
        Component materials;
        Component typing = Component.literal("null");
        boolean typingFlag = false;
        if(materials().size()==1){
            materials = Component.literal(materials().getFirst());
        } else if (materials().size()==2) {
            materials = Component.literal(materials().getFirst()+", "+ materials().getLast());
        } else if (materials().size()==3) {
            materials = Component.literal(materials().getFirst()+", "+materials().get(2)+", "+ materials().getLast());
        } else if (materials().size()==4) {
            materials = Component.literal(materials().getFirst()+", "+materials().get(2)+", "+materials().get(3)+", "+ materials().getLast());
        }else{
            materials = Component.literal(materials().getFirst()+", "+materials().get(2)+", "+materials().get(3)+", "+ materials().get(4) + ", " + materials().getLast());
        }
        if(itemType().isEmpty()){typingFlag = true;} else if (itemSubType().isEmpty()) {
            typing= Component.literal(itemType().toUpperCase());
        }else{
            typing = Component.literal(itemType().toUpperCase()+" ("+itemSubType().toUpperCase()+")");
        }
        MutableComponent finalAddition;
        if(!typingFlag){
            finalAddition=Component.literal(typing.tryCollapseToString() + " - "+materials.tryCollapseToString());
        }else{
            finalAddition=Component.literal(materials.getString());
        }
        tooltipComponents.add(finalAddition.withStyle(ChatFormatting.BLUE));
        additionalProperties().forEach(propertiesDescription -> {
            if(!propertiesDescription.getName().isEmpty()){
                tooltipComponents.add(Component.literal(" "));
                tooltipComponents.add(Component.literal(propertiesDescription.getName()+": ").withStyle(ChatFormatting.DARK_RED).append(Component.literal(propertiesDescription.getDescription()).withStyle(ChatFormatting.GRAY)));
            }
        });
    }

    public List<String> materials(){
        return List.of("iron", "leather");
    }

    protected String itemType(){
        return "item";
    }

    protected String itemSubType(){
        return "misc";
    }

    protected List<PropertiesDescription> additionalProperties(){
        return List.of(new PropertiesDescription("", "nothing"));
    }
}

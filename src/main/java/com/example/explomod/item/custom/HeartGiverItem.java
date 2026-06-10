package com.example.explomod.item.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HeartGiverItem extends Item {
    public HeartGiverItem(Properties properties) {
        super(properties);
    }

    private final ResourceLocation MAX_HEALTH_ID = ResourceLocation.withDefaultNamespace("max_health");
    private final AttributeModifier HEALTH_MOD = new AttributeModifier(
            MAX_HEALTH_ID, 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        AttributeInstance attributeinstance = player.getAttribute(Attributes.MAX_HEALTH);
        assert attributeinstance != null;
        attributeinstance.addTransientModifier(HEALTH_MOD);
        stack.consume(1, player);
        return super.use(level, player, usedHand);
    }
}

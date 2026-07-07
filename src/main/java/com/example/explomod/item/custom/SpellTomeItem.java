package com.example.explomod.item.custom;

import com.example.explomod.component.AtheriaDataComponents;
import com.example.explomod.component.SavedSpells;
import com.example.explomod.registries.Spell;
import com.example.explomod.registries.SpellRegistries;
import com.example.explomod.stats.AtheriaStats;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SpellTomeItem extends Item {
    public SpellTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(AtheriaDataComponents.SPELL, new SavedSpells(SpellRegistries.NULLSPELL));
        return itemstack;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        SavedSpells i = player.getItemInHand(usedHand).get(AtheriaDataComponents.SPELL);
        assert i != null;
        if(player instanceof ServerPlayer serverPlayer){
            serverPlayer.awardStat(AtheriaStats.HIDDEN_STATS.get().get(i.savedSpell().item().asItem()), 1);
        }
        player.getItemInHand(usedHand).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        player.displayClientMessage(Component.translatable("message.explomod.magic.learn"), false);
        return super.use(level, player, usedHand);
    }

    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("message.explomod.magic.learn").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        HolderLookup.Provider registries = context.registries();
        if (registries == null) {
            tooltipComponents.add(Component.literal("Active Spell: Loading...").withStyle(ChatFormatting.GRAY));
            return;
        }
        Optional<HolderLookup.RegistryLookup<Spell>> spellRegistryLookup = registries.lookup(SpellRegistries.CUSTOM_THING_KEY);
        if (spellRegistryLookup.isPresent()) {
            SavedSpells savedSpells = stack.get(AtheriaDataComponents.SPELL.get());
            assert savedSpells != null;
            ResourceLocation spellId = savedSpells.getSpellId().get();
            Optional<HolderLookup.RegistryLookup<Spell>> lookup = spellRegistryLookup;
            var spellElement = spellRegistryLookup.get().get(ResourceKey.create(SpellRegistries.CUSTOM_THING_KEY, spellId));
            if (spellElement.isPresent()) {
                Spell activeSpell = spellElement.get().value();
                tooltipComponents.add(Component.translatable("spell.explomod." + activeSpell.name())
                        .withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
            } else {
                tooltipComponents.add(Component.literal("spell-less").withStyle(ChatFormatting.RED));
            }
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
package com.example.explomod.component;

import com.example.explomod.registries.Spell;
import com.example.explomod.registries.SpellRegistries;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record SavedSpells(Optional<Holder<Spell>> spell) {
    public static final SavedSpells EMPTY = new SavedSpells(Optional.empty());
    private static final Codec<SavedSpells> FULL_CODEC = RecordCodecBuilder.create(
            p_348387_ -> p_348387_.group(
                            Spell.CODEC.optionalFieldOf("spell").forGetter(SavedSpells::spell)
                    )
                    .apply(p_348387_, SavedSpells::new)
    );
    public static final Codec<SavedSpells> CODEC = Codec.withAlternative(FULL_CODEC, Spell.CODEC, SavedSpells::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, SavedSpells> STREAM_CODEC = StreamCodec.composite(
            Spell.STREAM_CODEC.apply(ByteBufCodecs::optional),
            SavedSpells::spell,
            SavedSpells::new
    );

    public SavedSpells(Holder<Spell> p_331208_) {
        this(Optional.of(p_331208_));
    }

    public static ItemStack createItemStack(Item item, Holder<Spell> spell) {
        ItemStack itemstack = new ItemStack(item);
        itemstack.set(AtheriaDataComponents.SPELL, new SavedSpells(spell));
        return itemstack;
    }

    public Spell savedSpell() {
        return this.spell.map(Holder::value).orElse(null);
    }

    public Optional<ResourceLocation> getSpellId() {
        return this.spell.flatMap(Holder::unwrapKey).map(net.minecraft.resources.ResourceKey::location);
    }


    public boolean is(Holder<Spell> spell) {
        return this.spell.isPresent() && this.spell.get().is(spell);
    }

    public SavedSpells withSpell(Holder<Spell> spell) {
        return new SavedSpells(Optional.of(spell));
    }

    public void addSpellTooltip(List<Component> tooltipAdder) {
        addSpellTooltip(this.spell.get().value(), tooltipAdder);
    }

    public static void addSpellTooltip(Spell spell, List<Component> tooltipAdder) {
        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
        tooltipAdder.add(Component.translatable("description.explomod.spell."+spell.name()));
    }
}
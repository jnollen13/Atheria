package com.example.explomod.registries;

import com.example.explomod.sound.ModSounds;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class Spell implements FeatureElement {
    public static final Codec<Spell> CODEC2 = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("name", "unnamed").forGetter(Spell::name),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(Spell::item),
            Codec.INT.optionalFieldOf("cooldown", 1).forGetter(Spell::getCooldown)
    ).apply(instance, Spell::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC2 = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Spell::name,
            ByteBufCodecs.registry(Registries.ITEM), Spell::item,
            ByteBufCodecs.INT, Spell::getCooldown,
            Spell::new
    );
    public static final Codec<Holder<Spell>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Spell>> STREAM_CODEC;
    @Nullable
    private final String name;
    private final int cooldown;
    private final Item item;
    private FeatureFlagSet requiredFeatures;

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo, int cooldownDuration){
        this.name = name;
        this.item = saveTo;
        this.cooldown = cooldownDuration;
    }

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo){
        this.name = name;
        this.item = saveTo;
        this.cooldown = 1;
    }

    public Item item(){
        return this.item;
    }

    public Spell getSpell(){
        return (Spell)/*just in case*/ this;
    }

    public void DoSpell(ServerPlayer player){
        player.level().playSound(player, player.blockPosition(), ModSounds.MAGIC_BLOCK_STEP.get(), SoundSource.PLAYERS);
    }

    public int getCooldown(){
        return this.cooldown;
    }

    public static Spell spell(Item i){
        return SpellRegistries.CUSTOM_THINGS.getEntries().stream()
                // Map the DeferredHolder wrapper down to the actual Spell object
                .map(Holder::value)
                // Filter down to the matching item
                .filter(spell -> spell.isItem(i))
                .findFirst()
                // Gracefully fall back to your NullSpell instead of crashing
                .orElseGet(SpellRegistries.NULLSPELL);
    }

    public static Spell spell(String s){
        return SpellRegistries.CUSTOM_THINGS.getEntries().stream()
                // Map the DeferredHolder wrapper down to the actual Spell object
                .map(Holder::value)
                // Filter down to the matching item
                .filter(spell -> spell.isName(s))
                .findFirst()
                // Gracefully fall back to your NullSpell instead of crashing
                .orElseGet(SpellRegistries.NULLSPELL);
    }

    public boolean isItem(Item item){
        return item == this.item;
    }

    public boolean isName(String name){
        return Objects.equals(name, this.name);
    }

    public static boolean spellsItem(Spell spell, Item item){
        return spell.isItem(item);
    }

    public String name(){
        return this.name;
    }

    @Deprecated
    public static Spell getSpellFromItem(Item item) {
        return SpellRegistries.CUSTOM_THINGS.getEntries().stream()
                .map(Holder::value)
                .filter(spell -> spellsItem(spell, item))
                .findFirst()
                .orElseGet(Spell::deafultSpell);
    }

    public Spell requiredFeatures(FeatureFlag... requiredFeatures) {
        this.requiredFeatures = FeatureFlags.DEFAULT_FLAGS;
        return this;
    }

    public @NotNull FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    private static Spell deafultSpell(){
        return SpellRegistries.NULLSPELL.value();
    }

    static {
        CODEC = SpellRegistries.HOLDER_CODEC;
        STREAM_CODEC = ByteBufCodecs.holderRegistry(SpellRegistries.CUSTOM_THINGS.getRegistryKey());
    }
}

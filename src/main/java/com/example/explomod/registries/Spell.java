package com.example.explomod.registries;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.effect.ModEffects;
import com.example.explomod.payloads.SyncManaPayload;
import com.google.errorprone.annotations.ForOverride;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
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
    private final float manaUse;
    private final int low;
    private FeatureFlagSet requiredFeatures;

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, int minimum){
        this.name = name;
        this.item = saveTo;
        this.cooldown = cooldownDuration;
        this.manaUse=manaUsage;
        this.low=minimum;
    }

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum){
        this.name = name;
        this.item = saveTo;
        this.cooldown = cooldownDuration;
        this.manaUse=manaUsage;
        this.low=minimum;
    }

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage){
        this.name = name;
        this.item = saveTo;
        this.cooldown = cooldownDuration;
        this.manaUse=manaUsage;
        this.low=0;
    }

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage){
        this.name = name;
        this.item = saveTo;
        this.cooldown = cooldownDuration;
        this.manaUse=manaUsage;
        this.low=0;
    }

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo, int cooldownDuration){
        this.name = name;
        this.item = saveTo;
        this.cooldown = cooldownDuration;
        this.manaUse=1;
        this.low=0;
    }

    public Spell(@org.jetbrains.annotations.Nullable String name, @NotNull Item saveTo){
        this.name = name;
        this.item = saveTo;
        this.cooldown = 1;
        this.manaUse=1;
        this.low=0;
    }

    public Item item(){
        return this.item;
    }

    public Spell getSpell(){
        return (Spell)/*just in case*/ this;
    }

    public void DoSpell(Level level, LivingEntity entity){
        entity.level().playSound(entity, entity.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.MASTER, 2.0f, 1.0f);
    }

    protected void DoIntegratedSpell(Level level, LivingEntity caster, LivingEntity target){
        caster.swing(InteractionHand.MAIN_HAND);
        target.swing(InteractionHand.OFF_HAND);
    }

    public void DoTargetedSpell(Level level, LivingEntity target){
        target.level().playSound(target, target.blockPosition(), SoundEvents.HORSE_DEATH, SoundSource.MASTER, 2.0f, 1.0f);
    }

    public boolean canCast(LivingEntity castingEntity) {
        Mana mana = castingEntity.getData(AtheriaDataAttachments.MANA);
        return mana.mana() >= this.low + this.manaUse;
    }

    public boolean canCast(LivingEntity castingEntity, float manaModifier){
        Mana mana = castingEntity.getData(AtheriaDataAttachments.MANA);
        return mana.mana() >= this.low + (this.manaUse*manaModifier);
    }

    public float manaUsage(){
        return manaUse;
    }

    public int getCooldown(){
        return this.cooldown;
    }

    public String descriptionId(){
        return "spell.explomod."+this.name;
    }

    private Component translated(){
        return Component.translatable(this.descriptionId());
    }

    public Component displayName(boolean formated){
        if(!formated) {
            return translated();
        }else{
            return ComponentUtils.wrapInSquareBrackets(Component.translatable(descriptionId())).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
        }
    }

    public Component spellDescription(boolean formated){
        if(formated){
            return Component.translatable(descriptionId() + ".desc").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
        }else {
            return Component.translatable(descriptionId() + ".desc");
        }
    }

    public ResourceLocation spellImageLocation(String nameSpace){
        return ResourceLocation.fromNamespaceAndPath(nameSpace, "textures/spells/"+this.name+".png");
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated(forRemoval = true)
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

    @Deprecated
    private static Spell deafultSpell(){
        return SpellRegistries.NULLSPELL.value();
    }

    private void subtractMana(LivingEntity entity, float manaReduction){
        float currentMana = entity.getData(AtheriaDataAttachments.MANA).mana();
        entity.setData(AtheriaDataAttachments.MANA.get(), new Mana(currentMana-manaReduction));
        if(entity instanceof ServerPlayer serverPlayer) {
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new SyncManaPayload(currentMana-manaReduction)
            );
        }
    }

    public static Spell safeDeafultSpell(){
        return SpellRegistries.NULL_SPELL;
    }

    static {
        CODEC = SpellRegistries.HOLDER_CODEC;
        STREAM_CODEC = ByteBufCodecs.holderRegistry(SpellRegistries.CUSTOM_THINGS.getRegistryKey());
    }

    private static void notify(LivingEntity entity, Spell spell){
        if(entity instanceof Player player){
            player.displayClientMessage(Component.literal(Component.translatable("spell.target.self")+": "+spell.displayName(true)), true);
        }
    }

    private void cooldownChecker(CooldownType type, LivingEntity entity, int length){
        if(type==CooldownType.EFFECT){
            entity.addEffect(new MobEffectInstance(ModEffects.COOLDOWN, length));
        } else if (type==CooldownType.ITEM) {
            if(entity instanceof Player caster){
                caster.getCooldowns().addCooldown(caster.getUseItem().getItem(), length);
            }
        } else if (type==CooldownType.BOTH) {
            entity.addEffect(new MobEffectInstance(ModEffects.COOLDOWN, length));
            if(entity instanceof Player caster){
                caster.getCooldowns().addCooldown(caster.getUseItem().getItem(), length);
            }
        } else{
            entity.makeSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE);
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type){
        if(canCast(caster)){
            this.DoSpell(level, caster);
            cooldownChecker(type, caster, this.cooldown);
            subtractMana(caster, this.manaUse);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type, double cooldownModifier){
        if(canCast(caster)){
            this.DoSpell(level, caster);
            cooldownChecker(type, caster, this.cooldown*(int) cooldownModifier);
            subtractMana(caster, this.manaUse);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type, float manaModifier){
        if(canCast(caster, manaModifier)){
            this.DoSpell(level, caster);
            cooldownChecker(type, caster, this.cooldown);
            subtractMana(caster, this.manaUse*manaModifier);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type, double cooldownModifier, float manaModifier){
        if(canCast(caster, manaModifier)){
            this.DoSpell(level, caster);
            cooldownChecker(type, caster, this.cooldown*(int) cooldownModifier);
            subtractMana(caster, this.manaUse*manaModifier);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type, LivingEntity target){
        if(canCast(caster)){
            this.DoSpell(level, caster);
            notify(target, this.getSpell());
            this.DoTargetedSpell(level, target);
            cooldownChecker(type, caster, this.cooldown);
            subtractMana(caster, this.manaUse);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type, LivingEntity target, double cooldownModifier){
        if(canCast(caster)){
            this.DoSpell(level, caster);
            notify(target, this.getSpell());
            this.DoTargetedSpell(level, target);
            cooldownChecker(type, caster, this.cooldown*(int) cooldownModifier);
            subtractMana(caster, this.manaUse);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type, LivingEntity target, float manaModifier){
        if(canCast(caster, manaModifier)){
            this.DoSpell(level, caster);
            notify(target, this.getSpell());
            this.DoTargetedSpell(level, target);
            cooldownChecker(type, caster, this.cooldown);
            subtractMana(caster, this.manaUse*manaModifier);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpell(Level level, LivingEntity caster, CooldownType type, LivingEntity target, double cooldownModifier, float manaModifier){
        if(canCast(caster, manaModifier)){
            this.DoSpell(level, caster);
            notify(target, this.getSpell());
            this.DoTargetedSpell(level, target);
            cooldownChecker(type, caster, this.cooldown*(int) cooldownModifier);
            subtractMana(caster, this.manaUse*manaModifier);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpellIntegrated(Level level, LivingEntity caster, CooldownType type, LivingEntity target, double cooldownModifier, float manaModifier){
        if(canCast(caster, manaModifier)){
            DoIntegratedSpell(level, caster, target);
            notify(target, this.getSpell());
            cooldownChecker(type, caster, this.cooldown*(int) cooldownModifier);
            subtractMana(caster, this.manaUse*manaModifier);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpellIntegrated(Level level, LivingEntity caster, CooldownType type, LivingEntity target, double cooldownModifier){
        if(canCast(caster)){
            DoIntegratedSpell(level, caster, target);
            notify(target, this.getSpell());
            cooldownChecker(type, caster, this.cooldown*(int) cooldownModifier);
            subtractMana(caster, this.manaUse);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpellIntegrated(Level level, LivingEntity caster, CooldownType type, LivingEntity target, float manaModifier){
        if(canCast(caster, manaModifier)){
            DoIntegratedSpell(level, caster, target);
            notify(target, this.getSpell());
            cooldownChecker(type, caster, this.cooldown);
            subtractMana(caster, this.manaUse*manaModifier);
            return true;
        }else {
            return false;
        }
    }

    public boolean tryCastSpellIntegrated(Level level, LivingEntity caster, CooldownType type, LivingEntity target){
        if(canCast(caster)){
            DoIntegratedSpell(level, caster, target);
            notify(target, this.getSpell());
            cooldownChecker(type, caster, this.cooldown);
            subtractMana(caster, this.manaUse);
            return true;
        }else {
            return false;
        }
    }

    public enum CooldownType{
        EFFECT,
        NONE,
        ITEM,
        BOTH
    }
}

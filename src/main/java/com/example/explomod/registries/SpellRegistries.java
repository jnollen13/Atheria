package com.example.explomod.registries;

import com.example.explomod.ExploMod;
import com.example.explomod.spells.FlySpell;
import com.example.explomod.spells.SpellList;
import com.example.explomod.spells.Spells;
import com.example.explomod.spells.UnSummoningSpell;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.List;

import static com.example.explomod.spells.Spells.*;

public class SpellRegistries {
    public static final ResourceKey<Registry<Spell>> SPELL_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "spell"));

    public static final DeferredRegister<Spell> CUSTOM_THINGS =
            DeferredRegister.create(SPELL_KEY, ExploMod.MODID);

    public static final DeferredHolder<Spell, Spell> NULLSPELL =
            CUSTOM_THINGS.register("null", () -> new Spell("null", Items.AIR));

    // public static final Holder<Spell> TEST_SPELL = CUSTOM_THINGS.register("test", () -> new FlySpell("test", Items.BAKED_POTATO, 1));
    // public static final Holder<Spell> TEST_SPELL2 = CUSTOM_THINGS.register("test2", () -> new Spell("test2", Items.POISONOUS_POTATO, 1));
    public static final Holder<Spell> AIRBURST_SPELL = CUSTOM_THINGS.register("airburst", () -> new Spell("airburst", Items.BONE_MEAL));
    public static final Holder<Spell> FIREBALL_SPELL = CUSTOM_THINGS.register("fireball", () -> new Spell("fireball", Items.FIRE_CHARGE));
    public static final Holder<Spell> DASH_SPELL = CUSTOM_THINGS.register("dash", () -> new Spell("dash", Items.LEATHER_BOOTS));
    // public static final Holder<Spell> UNSUMMONING = CUSTOM_THINGS.register("unsummoning", () -> new UnSummoningSpell("unsummoning", ExploMod.UNSUMMONING_STAFF.asItem(), 1000,50,-5));

    public static final Spell UNSUMMONING_SPELL = new UnSummoningSpell("unsummoning", Items.TOTEM_OF_UNDYING, 1000,50,-5);
    public static final Spell NULL_SPELL =  new Spell("null", Items.AIR);

    public static SpellList SPELLS = new SpellList(List.of(NULL_SPELL, UNSUMMONING_SPELL, Spells.FLYSPELL, HEAL_I,HEAL_II,HEAL_III,HEAL_IV,HEAL_V));

    public static final Codec<Holder<Spell>> HOLDER_CODEC =
            RegistryFixedCodec.create(SPELL_KEY);

    public static void register(IEventBus eventBus) {
        CUSTOM_THINGS.register(eventBus);

        eventBus.addListener(SpellRegistries::registerDatapackRegistry);
    }

    private static void registerDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                SPELL_KEY,
                Spell.CODEC2,
                Spell.CODEC2
        );
    }
}

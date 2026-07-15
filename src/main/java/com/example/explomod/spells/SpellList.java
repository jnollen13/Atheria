package com.example.explomod.spells;

import com.example.explomod.registries.Spell;
import com.example.explomod.registries.SpellRegistries;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class SpellList {
    private List<Spell> regsteredSpells;

    public SpellList(List<Spell> spells){
        this.regsteredSpells = spells;
    }

    public void register(Spell addSpell){
     regsteredSpells.add(addSpell);
    }

    public void register(Spell addSpell, Spell addSpell2){
        regsteredSpells.add(addSpell);
        regsteredSpells.add(addSpell2);
    }

    public void register(List<Spell> addSpells){
        regsteredSpells.addAll(addSpells);
    }

    public void register(Collection<Spell> addSpells){
        regsteredSpells.addAll(addSpells);
    }

    public void forEach(Consumer<? super Spell> action){
        regsteredSpells.forEach(action);
    }

    public boolean isSpellRegistered(Spell searchSpell){
        return regsteredSpells.contains(searchSpell);
    }

    public Spell getAt(int i){
        return regsteredSpells.get(i);
    }

    public Spell getFirst(){
        return regsteredSpells.getFirst();
    }

    public Spell getLast(){
        return regsteredSpells.getLast();
    }

    public Spell getSpellFromItem(Item i){
        return regsteredSpells.stream().filter(spell -> spell.isItem(i)).findFirst().orElse(SpellRegistries.NULL_SPELL);
    }

    public Spell getSpellFromName(String name){
        return regsteredSpells.stream().filter(spell -> spell.isName(name)).findFirst().orElse(SpellRegistries.NULL_SPELL);
    }
}

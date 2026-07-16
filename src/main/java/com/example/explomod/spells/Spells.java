package com.example.explomod.spells;

import com.example.explomod.item.custom.magic.SimpleSpellItem;
import com.example.explomod.registries.Spell;
import com.example.explomod.registries.SpellRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.http.annotation.Experimental;

import java.util.List;

public class Spells{
    public static final Spell FLYSPELL = new FlySpell("flight", Items.ELYTRA, 10);
    public static final Spell HEAL_I = new HealSpell("heal1", Items.GLISTERING_MELON_SLICE, 40, 2, -2, 2f);
    public static final Spell HEAL_II = new HealSpell("heal2", Items.GLISTERING_MELON_SLICE, 40, 4, -3, 4f);
    public static final Spell HEAL_III = new HealSpell("heal3", Items.GLISTERING_MELON_SLICE, 40, 6, -4, 6f);
    public static final Spell HEAL_IV = new HealSpell("heal4", Items.GLISTERING_MELON_SLICE, 40, 8, -5, 8f);
    public static final Spell HEAL_V = new HealSpell("heal5", Items.GLISTERING_MELON_SLICE, 40, 10, -6, 10f);
    public static final Spell MANA_DRAIN = new DrainSpell("mana_drain", Items.HOPPER, 100, 9, -9, true);
    public static final Spell HEALTH_DRAIN = new DrainSpell("health_drain", Items.HOPPER_MINECART, 100, 10.36f, -5, false);

    @Experimental
    public static Item setSpell(Item i, Spell spell){
        if(i instanceof SimpleSpellItem spellItem){
            return spellItem.withSpell(spell);
        }else{
            return i;
        }
    }
}

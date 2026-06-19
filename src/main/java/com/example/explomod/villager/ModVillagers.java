package com.example.explomod.villager;

import com.example.explomod.ExploMod;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, ExploMod.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
         DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, ExploMod.MODID);

    public static final Holder<PoiType> MODSELLER_POI = POI_TYPES.register("modseller_poi",
            () ->  new PoiType(ImmutableSet.copyOf(ExploMod.TABLE.get().getStateDefinition().getPossibleStates()),2,2));

    public static final Holder<PoiType> DARK_PORTAL_POI = POI_TYPES.register("dark_portal_poi",
            () ->  new PoiType(ImmutableSet.copyOf(ExploMod.DARK_PORTAL.get().getStateDefinition().getPossibleStates()),1,1));

    public static final Holder<VillagerProfession> MODSELLER = VILLAGER_PROFESSIONS.register("modseller",
            () -> new VillagerProfession("modseller", holder -> holder.value() == MODSELLER_POI.value(),
        poiTypeHolder -> poiTypeHolder.value() == MODSELLER_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.AMETHYST_BLOCK_CHIME));


    public static void register(IEventBus eventBus) {
        VILLAGER_PROFESSIONS.register(eventBus);
        POI_TYPES.register(eventBus);

    }
}

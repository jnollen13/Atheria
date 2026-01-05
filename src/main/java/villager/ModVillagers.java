package villager;

import com.example.explomod.ExploMod;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import sound.ModSounds;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, ExploMod.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
         DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, ExploMod.MODID);

    public static final Holder<PoiType> KAUPEN_POI = POI_TYPES.register("kaupen_poi",
            () ->  new PoiType(ImmutableSet.copyOf(ExploMod.LOG_BLOCK.get().getStateDefinition().getPossibleStates()),1,1));

    public static final Holder<VillagerProfession> MODSELLER = VILLAGER_PROFESSIONS.register("modseller",
            () -> new VillagerProfession("modseller", holder -> holder.value() == KAUPEN_POI.value(),
                    poiTypeHolder -> poiTypeHolder.value() == KAUPEN_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                    ModSounds.MAGIC_BLOCK_HIT.get()));



    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}

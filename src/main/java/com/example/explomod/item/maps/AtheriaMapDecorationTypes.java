package com.example.explomod.item.maps;

import com.example.explomod.ExploMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtheriaMapDecorationTypes {
    public static final DeferredRegister<MapDecorationType> MAP_DECORATION = DeferredRegister.create(Registries.MAP_DECORATION_TYPE, ExploMod.MODID);

    public static final DeferredHolder<MapDecorationType, MapDecorationType> VENDOR_STALL = MAP_DECORATION.register("vendor_stall", () -> new MapDecorationType(assetId("vendor_stall"), true, 12893081, false, true));
    public static final DeferredHolder<MapDecorationType, MapDecorationType> PARKOUR_TOWER = MAP_DECORATION.register("parkour_tower", () -> new MapDecorationType(assetId("parkour_tower"), true, 8816520, false, true));
    public static final DeferredHolder<MapDecorationType, MapDecorationType> DARK_PORTAL = MAP_DECORATION.register("dark_portal", () -> new MapDecorationType(assetId("dark_portal"), true, 3951223, false, true));
    public static final DeferredHolder<MapDecorationType, MapDecorationType> DUNGEON = MAP_DECORATION.register("dungeon", () -> new MapDecorationType(assetId("dungeon"), true, 7500660, false, true));
    public static final DeferredHolder<MapDecorationType, MapDecorationType> SHIP = MAP_DECORATION.register("ship", () -> new MapDecorationType(assetId("ship"), true, 2704624, false, true));
    public static final DeferredHolder<MapDecorationType, MapDecorationType> GRASS_GOLEM_CAMP = MAP_DECORATION.register("grass_golem_camp", () -> new MapDecorationType(assetId("grass_golem_camp"), true, 3967576, true, true));
    public static final DeferredHolder<MapDecorationType, MapDecorationType> MONSTER_OUTPOST = MAP_DECORATION.register("monster_outpost", () -> new MapDecorationType(assetId("monster_outpost"), true, 6844251, false, true));
    public static final DeferredHolder<MapDecorationType, MapDecorationType> ATTACK = MAP_DECORATION.register("attack", () -> new MapDecorationType(assetId("attack"), true, 13766953, false, true));

    private static ResourceLocation assetId(String name){
        return ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name);
    }

    public static void register(IEventBus eventBus){
        MAP_DECORATION.register(eventBus);
    }
}

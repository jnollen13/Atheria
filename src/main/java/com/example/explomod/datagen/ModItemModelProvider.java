package com.example.explomod.datagen;

import com.example.explomod.ExploMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExploMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ExploMod.BLOODWOOD_DOOR.asItem());
        basicItem(ExploMod.RADIUM_FIRESTARTER.asItem());
        basicItem(ExploMod.RADIUM_NUGGET.asItem());
        basicItem(ExploMod.FULL_MOON.asItem());
        handheldItem(ExploMod.GRASSY_SWORD);
        handheldItem(ExploMod.GRASS_AXE);
        handheldItem(ExploMod.GRASS_HOE);
        handheldItem(ExploMod.GRASS_PICKAXE);
        handheldItem(ExploMod.GRASS_SHOVEL);
        withExistingParent(ExploMod.GECKO_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ExploMod.GRASS_GOLEM_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ExploMod.LPHANTOM_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ExploMod.TRADER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ExploMod.BLOCK_BIRD_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        structureMap(ExploMod.ATHERIA_STRUCTURE_MAP);
        structureMap(ExploMod.DARK_PORTAL_MAP);
        structureMap(ExploMod.DUNGEON_MAP);
        structureMap(ExploMod.PARKOUR_TOWER_MAP);
        structureMap(ExploMod.SHIP_MAP);
        structureMap(ExploMod.GRASS_GOLEM_CAMP_MAP);
        structureMap(ExploMod.MONSTER_OUTPOST_MAP);
        structureMap(ExploMod.JUNGLE_EXPLORER_MAP);
        structureMap(ExploMod.OCEAN_EXPLORER_MAP);
        spellCoin(ExploMod.LIGHTNING_CUBE_COIN);
    }


    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(ExploMod.MODID,"item/" + item.getId().getPath()));
    }
    protected ItemModelBuilder structureMap(DeferredItem<?> item){
        return withExistingParent(item.getId().getPath(), ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "item/structure_map"));
    }
    protected ItemModelBuilder spellCoin(DeferredItem<?> item){
        return withExistingParent(item.getId().getPath(), ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "item/token"));
    }
}

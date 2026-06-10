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
        withExistingParent(ExploMod.GECKO_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ExploMod.GRASS_GOLEM_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ExploMod.LPHANTOM_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ExploMod.TRADER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }


    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(ExploMod.MODID,"item/" + item.getId().getPath()));
    }
}

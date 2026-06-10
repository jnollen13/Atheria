package com.example.explomod.datagen;

import com.example.explomod.ExploMod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExploMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        doorBlockWithRenderType(ExploMod.BLOODWOOD_DOOR.get(), modLoc("block/logside"), modLoc("block/bloodwood_door_top"), "cutout");
        buttonBlock(ExploMod.BLOODWOOD_BUTTON.get(), blockTexture(ExploMod.LOG_BLOCK.get()));
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("explomod:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("explomod:block/" + deferredBlock.getId().getPath() + appendix));
    }
}

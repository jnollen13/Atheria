package datagen;

import com.example.explomod.ExploMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
        protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            dropSelf(ExploMod.PHANTOM_BLOCK.get());
            dropSelf(ExploMod.TABLE.get());
            dropSelf(ExploMod.NEW_LAMP.get());
            dropSelf(ExploMod.DENSE_GRASS.get());
            dropSelf(ExploMod.LAVA_HOLDER.get());
            dropSelf(ExploMod.STONED.get());
            dropSelf(ExploMod.KIT.get());
            dropSelf(ExploMod.BLOODWOOD_DOOR.get());
            dropSelf(ExploMod.LOG_BLOCK.get());
            dropSelf(ExploMod.XRAY_BLOCK.get());
            dropSelf(ExploMod.BLOODWOOD_SAPLING.get());
            dropSelf(ExploMod.STORMBERRY_BUSH.get());
            dropSelf(ExploMod.REDTRAV.get());
            dropSelf(ExploMod.WINTER_OAK_SAPLING.get());
            this.add(ExploMod.FLOWERED_LEAVES.get(), block ->
                    createLeavesDrops(block, ExploMod.WINTER_OAK_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
            dropSelf(ExploMod.TANK_BLOCK.get());
            dropSelf(ExploMod.STOOL.get());
            dropSelf(ExploMod.GROUND.get());
            dropOther(ExploMod.GLOWBERRY_BUSH.get(), Items.GLOW_BERRIES);
            dropSelf(ExploMod.STORMBERRY_CAKE.get());
            dropSelf(ExploMod.BLOODWOOD_BUTTON.get());
            add(ExploMod.EXAMPLE_BLOCK.get(),
                    block -> createOreDrop(ExploMod.EXAMPLE_BLOCK.get(), ExploMod.EXAMPLE_ITEM.get()));
            add(ExploMod.RADIUM_ORE.get(),
                    block -> createOreDrop(ExploMod.RADIUM_ORE.get(), ExploMod.RAW_RADIUM.get()));
        }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return ExploMod.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
        }
}

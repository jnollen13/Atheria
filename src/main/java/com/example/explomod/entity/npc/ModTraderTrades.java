package com.example.explomod.entity.npc;

import com.example.explomod.ExploMod;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Optional;

public class ModTraderTrades extends VillagerTrades {
    private static final int DEFAULT_SUPPLY = 12;
    private static final int COMMON_ITEMS_SUPPLY = 16;
    private static final int UNCOMMON_ITEMS_SUPPLY = 3;
    private static final int XP_LEVEL_1_SELL = 1;
    private static final int XP_LEVEL_1_BUY = 2;
    private static final int XP_LEVEL_2_SELL = 5;
    private static final int XP_LEVEL_2_BUY = 10;
    private static final int XP_LEVEL_3_SELL = 10;
    private static final int XP_LEVEL_3_BUY = 20;
    private static final int XP_LEVEL_4_SELL = 15;
    private static final int XP_LEVEL_4_BUY = 30;
    private static final int XP_LEVEL_5_TRADE = 30;
    private static final float LOW_TIER_PRICE_MULTIPLIER = 0.05F;
    private static final float HIGH_TIER_PRICE_MULTIPLIER = 0.2F;
                public static final Int2ObjectMap<ModTraderTrades.ItemListing[]> TRADER_TRADES = toIntMap(
            ImmutableMap.of(
                    1,
                    new ModTraderTrades.ItemListing[]{
                            new ModTraderTrades.ItemsForEmeralds(Items.SLIME_BALL, 4, 1, 5, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.BLOODWOOD_DOOR_ITEM.get(), 2, 2, 2, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.LOG_BLOCK.get(), 2, 6, 5, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.FERMENTED_GLOWSTONE.get(), 4, 1, 3, 1),
                            new ModTraderTrades.EmeraldForItems(Items.CHAIN, 16, 5, 2),
                            new ModTraderTrades.EmeraldForItems(Items.LAPIS_LAZULI, 16, 5, 2),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.BLOODWOOD_BUTTON_ITEM.get(), 1, 2, 5, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.RAW_RADIUM.get(), 3, 1, 4, 1),
                            new ModTraderTrades.EnchantBookForEmeralds(1, 1, 5, EnchantmentTags.CURSE),
                            new ModTraderTrades.EnchantBookForEmeralds(1, 1, 2, EnchantmentTags.NON_TREASURE),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.MELON_JUICE.get(), 2, 1, 3, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.MELON_JUICE_HALF_EMPTY.get(), 1, 1, 1, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.COPPER_PICKAXE.get(), 1, 1, 2, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.FERMENTED_MELON_JUICE.get(), 3, 1, 5, 2),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.STORM_BERRY_ROLL.get(), 8, 1, 4, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.STORM_BERRY.get(), 2, 1, 15, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.STORMBERRY_COOKIE.get(), 3, 1, 5, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.PHANTOM_SHOVEL.get(), 5, 1, 1, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.EMPTY_CRATE_ITEM.get(), 5, 1, 1, 1),
                    },
                    2,
                    new ModTraderTrades.ItemListing[]{
                            new ModTraderTrades.ItemsForEmeralds(Items.AMETHYST_SHARD, 3, 1, 1, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.STOOL_ITEM.get(), 6, 1, 6, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.CRATE_ITEM.get(), 10, 1, 1, 1),
                            new ModTraderTrades.ItemsForEmeralds(Items.CHAINMAIL_HELMET, 5, 1, 1, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.TABLE_ITEM.get(), 1, 2, 6, 1),
                            new ModTraderTrades.ItemsForEmeralds(ExploMod.STRANGE_ARROW.get(), 16, 1, 1, 1),
                            new ModTraderTrades.EnchantBookForEmeralds(1, 1, 6, EnchantmentTags.CURSE),
                    }
            )
    );
    public static class EnchantBookForEmeralds implements ItemListing {
        private final int villagerXp;
        private final TagKey<Enchantment> tradeableEnchantments;
        private final int minLevel;
        private final int maxLevel;

        public EnchantBookForEmeralds(int villagerXp, TagKey<Enchantment> tradeableEnchantments) {
            this(villagerXp, 0, Integer.MAX_VALUE, tradeableEnchantments);
        }

        public EnchantBookForEmeralds(int villagerXp, int minLevel, int maxLevel, TagKey<Enchantment> tradeableEnchantments) {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.villagerXp = villagerXp;
            this.tradeableEnchantments = tradeableEnchantments;
        }

        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            Optional<Holder<Enchantment>> optional = trader.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getRandomElementOf(this.tradeableEnchantments, random);
            int i;
            ItemStack itemstack;
            if (!optional.isEmpty()) {
                Holder<Enchantment> holder = (Holder)optional.get();
                Enchantment enchantment = (Enchantment)holder.value();
                int j = Math.max(enchantment.getMinLevel(), this.minLevel);
                int k = Math.min(enchantment.getMaxLevel(), this.maxLevel);
                int l = Mth.nextInt(random, j, k);
                itemstack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(holder, l));
                i = 2 + random.nextInt(5 + l * 10) + 3 * l;
                if (holder.is(EnchantmentTags.CURSE)) {
                    i *= 0.25;
                }

                if (i > 64) {
                    i = 64;
                }
            } else {
                i = 1;
                itemstack = new ItemStack(Items.BOOK);
            }

            return new MerchantOffer(new ItemCost(Items.EMERALD, i), Optional.of(new ItemCost(Items.BOOK)), itemstack, 12, this.villagerXp, 0.2F);
        }
    }

    private static Int2ObjectMap<ModTraderTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, ModTraderTrades.ItemListing[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }
}

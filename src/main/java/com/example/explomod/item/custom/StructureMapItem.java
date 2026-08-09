package com.example.explomod.item.custom;

import com.example.explomod.component.AtheriaDataComponents;
import com.example.explomod.item.maps.AtheriaMapDecorationTypes;
import com.example.explomod.utill.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class StructureMapItem extends Item {
    private final TagKey<Structure> destination;
    private final Holder<MapDecorationType> destinationType;
    private final String displayName;

    public StructureMapItem(TagKey<Structure> destination, String displayName, Holder<MapDecorationType> destinationType, Properties properties) {
        super(properties);
        this.destination=destination;
        this.destinationType=destinationType;
        this.displayName=displayName;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if (!(player.level() instanceof ServerLevel)) {
            return InteractionResultHolder.fail(player.getItemInHand(usedHand));
        } else {
            ServerLevel serverlevel = (ServerLevel)player.level();
            BlockPos blockpos = serverlevel.findNearestMapStructure(this.destination, player.blockPosition(), 1024, true);
            if (blockpos != null) {
                ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte) RandomSource.create().nextIntBetweenInclusive(1,4), true, true);
                MapItem.renderBiomePreviewMap(serverlevel, itemstack);
                MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
                itemstack.set(DataComponents.ITEM_NAME, Component.translatable(this.displayName));
                itemstack.set(AtheriaDataComponents.COORDINATES.get(), player.blockPosition());
                player.getItemInHand(usedHand).consume(1, player);
                player.addItem(itemstack);
                return InteractionResultHolder.success(player.getItemInHand(usedHand));
            } else {
                return InteractionResultHolder.fail(player.getItemInHand(usedHand));
            }
        }
    }
}

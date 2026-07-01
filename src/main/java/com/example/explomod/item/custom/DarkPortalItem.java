package com.example.explomod.item.custom;

import com.example.explomod.ExploMod;
import com.example.explomod.block.custom.DarkPortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class DarkPortalItem extends Item {
    public DarkPortalItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            if (this.createPortalFrame(context)) {
                player.swing(context.getHand());
                if (!player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.FAIL;
    }

    /**
     * Creates an Aether Portal.
     *
     * @param context The {@link UseOnContext} of the usage interaction.
     * @return Whether the portal frame can be placed, as a {@link Boolean}.
     */
    private boolean createPortalFrame(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos).canBeReplaced(new BlockPlaceContext(context))) {
            pos = pos.relative(context.getClickedFace()); // If the block can't be placed at the clicked position, it'll choose a relative position based on the clicked face.
        }
        Direction.Axis axis = context.getHorizontalDirection().getAxis();

        for (int h = -1; h < 3; h++) {
            for (int v = pos.getY(); v < pos.getY() + 5; v++) {
                BlockPos truePos = axis == Direction.Axis.X ? new BlockPos(pos.getX(), v, pos.getZ() + h) : new BlockPos(pos.getX() + h, v, pos.getZ());
                if (!level.getBlockState(truePos).canBeReplaced(new BlockPlaceContext(context))) {
                    return false; // If any blocks in the frame positions region can't be replaced, then the portal can't be placed.
                }
            }
        }

        for (int h = -1; h < 3; h++) {
            for (int v = pos.getY(); v < pos.getY() + 5; v++) {
                BlockPos truePos = axis == Direction.Axis.X ? new BlockPos(pos.getX(), v, pos.getZ() + h) : new BlockPos(pos.getX() + h, v, pos.getZ());
                level.setBlockAndUpdate(truePos, Blocks.DARK_PRISMARINE.defaultBlockState());
            }
        }

        for (int h = 0; h < 2; h++) {
            for (int v = pos.getY() + 1; v < pos.getY() + 4; v++) {
                BlockPos truePos = axis == Direction.Axis.X ? new BlockPos(pos.getX(), v, pos.getZ() + h) : new BlockPos(pos.getX() + h, v, pos.getZ());
                Direction.Axis trueAxis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                level.setBlock(truePos, ExploMod.DARK_PORTAL.get().defaultBlockState().setValue(DarkPortalBlock.AXIS, trueAxis), 2 | 16);
            }
        }

        return true;
    }
}

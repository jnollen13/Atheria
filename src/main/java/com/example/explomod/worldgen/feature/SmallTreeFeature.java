package com.example.explomod.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SmallTreeFeature extends Feature<TwoBlockConfiguration> {
    public SmallTreeFeature(Codec< TwoBlockConfiguration > p_66808_) {
        super(p_66808_);
    }

    @Override
    public boolean place(FeaturePlaceContext<TwoBlockConfiguration> p_160341_) {
        TwoBlockConfiguration twoBlockConfiguration = p_160341_.config();
        WorldGenLevel worldgenlevel = p_160341_.level();
        BlockPos blockpos = p_160341_.origin();
        BlockPos blockpos2 = p_160341_.origin().above(1);
        BlockPos blockpos3 = p_160341_.origin().above(2);
        BlockPos blockpos3n = p_160341_.origin().above(2).north(1);
        BlockPos blockpos3e = p_160341_.origin().above(2).east(1);
        BlockPos blockpos3s = p_160341_.origin().above(2).south(1);
        BlockPos blockpos3w = p_160341_.origin().above(2).west(1);
        BlockPos blockpos4 = p_160341_.origin().above(3);
        BlockState blockstate = twoBlockConfiguration.toPlace().getState(p_160341_.random(), blockpos);
        BlockState blockstate2 = twoBlockConfiguration.top().getState(p_160341_.random(), blockpos2);
        if (blockstate.canSurvive(worldgenlevel, blockpos)&&blockstate.canSurvive(worldgenlevel, blockpos2)&&blockstate.canSurvive(worldgenlevel, blockpos3)
                &&blockstate2.canSurvive(worldgenlevel, blockpos4)&&blockstate2.canSurvive(worldgenlevel, blockpos3n)&&blockstate2.canSurvive(worldgenlevel, blockpos3e)
                &&blockstate2.canSurvive(worldgenlevel, blockpos3s)&&blockstate2.canSurvive(worldgenlevel, blockpos3w)) {
            if (blockstate.getBlock() instanceof DoublePlantBlock) {
                return false;
            }else if (blockstate2.getBlock() instanceof DoublePlantBlock) {
                return false;
            } else {
                worldgenlevel.setBlock(blockpos, blockstate, 2);
                worldgenlevel.setBlock(blockpos2, blockstate, 2);
                worldgenlevel.setBlock(blockpos3, blockstate, 2);
                worldgenlevel.setBlock(blockpos4, blockstate2, 2);
                worldgenlevel.setBlock(blockpos3n, blockstate2, 2);
                worldgenlevel.setBlock(blockpos3e, blockstate2, 2);
                worldgenlevel.setBlock(blockpos3s, blockstate2, 2);
                worldgenlevel.setBlock(blockpos3w, blockstate2, 2);
            }

            return true;
        } else {
            return false;
        }
    }
}

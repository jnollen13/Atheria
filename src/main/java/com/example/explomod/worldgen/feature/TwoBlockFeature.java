package worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class TwoBlockFeature extends Feature<TwoBlockConfiguration> {
    public TwoBlockFeature(Codec< TwoBlockConfiguration > p_66808_) {
        super(p_66808_);
    }

    @Override
    public boolean place(FeaturePlaceContext<TwoBlockConfiguration> p_160341_) {
        TwoBlockConfiguration twoBlockConfiguration = p_160341_.config();
        WorldGenLevel worldgenlevel = p_160341_.level();
        BlockPos blockpos = p_160341_.origin();
        BlockPos blockpos2 = p_160341_.origin().above(1);
        BlockState blockstate = twoBlockConfiguration.toPlace().getState(p_160341_.random(), blockpos);
        BlockState blockstate2 = twoBlockConfiguration.top().getState(p_160341_.random(), blockpos2);
        if (blockstate.canSurvive(worldgenlevel, blockpos)&&blockstate2.canSurvive(worldgenlevel, blockpos2)) {
            if (blockstate.getBlock() instanceof DoublePlantBlock) {
                return false;
            }else if (blockstate2.getBlock() instanceof DoublePlantBlock) {
                if (!worldgenlevel.isEmptyBlock(blockpos2.above())) {
                    return false;
                }
                DoublePlantBlock.placeAt(worldgenlevel, blockstate2, blockpos2, 2);
            } else {
                worldgenlevel.setBlock(blockpos, blockstate, 2);
                worldgenlevel.setBlock(blockpos2, blockstate2, 2);
            }

            return true;
        } else {
            return false;
        }
    }
}


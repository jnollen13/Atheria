package com.example.explomod.block.custom.crates;

import com.example.explomod.block.custom.crates.template.TemplateCrateBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class AppleCrateBlock extends TemplateCrateBlock {
    public AppleCrateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Item getItem() {
        return Items.APPLE;
    }

    // optional
    @Override
    public IntegerProperty getType() {
        return IntegerProperty.create("apples", MIN, getMax());
    }

    @Override
    public int getMax() {
        return 1;
    }
}

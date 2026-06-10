package com.example.explomod.block.custom.crates;

import com.example.explomod.block.custom.crates.template.TemplateCrateBlock;

public class AppleCrateBlock extends TemplateCrateBlock {
    public AppleCrateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMax() {
        return 1;
    }
}

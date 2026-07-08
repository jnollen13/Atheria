package com.example.explomod.block.custom.crates.template;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CrateData {
    private final Item itemList;
    private final Block blockList;

    public CrateData(Item i, Block b){
        this.itemList = i;
        this.blockList = b;
    }

    public Block getBlock() {
        return blockList;
    }

    public Item getItem() {
        return itemList;
    }

    public CrateData getCrateData(){
        return new CrateData(itemList, blockList);
    }
}

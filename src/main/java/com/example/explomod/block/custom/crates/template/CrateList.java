package com.example.explomod.block.custom.crates.template;

import net.minecraft.world.level.block.Block;

import java.util.List;

public class CrateList {
    private final List<CrateData> crates;
    private int crateNumber;

    public CrateList(List<CrateData> crates){
        this.crates = crates;
        crateNumber=crates.toArray().length;
    }

    public List<CrateData> getCrates() {
        return crates;
    }

    public void addCrate(CrateData b){
        crates.add(b);
        crateNumber++;
    }
    public boolean isCrate(Block b){
        return crates.contains(b);
    }

    public int getCrateNumber() {
        return crateNumber;
    }

    public CrateData get(int i){
        return crates.get(i);
    }
}

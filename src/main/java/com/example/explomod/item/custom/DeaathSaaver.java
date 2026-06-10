package com.example.explomod.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class DeaathSaaver extends Item {
    public DeaathSaaver(Properties properties) {
        super(properties);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(Items.TOTEM_OF_UNDYING);
    }
}

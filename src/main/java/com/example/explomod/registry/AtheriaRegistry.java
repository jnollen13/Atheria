package com.example.explomod.registry;

import com.example.explomod.advancement.IStatTrigger;

public class AtheriaRegistry {
    public static IAtheriaRegistry INSTANCE = null;

    public static IStatTrigger getStatTrigger(){
        return INSTANCE.getStatTrigger();
    }
}

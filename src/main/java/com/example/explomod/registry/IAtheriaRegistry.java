package com.example.explomod.registry;

import com.example.explomod.advancement.IStatTrigger;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface IAtheriaRegistry {

    IStatTrigger getStatTrigger();
}

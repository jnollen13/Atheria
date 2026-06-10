package com.example.explomod.client.renderer;

import com.example.explomod.datagen.AtheriaDiemsions;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

public class AtheriaDimensionEffects {
    public final static DimensionSpecialEffects AETHER_EFFECTS = new AtheriaDimensionSpecialEffects.DarkEffects();

    public static void registerRenderEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(AtheriaDiemsions.DARK_DIMENSION_TYPE.location(), AETHER_EFFECTS);
    }
}

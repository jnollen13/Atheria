package com.example.explomod.client.renderer;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AtheriaDimensionSpecialEffects extends DimensionSpecialEffects {
    public AtheriaDimensionSpecialEffects(float cloudLevel, boolean hasGround, SkyType skyType, boolean forceBrightLightmap, boolean constantAmbientLight) {
        super(cloudLevel, hasGround, skyType, forceBrightLightmap, constantAmbientLight);
    }

    /**
     * @param vec3 
     * @param v
     * @return
     */
    @Override
    public Vec3 getBrightnessDependentFogColor(@NotNull Vec3 vec3, float v) {
        return null;
    }

    /**
     * @param i 
     * @param i1
     * @return
     */
    @Override
    public boolean isFoggyAt(int i, int i1) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DarkEffects extends DimensionSpecialEffects {
        public DarkEffects() {
            super(Float.NaN, false, SkyType.END, false, false);
        }

        public @NotNull Vec3 getBrightnessDependentFogColor(@NotNull Vec3 p_108901_, float p_108902_) {
            return p_108901_;
        }

        public boolean isFoggyAt(int p_108898_, int p_108899_) {
            return true;
        }

        @Nullable
        public float[] getSunriseColor(float p_108888_, float p_108889_) {
            return null;
        }
    }
}

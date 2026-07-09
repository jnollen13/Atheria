package com.example.explomod.client.renderer;

import com.example.explomod.ExploMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class AtheriaDimensionSpecialEffects extends DimensionSpecialEffects {
    public AtheriaDimensionSpecialEffects() {
        super(250.0F, false, SkyType.NONE, false, false);
    }

    @Override
    public @org.jetbrains.annotations.Nullable float[] getSunriseColor(float timeOfDay, float partialTicks) {
        return null;
    }

    /**
     * @param vec3 
     * @param v
     * @return
     */
    @Override
    public @NotNull Vec3 getBrightnessDependentFogColor(@NotNull Vec3 vec3, float v) {
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

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        setupFog.run();

        RenderSystem.setShaderFogColor(0f, 0f, 0f);

        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DarkEffects extends DimensionSpecialEffects {
        public DarkEffects() {
            super(Float.NaN, false, SkyType.NONE, false, false);
        }

        public @NotNull Vec3 getBrightnessDependentFogColor(Vec3 p_108894_, float p_108895_) {
            return p_108894_.scale(0.15F);
        }

        public boolean isFoggyAt(int p_108891_, int p_108892_) {
            return false;
        }

        @Nullable
        public float[] getSunriseColor(float p_108888_, float p_108889_) {
            return null;
        }


    }
}

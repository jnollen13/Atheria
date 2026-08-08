package com.example.explomod.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TNTParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected TNTParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, int lifespan) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.8f;

        this.lifetime = lifespan;
        this.setSpriteFromAge(spriteSet);
        this.sprites=spriteSet;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new TNTParticle(clientLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.spriteSet, 35);
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, int lifespan) {
            return new TNTParticle(clientLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.spriteSet, lifespan);
        }
    }
}


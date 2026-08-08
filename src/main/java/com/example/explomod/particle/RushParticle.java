package com.example.explomod.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RushParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected RushParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, int lifespan) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.8f;

        this.lifetime = lifespan;
        this.setSpriteFromAge(spriteSet);
        this.sprites=spriteSet;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.scale(RandomSource.create().nextFloat()+RandomSource.create().nextFloat());
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.y=this.y+0.01;
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
            return new RushParticle(clientLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.spriteSet, RandomSource.create().nextInt(9, 41));
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, int lifespan) {
            return new RushParticle(clientLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.spriteSet, lifespan);
        }
    }
}

package com.example.explomod.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MaxMana(float maxMana) {
    public static final Codec<MaxMana> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("max_mana").forGetter(MaxMana::maxMana)
            ).apply(instance, MaxMana::new)
    );
}

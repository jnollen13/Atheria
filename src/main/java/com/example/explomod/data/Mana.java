package com.example.explomod.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record Mana(float mana) {
    public static final Codec<Mana> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("mana").forGetter(Mana::mana)
            ).apply(instance, Mana::new)
    );
    public boolean oom(Player player){
        Mana mana1 = player.getData(AtheriaDataAttachments.MANA.get());
        return !(mana1.mana() > 0);
    }
}

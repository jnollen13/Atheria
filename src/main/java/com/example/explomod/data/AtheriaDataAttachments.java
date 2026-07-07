package com.example.explomod.data;

import com.example.explomod.ExploMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collections;
import java.util.function.Supplier;

public class AtheriaDataAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ExploMod.MODID);

    public static final Supplier<AttachmentType<Mana>> MANA = ATTACHMENT_TYPES.register(
            "mana",
            () -> AttachmentType.builder(() -> new Mana(1.0f))
                    .serialize(Mana.CODEC)
                    .build()
    );

    public static final Supplier<AttachmentType<LearnedSpells>> KNOWN_SPELLS = ATTACHMENT_TYPES.register(
            "known_spells",
            () -> AttachmentType.builder(() -> new LearnedSpells(Collections.emptyList()))
                    .serialize(LearnedSpells.CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}

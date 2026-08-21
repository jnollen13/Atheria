package com.example.explomod;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class AtheriaKeys {
    public final static KeyMapping SHOW_MANA = new KeyMapping("key.explomod.show.mana.desc", GLFW.GLFW_KEY_G, "key.explomod.category");

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(SHOW_MANA);
    }
}

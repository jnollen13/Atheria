package com.example.explomod;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class AtheriaKeys {
    public final static KeyMapping CAST_FIREBALL = new KeyMapping("key.explomod.fireball.desc", GLFW.GLFW_KEY_I, "key.explomod.category");
    public final static KeyMapping DASH = new KeyMapping("key.explomod.dash.desc", GLFW.GLFW_KEY_COMMA, "key.explomod.category");
    public final static KeyMapping SHOW_MANA = new KeyMapping("key.explomod.show.mana.desc", GLFW.GLFW_KEY_G, "key.explomod.category");
    public final static KeyMapping AIR_BURST = new KeyMapping("key.explomod.air_burst.desc", GLFW.GLFW_KEY_I, "key.explomod.category");


    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CAST_FIREBALL);
        event.register(SHOW_MANA);
        event.register(DASH);
        event.register(AIR_BURST);
    }
}

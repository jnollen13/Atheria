package com.example.explomod;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.entity.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterEntitySpectatorShadersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ExploMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ExploMod.MODID, value = Dist.CLIENT)
public class ExploModClient {

    public ExploModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ExploMod.LOGGER.info("HELLO FROM CLIENT SETUP");
    }

    public static void clientInit(IEventBus bus) {
        bus.addListener(ExploModClient::registerSpectatorShaders);

        ExploModClient.eventSetup(bus);
    }

    public static void eventSetup(IEventBus neoBus) {
        neoBus.addListener(AtheriaKeys::registerKeyMappings);
    }

    public static void registerSpectatorShaders(RegisterEntitySpectatorShadersEvent event) {
        event.register(ModEntities.GRASS_GOLEM.get(), ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "shaders/post/grass_golem.json"));
    }


    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.PLAYER_HEALTH,
                ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "mana"),
                (guiGraphics, deltaTracker) -> {
                    assert Minecraft.getInstance().player != null;
                    Mana stats = Minecraft.getInstance().player.getData(AtheriaDataAttachments.MANA.get());
                    float currentValue = stats.mana();
                    float maxValue = 100.0f;
                    float percentage = Math.max(0.0f, Math.min(1.0f, currentValue / maxValue));
                    if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null && !Minecraft.getInstance().options.hideGui) {
                        boolean shouldRender = AtheriaKeys.SHOW_MANA.isDown();
                        if(shouldRender) {
                            int x = guiGraphics.guiWidth() / 2 - 91;
                            int y = guiGraphics.guiHeight() - 29;
                            guiGraphics.fill(x, y, x + 182, y + 5, 0xFF555555);
                            if(percentage>=0) {
                                int fillWidth = (int) (percentage * 182);
                                guiGraphics.fill(x, y, x + fillWidth, y + 5, 0xFF55FFFF);
                            }else{
                                int fillWidth = (int) (percentage * 182);
                                guiGraphics.fill(x, y, x + Math.abs(fillWidth), y + 5, 0xFFFF0000);
                            }
                        }
                    }
                }
        );
    }
}

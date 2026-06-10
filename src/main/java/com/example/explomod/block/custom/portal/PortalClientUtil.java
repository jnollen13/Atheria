package com.example.explomod.block.custom.portal;

import com.example.explomod.ExploMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Portal;

public class PortalClientUtil {
    public static void handleAetherPortal(Player player) {
        if (player instanceof LocalPlayer localPlayer) {
            if (!(Minecraft.getInstance().screen instanceof ReceivingLevelScreen)) {
                float f = 0.0F;
                if (localPlayer.portalProcess != null && localPlayer.portalProcess.isInsidePortalThisTick() && localPlayer.portalProcess.isSamePortal((Portal) ExploMod.DARK_PORTAL.get())) {
                    if (Minecraft.getInstance().screen != null
                            && !Minecraft.getInstance().screen.isPauseScreen()
                            && !(Minecraft.getInstance().screen instanceof DeathScreen)
                            && !(Minecraft.getInstance().screen instanceof WinScreen)) {
                        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen) {
                            localPlayer.closeContainer();
                        }

                        Minecraft.getInstance().setScreen(null);
                    }

                    f = 0.0125F;
                    localPlayer.portalProcess.setAsInsidePortalThisTick(false);
                }
            }
        }
    }

    public static void playTriggerSound() {
        Minecraft.getInstance().getSoundManager().play(PortalTriggerSoundInstance.forLocalAmbience(Minecraft.getInstance().player, SoundEvents.PORTAL_TRIGGER, Minecraft.getInstance().level.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F));
    }

    public static void playTravelSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRAVEL, Minecraft.getInstance().level.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F));
    }
}

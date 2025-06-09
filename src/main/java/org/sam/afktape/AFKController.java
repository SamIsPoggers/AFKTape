package org.sam.afktape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Set;

public class AFKController {
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean afkMode = false;
    private final Set<KeyBinding> heldKeys = new HashSet<>();
    private boolean mouseGrabbed = true;

    public boolean isAfkEnabled() {
        return afkMode;
    }

    public Set<KeyBinding> getHeldKeys() {
        return heldKeys;
    }

    public void toggleAFK() {
        afkMode = !afkMode;
        heldKeys.clear();

        if (afkMode) {
            for (KeyBinding key : mc.gameSettings.keyBindings) {
                if (key != null
                        && key != AFKTape.toggleAfkKey
                        && ( (key.getKeyCode() > 0 && Keyboard.isKeyDown(key.getKeyCode()))  // keyboard keys
                        || (key.getKeyCode() < 0 && org.lwjgl.input.Mouse.isButtonDown(key.getKeyCode() + 100)) )) { // mouse buttons
                    heldKeys.add(key);
                }
            }
            System.out.println("AFK Mode Enabled. Keys: " + heldKeys);
        } else {
            System.out.println("AFK Mode Disabled.");
        }
    }

    public void toggleMouseGrab() {
        mouseGrabbed = !mouseGrabbed;

        if (!mouseGrabbed) {
            mc.inGameHasFocus = false;
            mc.mouseHelper.ungrabMouseCursor();
            mc.gameSettings.pauseOnLostFocus = false; // 👈 This disables pause on unfocus
            System.out.println("Mouse ungrabbed. Pause on lost focus: disabled.");
        } else {
            mc.inGameHasFocus = true;
            mc.mouseHelper.grabMouseCursor();
            mc.gameSettings.pauseOnLostFocus = true; // 👈 Restore default behavior
            System.out.println("Mouse grabbed. Pause on lost focus: enabled.");
        }
    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (afkMode) {
            for (KeyBinding key : heldKeys) {
                KeyBinding.setKeyBindState(key.getKeyCode(), true);
            }
        }
    }

    public void stopAFK() {
        if (afkMode) {
            afkMode = false;
            for (KeyBinding key : heldKeys) {
                KeyBinding.setKeyBindState(key.getKeyCode(), false);
            }
            heldKeys.clear();
            System.out.println("AFK Mode Disabled.");
        }
    }
}

package org.sam.afktape;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import java.util.HashSet;
import java.util.Set;

public class KeybindHandler {

    public static final KeybindHandler INSTANCE = new KeybindHandler();

    private boolean running = false;
    private boolean paused = false;
    public boolean wasPaused = false;

    public final Set<KeyMapping> enabledKeys = new HashSet<>();

    public boolean isRunning() {
        return running && !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isRunningIgnorePause() {
        return running;
    }

    public void enable(Set<KeyMapping> keys) {
        running = true;
        enabledKeys.addAll(keys);
        Minecraft.getInstance().mouseHandler.releaseMouse();
    }

    public String[] getMessage() {
        if (Minecraft.getInstance().player != null) {
            String[] msg = new String[2];
            StringBuilder str = new StringBuilder();
            str.append(ChatFormatting.WHITE).append("Taped down ");

            KeyMapping[] keyArray = enabledKeys.toArray(new KeyMapping[0]);
            for (int i = 0; i < keyArray.length; i++) {
                String keyName = keyArray[i].getTranslatedKeyMessage().getString().toUpperCase();
                int size = keyArray.length;

                if (size == 1 || i == size - 2) {
                    str.append(ChatFormatting.AQUA).append(keyName);
                } else if (i == size - 1) {
                    str.append(ChatFormatting.WHITE).append(" and ").append(ChatFormatting.AQUA).append(keyName);
                } else {
                    str.append(ChatFormatting.AQUA).append(keyName).append(ChatFormatting.WHITE).append(", ");
                }
            }

            msg[0] = str.toString();
            msg[1] = ChatFormatting.WHITE + "Press " + ChatFormatting.RED + "ESCAPE" + ChatFormatting.WHITE + " to exit";
            return msg;
        }
        return new String[0];
    }

    public void disable() {
        enabledKeys.forEach(key -> key.setDown(false));
        enabledKeys.clear();
        running = false;
        unpause();
        wasPaused = false;

        if (Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().mouseHandler.grabMouse();
        }
    }

    public void pause() {
        paused = true;
        wasPaused = true;
    }

    public void unpause() {
        paused = false;
    }
}

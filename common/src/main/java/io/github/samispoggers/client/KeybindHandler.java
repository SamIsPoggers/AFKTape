package io.github.samispoggers.client;

import io.github.samispoggers.config.AFKTapeConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import java.util.HashSet;
import java.util.Set;
import static net.minecraft.ChatFormatting.*;

public class KeybindHandler {
    public static final KeybindHandler INSTANCE = new KeybindHandler();
    private final SoundManager soundManager = new SoundManager();
    private boolean running = false;
    private boolean paused = false;
    public boolean wasPaused = false;
    public Set<KeyMapping> enabledKeys = new HashSet<>();

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
        if (AFKTapeConfig.get().muteWhenAfk) {
            soundManager.mute();
        }
        Minecraft.getInstance().mouseHandler.releaseMouse();
    }

    public String[] getMessage() {
        String[] msg = new String[4];
        if (Minecraft.getInstance().player != null) {
            StringBuilder str = new StringBuilder();
            str.append(WHITE).append("Taped down ");
            for (int i = 0; i < enabledKeys.size(); i++) {
                KeyMapping keyMapping = enabledKeys.toArray(new KeyMapping[0])[i];
                String keyName = keyMapping.getTranslatedKeyMessage().getString().toUpperCase();
                int size = enabledKeys.size();
                if (size == 1 || i == size - 2) {
                    str.append(AQUA).append(keyName);
                } else if (i == size - 1) {
                    str.append(WHITE).append(" and ").append(AQUA).append(keyName);
                } else {
                    str.append(AQUA).append(keyName).append(WHITE).append(", ");
                }
            }
            msg[0] = str.toString();
            if (AFKTapeConfig.get().muteWhenAfk) {
                msg[1] = (WHITE + "Volume (" + soundManager.getOldVolume() +"%) is " + GRAY + "MUTED" + WHITE);
            } else {
                msg[1] = (WHITE + "Volume is " + GREEN + "NOT MUTED" + WHITE);
            }
            msg[2] = "";
            msg[3] = (WHITE + "Press " + RED + "ESCAPE" + WHITE + " to exit");
            return msg;
        }
        return new String[0];
    }

    public void disable() {
        enabledKeys.forEach(key -> key.setDown(false));
        enabledKeys.clear();
        if (AFKTapeConfig.get().muteWhenAfk) {
            soundManager.unmute();
        }
        running = false;
        unpause();
        wasPaused = false;
        if (Minecraft.getInstance().screen == null) Minecraft.getInstance().mouseHandler.grabMouse();
    }

    public void pause() {
        paused = true;
        wasPaused = true;
    }

    public void unpause() {
        paused = false;
    }
}
package io.github.samispoggers.client;

import io.github.samispoggers.config.AFKTapeConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.minecraft.ChatFormatting.*;

public class KeybindHandler {
    public static final KeybindHandler INSTANCE = new KeybindHandler();

    private final SoundManager soundManager = new SoundManager();

    private boolean running = false;
    private boolean paused = false;

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
        enabledKeys.clear();
        enabledKeys.addAll(keys);

        if (AFKTapeConfig.get().muteWhenAfk) {
            soundManager.mute();
        }

        Minecraft.getInstance().mouseHandler.releaseMouse();
    }

    public String[] getMessage() {
        if (Minecraft.getInstance().player == null) {
            return new String[0];
        }

        String[] msg = new String[4];

        List<String> keyNames = enabledKeys.stream()
                .map(key -> key.getTranslatedKeyMessage().getString().toUpperCase())
                .sorted()
                .toList();

        StringBuilder builder = new StringBuilder();
        builder.append(WHITE).append("Taped down ");

        for (int i = 0; i < keyNames.size(); i++) {
            if (i > 0) {
                if (i == keyNames.size() - 1) {
                    builder.append(WHITE).append(" and ");
                } else {
                    builder.append(WHITE).append(", ");
                }
            }

            builder.append(AQUA).append(keyNames.get(i));
        }

        msg[0] = builder.toString();

        if (AFKTapeConfig.get().muteWhenAfk) {
            msg[1] = WHITE + "Volume (" + soundManager.getOldVolume() + "%) is " + GRAY + "MUTED" + WHITE;
        } else {
            msg[1] = WHITE + "Volume is " + GREEN + "NOT MUTED" + WHITE;
        }

        msg[2] = "";
        msg[3] = WHITE + "Press " + RED + "ESCAPE" + WHITE + " to exit";

        return msg;
    }

    public void disable() {
        enabledKeys.forEach(key -> key.setDown(false));
        enabledKeys.clear();

        if (AFKTapeConfig.get().muteWhenAfk) {
            soundManager.unmute();
        }

        running = false;
        paused = false;

        if (Minecraft.getInstance().gui.screen() == null) {
            Minecraft.getInstance().mouseHandler.grabMouse();
        }
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }
}
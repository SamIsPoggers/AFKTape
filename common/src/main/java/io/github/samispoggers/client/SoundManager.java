package io.github.samispoggers.client;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;

public class SoundManager {
    private final Minecraft mc = Minecraft.getInstance();
    private boolean muted = false;
    private Double oldVolume = -1.0;

    public void mute() {
        if (muted) {
            return;
        }
        oldVolume = (double) mc.options.getSoundSourceVolume(SoundSource.MASTER);
        mc.options.getSoundSourceOptionInstance(SoundSource.MASTER).set(0.0);
        muted = true;
    }

    public void unmute() {
        if (!muted) {
            return;
        }
        mc.options.getSoundSourceOptionInstance(SoundSource.MASTER).set(oldVolume);
        muted = false;
    }

    public int getOldVolume() {
        return (int) (oldVolume * 100);
    }
}
package io.github.samispoggers.afktape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundCategory;

public final class SoundManager {
	private boolean muted = false;
	private float oldVolume = -1.0F;

	public void mute() {
		if (muted) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		oldVolume = mc.options.getSoundCategoryVolume(SoundCategory.MASTER);
		mc.options.setSoundCategoryVolume(SoundCategory.MASTER, 0.0F);
		muted = true;
	}

	public void unmute() {
		if (!muted) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		mc.options.setSoundCategoryVolume(SoundCategory.MASTER, oldVolume);
		muted = false;
	}

	public boolean isMuted() {
		return muted;
	}

	public int getOldVolume() {
		return Math.round(oldVolume * 100.0F);
	}
}

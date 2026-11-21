package io.github.samispoggers.afktape;

import io.github.samispoggers.afktape.config.AFKTapeConfig;
import io.github.samispoggers.afktape.mixin.KeyBindingAccessor;
import io.github.samispoggers.afktape.mixin.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AFKController {
	private final SoundManager soundManager = new SoundManager();
	private boolean afkMode;

	private final Set<KeyBinding> heldKeys = new HashSet<>();

	public boolean isAfkEnabled() {
		return afkMode;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public Set<KeyBinding> getHeldKeys() {
		return Collections.unmodifiableSet(heldKeys);
	}

	public List<String> getHeldKeyNames() {
		List<String> names = new ArrayList<>();
		for (KeyBinding binding : heldKeys) {
			names.add(getKeyName(binding.getKeyCode()));
		}
		Collections.sort(names);
		return names;
	}

	public static String getKeyName(int code) {
		if (code < 0) {
			switch (code + 100) {
				case 0: return "Left Click";
				case 1: return "Right Click";
				case 2: return "Middle Click";
				default: return "Mouse " + (code + 101);
			}
		}
		String name = Keyboard.getKeyName(code);
		return name == null ? "?" : name;
	}

	private List<KeyBinding> getAllBindings() {
		return KeyBindingAccessor.afktape$getAll();
	}

	public void toggleAFK() {
		if (afkMode) {
			stopAFK();
			return;
		}

		heldKeys.clear();

		for (KeyBinding binding : getAllBindings()) {
			int code = binding.getKeyCode();

			if (code > 0 && code < Keyboard.KEYBOARD_SIZE
				&& Keyboard.isKeyDown(code)) {
				heldKeys.add(binding);
			} else if (code < 0 && Mouse.isButtonDown(code + 100)) {
				heldKeys.add(binding);
			}
		}

		heldKeys.remove(AFKTape.toggleAfkKey);
		heldKeys.remove(AFKTape.unlockMouseKey);

		afkMode = true;
		if (AFKTapeConfig.isMuteWhenAfk()) {
			soundManager.mute();
		}
		if (shouldUnlockMouse()) {
			releaseMouse();
		}
	}

	public static boolean shouldUnlockMouse() {
		boolean keyDown = AFKTape.unlockMouseKey != null && isBindingDown(AFKTape.unlockMouseKey);
		return AFKTapeConfig.isUnlockMouseByDefault() ? !keyDown : keyDown;
	}

	public void tickAFK() {
		if (!afkMode) {
			return;
		}

		for (KeyBinding binding : heldKeys) {
			KeyBinding.set(binding.getKeyCode(), true);
		}

		if (Minecraft.getInstance().screen == null) {
			boolean wantUnlock = shouldUnlockMouse();
			if (wantUnlock && Mouse.isGrabbed()) {
				releaseMouse();
			} else if (!wantUnlock && !Mouse.isGrabbed()) {
				grabMouse();
			}
		}
	}

	private static boolean isBindingDown(KeyBinding binding) {
		int code = binding.getKeyCode();
		if (code < 0) {
			return Mouse.isButtonDown(code + 100);
		}
		return code > 0 && Keyboard.isKeyDown(code);
	}

	public void stopAFK() {
		if (!afkMode) {
			return;
		}

		afkMode = false;

		if (AFKTapeConfig.isMuteWhenAfk()) {
			soundManager.unmute();
		}

		boolean inGame = Minecraft.getInstance().screen == null;
		for (KeyBinding binding : heldKeys) {
			KeyBinding.set(binding.getKeyCode(), inGame && isBindingDown(binding));
		}

		heldKeys.clear();
		grabMouse();
	}

	private void releaseMouse() {
		Minecraft mc = Minecraft.getInstance();
		((MinecraftAccessor) mc).afktape$setFocused(false);
		mc.mouse.unlock();
		Mouse.setGrabbed(false);
	}

	private void grabMouse() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen != null) {
			return;
		}
		((MinecraftAccessor) mc).afktape$setFocused(true);
		mc.mouse.lock();
	}

	public boolean isKeyTaped(int keyCode) {
		if (!afkMode) {
			return false;
		}
		for (KeyBinding binding : heldKeys) {
			if (binding.getKeyCode() == keyCode) {
				return true;
			}
		}
		return false;
	}
}

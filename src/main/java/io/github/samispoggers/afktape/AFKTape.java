
package io.github.samispoggers.afktape;

import net.minecraft.client.options.KeyBinding;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.keybinds.api.KeybindEvents;
import net.ornithemc.osl.keybinds.api.KeybindRegistry;
import org.lwjgl.input.Keyboard;

public final class AFKTape implements ClientModInitializer {
	public static KeyBinding toggleAfkKey;
	public static KeyBinding unlockMouseKey;

	public static AFKController afkController;

	@Override
	public void initClient() {
		KeybindEvents.REGISTER_KEYBINDS.register(AFKTape::registerKeybinds);

		afkController = new AFKController();
	}

	private static void registerKeybinds() {
		toggleAfkKey = KeybindRegistry.register(
			"key.afktape.toggle",
			Keyboard.KEY_K,
			"key.categories.afktape"
		);

		unlockMouseKey = KeybindRegistry.register(
			"key.afktape.mouse",
			Keyboard.KEY_LMENU,
			"key.categories.afktape"
		);
	}
}

package io.github.samispoggers.client;

import io.github.samispoggers.AFKTape;
import io.github.samispoggers.config.AFKTapeConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class AFKTapeClient {
    public static KeyMapping toggleAfkKey;
    public static KeyMapping unlockMouseKey;

    private AFKTapeClient() {
    }

    public static void init() {
        AFKTapeConfig.load();
    }

    public static KeyMapping createToggleAfkKey() {
        KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath(AFKTape.MOD_ID, "afktape"));

        return new KeyMapping(
                "key.afktape.toggle",
                GLFW.GLFW_KEY_K,
                category
        );
    }

    public static KeyMapping createUnlockMouseKey() {
        KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath(AFKTape.MOD_ID, "afktape"));

        return new KeyMapping(
                "key.afktape.mouse",
                GLFW.GLFW_KEY_LEFT_ALT,
                category
        );
    }
}

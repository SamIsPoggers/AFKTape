package org.sam.afktape;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    public static KeyMapping toggleAfkKey;
    public static KeyMapping unlockMouseKey;

    public static void init() {
        toggleAfkKey = new KeyMapping("key.afktape.toggle",
                GLFW.GLFW_KEY_K,
                "category.afktape"
        );

        unlockMouseKey = new KeyMapping("key.afktape.mouse",
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.afktape"
        );
    }
}
package io.github.samispoggers.client;

import io.github.samispoggers.AFKTape;
import io.github.samispoggers.config.AFKTapeConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class AFKTapeClient implements ClientModInitializer {
    public static KeyMapping toggleAfkKey;
    public static KeyMapping unlockMouseKey;

    @Override
    public void onInitializeClient() {
        AFKTapeConfig.load();

        KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath(AFKTape.MOD_ID, "afktape"));

        toggleAfkKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.afktape.toggle",
                GLFW.GLFW_KEY_K,
                category
        ));

        unlockMouseKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.afktape.mouse",
                GLFW.GLFW_KEY_LEFT_ALT,
                category
        ));
    }
}

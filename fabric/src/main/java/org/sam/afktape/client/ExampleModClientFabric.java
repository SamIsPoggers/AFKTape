package org.sam.afktape.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.sam.afktape.KeyBinds;

public class ExampleModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBinds.init();
        KeyBindingHelper.registerKeyBinding(KeyBinds.toggleAfkKey);
        KeyBindingHelper.registerKeyBinding(KeyBinds.unlockMouseKey);
    }
}

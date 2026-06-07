package io.github.samispoggers.fabric;

import io.github.samispoggers.client.AFKTapeClient;
import io.github.samispoggers.platform.PlatformHooks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;

public class AFKTapeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlatformHooks.setConfigDir(FabricLoader.getInstance().getConfigDir());
        AFKTapeClient.init();
        AFKTapeClient.toggleAfkKey = KeyMappingHelper.registerKeyMapping(AFKTapeClient.createToggleAfkKey());
        AFKTapeClient.unlockMouseKey = KeyMappingHelper.registerKeyMapping(AFKTapeClient.createUnlockMouseKey());
    }
}

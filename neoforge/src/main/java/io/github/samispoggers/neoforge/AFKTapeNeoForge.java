package io.github.samispoggers.neoforge;

import io.github.samispoggers.AFKTape;
import io.github.samispoggers.client.AFKTapeClient;
import io.github.samispoggers.config.AFKTapeConfig;
import io.github.samispoggers.platform.PlatformHooks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = AFKTape.MOD_ID, dist = Dist.CLIENT)
public class AFKTapeNeoForge {
    public AFKTapeNeoForge(IEventBus modEventBus, ModContainer container) {
        PlatformHooks.setConfigDir(FMLPaths.CONFIGDIR.get());
        AFKTape.init();
        AFKTapeClient.init();
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc, parent) -> AFKTapeConfig.createScreen(parent)
        );
        modEventBus.addListener(this::registerKeyMappings);
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
        AFKTapeClient.toggleAfkKey = AFKTapeClient.createToggleAfkKey();
        AFKTapeClient.unlockMouseKey = AFKTapeClient.createUnlockMouseKey();
        event.register(AFKTapeClient.toggleAfkKey);
        event.register(AFKTapeClient.unlockMouseKey);
    }
}

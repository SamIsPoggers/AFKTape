package org.sam.afktape;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(ExampleMod.MOD_ID)
public class ExampleModNeoForge {
    public ExampleModNeoForge() {
        ExampleMod.init();

        KeyBinds.init();
    }

    @EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class clientInit {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.toggleAfkKey);
            event.register(KeyBinds.unlockMouseKey);
        }
    }
}

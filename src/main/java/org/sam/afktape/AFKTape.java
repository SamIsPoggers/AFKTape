package org.sam.afktape;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod(
        modid = AFKTape.MODID,
        name = AFKTape.MODNAME,
        version = AFKTape.VERSION)
public class AFKTape {
    public static final String MODID = "afktape";
    public static final String MODNAME = "AFK Tape";
    public static final String VERSION = "1.0";

    public static KeyBinding toggleAfkKey;
    public static KeyBinding unlockMouseKey;

    private static AFKController afkController; // <-- Moved here

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        toggleAfkKey = new KeyBinding("key.afktape.toggle", Keyboard.KEY_K, "key.categories.afktape");
        unlockMouseKey = new KeyBinding("key.afktape.mouse", Keyboard.KEY_LMENU, "key.categories.afktape");

        FMLClientHandler.instance().getClient().gameSettings.keyBindings =
                appendKeyBinding(FMLClientHandler.instance().getClient().gameSettings.keyBindings, toggleAfkKey, unlockMouseKey);

        afkController = new AFKController();
        MinecraftForge.EVENT_BUS.register(this); // This must come after key setup
        MinecraftForge.EVENT_BUS.register(afkController);
        MinecraftForge.EVENT_BUS.register(new DisplayOverlay(afkController));
    }

    private KeyBinding[] appendKeyBinding(KeyBinding[] original, KeyBinding... newBindings) {
        KeyBinding[] result = new KeyBinding[original.length + newBindings.length];
        System.arraycopy(original, 0, result, 0, original.length);
        System.arraycopy(newBindings, 0, result, original.length, newBindings.length);
        return result;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (toggleAfkKey.isPressed()) {
            if (afkController.isAfkEnabled()) {
                afkController.stopAFK();
            } else {
                afkController.toggleAFK();
            }
        }

        if (unlockMouseKey.isPressed()) {
            afkController.toggleMouseGrab(); // Optional: we'll implement next
        }
    }
}

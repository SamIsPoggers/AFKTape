package io.github.samispoggers.mixin;

import io.github.samispoggers.client.AFKTapeClient;
import io.github.samispoggers.client.KeybindHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Shadow
    public LocalPlayer player;

    @Shadow @Final
    public Options options;

    @Shadow @Final
    public MouseHandler mouseHandler;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void modifyTick(CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = mc.gui.screen(); // or mc.gui.getScreen() depending on mappings
        if (KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            if (player == null || !player.isAlive()) {
                KeybindHandler.INSTANCE.disable();
            }
            if (screen == null) {
                if (AFKTapeClient.unlockMouseKey.isDown()) {
                    mouseHandler.grabMouse();
                } else {
                    mouseHandler.releaseMouse();
                }
            }
        }

        if (KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            if (screen == null) {
                if (KeybindHandler.INSTANCE.isPaused()) {
                    KeybindHandler.INSTANCE.unpause();
                }
            } else {
                if (screen instanceof PauseScreen) {
                    options.pauseOnLostFocus = false;
                    Minecraft.getInstance().gui.setScreen(null);
                    Minecraft.getInstance().mouseHandler.grabMouse();
                    System.out.println("Trigger!!!!");
                }
                if (!KeybindHandler.INSTANCE.isPaused()) {
                    KeybindHandler.INSTANCE.pause();
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "pauseGame(Z)V", cancellable = true)
    private void modifyOpenPauseMenu(CallbackInfo info) {
        if (KeybindHandler.INSTANCE.isRunning()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "handleKeybinds()V")
    private void modifyHandleInputEvents(CallbackInfo info) {
        if (AFKTapeClient.toggleAfkKey.consumeClick()) {
            Set<String> seen = new HashSet<>();
            Set<KeyMapping> pressedKeybinds = new HashSet<>();

            for (KeyMapping keyMapping : options.keyMappings) {
                if (!keyMapping.isDown() || keyMapping == AFKTapeClient.toggleAfkKey) {
                    continue;
                }

                String keyName = keyMapping.getTranslatedKeyMessage().getString();

                if (seen.add(keyName)) {
                    pressedKeybinds.add(keyMapping);
                }
            }

            if (!pressedKeybinds.isEmpty()) {
                KeybindHandler.INSTANCE.enable(pressedKeybinds);
            }
        }

        if (KeybindHandler.INSTANCE.isRunning()) {
            for (KeyMapping key : KeybindHandler.INSTANCE.enabledKeys) {
                if (!key.isDown()) {
                    key.setDown(true);
                }
            }
        }
    }
}
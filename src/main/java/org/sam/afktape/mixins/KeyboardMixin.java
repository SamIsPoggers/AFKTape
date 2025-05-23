package org.sam.afktape.mixins;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.sam.afktape.client.KeybindHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    final int KEY_ESCAPE = 256;
    long lastEscapePressTime = 0;

    @Inject(at = @At("HEAD"), method = "onKey(JIIII)V", cancellable = true)
    private void modifyOnKey(long window, int key, int scancode, int i, int j, CallbackInfo info) {
        boolean keyState = i == 1;

        if (KeybindHandler.INSTANCE.isRunning()) {
            for (KeyBinding keyBinding : KeybindHandler.INSTANCE.enabledKeys) {
                if (keyBinding.matchesKey(key, scancode)) info.cancel();
            }
        }

        if (key == KEY_ESCAPE && keyState && KeybindHandler.INSTANCE.isRunning() && MinecraftClient.getInstance().currentScreen == null) {
            KeybindHandler.INSTANCE.disable();
            info.cancel();
        }

        if (key == KEY_ESCAPE && keyState && KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            if (MinecraftClient.getInstance().currentScreen == null) {
                KeybindHandler.INSTANCE.disable();
                info.cancel();
            } else if (System.currentTimeMillis() - lastEscapePressTime <= 300) {
                KeybindHandler.INSTANCE.disable();
            }
            lastEscapePressTime = System.currentTimeMillis();
        }
    }
}
package org.sam.afktape.mixins;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import org.sam.afktape.KeybindHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {

    final int KEY_ESCAPE = 256;
    long lastEscapePressTime = 0;

    @Inject(at = @At("HEAD"), method = "keyPress(JIIII)V", cancellable = true)
    private void modifyOnKey(long window, int key, int scancode, int i, int j, CallbackInfo info) {
        boolean keyState = i == 1;

        if (KeybindHandler.INSTANCE.isRunning()) {
            for (KeyMapping keyMapping : KeybindHandler.INSTANCE.enabledKeys) {
                if (keyMapping.matches(key, scancode)) info.cancel();
            }
        }

        if (key == KEY_ESCAPE && keyState && KeybindHandler.INSTANCE.isRunning() && Minecraft.getInstance().screen == null) {
            KeybindHandler.INSTANCE.disable();
            info.cancel();
        }

        if (key == KEY_ESCAPE && keyState && KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            if (Minecraft.getInstance().screen == null) {
                KeybindHandler.INSTANCE.disable();
                info.cancel();
            } else if (System.currentTimeMillis() - lastEscapePressTime <= 300) {
                KeybindHandler.INSTANCE.disable();
            }
            lastEscapePressTime = System.currentTimeMillis();
        }
    }
}
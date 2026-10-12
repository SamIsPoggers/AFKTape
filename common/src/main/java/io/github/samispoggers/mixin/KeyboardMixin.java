package io.github.samispoggers.mixin;

import io.github.samispoggers.client.KeybindHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    @Unique
    private static final int KEY_ESCAPE = 256;
    @Unique
    private static final int ACTION_PRESS = 1;

    @Inject(at = @At("HEAD"), method = "keyPress", cancellable = true)
    private void modifyKeyPress(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (action != ACTION_PRESS) {
            return;
        }

        int key = event.key();

//        if (key == KEY_ESCAPE && KeybindHandler.INSTANCE.isRunningIgnorePause()) {
//            KeybindHandler.INSTANCE.disable();
//            ci.cancel();
//            return;
//        }

        if (KeybindHandler.INSTANCE.isRunning()) {
            for (KeyMapping keyMapping : KeybindHandler.INSTANCE.enabledKeys) {
                if (keyMapping.matches(event)) {
                    ci.cancel();
                    return;
                }
            }
        }

    }
}
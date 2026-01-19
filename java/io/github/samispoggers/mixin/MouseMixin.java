package io.github.samispoggers.mixin;

import io.github.samispoggers.client.AFKTapeClient;
import io.github.samispoggers.client.KeybindHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(at = @At("HEAD"), method = "isMouseGrabbed()Z", cancellable = true)
    private void modifyIsCursorLocked(CallbackInfoReturnable<Boolean> info) {
        if (Minecraft.getInstance().screen == null && KeybindHandler.INSTANCE.isRunning()) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "grabMouse()V", cancellable = true)
    private void modifyLockCursor(CallbackInfo info) {
        if (KeybindHandler.INSTANCE.isRunning() && !AFKTapeClient.unlockMouseKey.isDown()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onMove(JDD)V", cancellable = true)
    private void modifyOnCursorPos(CallbackInfo info) {
        if (KeybindHandler.INSTANCE.isRunning() && !AFKTapeClient.unlockMouseKey.isDown()) {
            info.cancel();
        }
    }
}
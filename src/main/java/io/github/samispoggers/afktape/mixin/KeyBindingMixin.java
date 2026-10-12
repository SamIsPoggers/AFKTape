package io.github.samispoggers.afktape.mixin;

import io.github.samispoggers.afktape.AFKTape;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {

	@Inject(method = "set(IZ)V", at = @At("HEAD"), cancellable = true)
	private static void afktape$preventKeyRelease(int keyCode, boolean pressed, CallbackInfo ci) {
		if (!pressed && AFKTape.afkController != null && AFKTape.afkController.isKeyTaped(keyCode)) {
			ci.cancel();
		}
	}
}

package io.github.samispoggers.afktape.mixin;

import io.github.samispoggers.afktape.AFKTape;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.lwjgl.input.Keyboard;

@Mixin(Minecraft.class)
public abstract class MinecraftTickMixin {

	@Inject(method = "tick()V", at = @At("TAIL"))
	private void afktape$handleKeys(CallbackInfo ci) {
		if (AFKTape.afkController == null) {
			return;
		}

		if (AFKTape.toggleAfkKey != null
			&& AFKTape.toggleAfkKey.consumeClick()) {
			AFKTape.afkController.toggleAFK();
		}

		if (AFKTape.afkController.isAfkEnabled()
			&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Minecraft mc = (Minecraft) (Object) this;

			if (mc.screen != null) {
				mc.openScreen(null);
			}

			AFKTape.afkController.stopAFK();
		}

		AFKTape.afkController.tickAFK();
	}

	@Redirect(
		method = "tick()V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;focused:Z",
			opcode = Opcodes.GETFIELD
		)
	)
	private boolean afktape$focusedDuringAfk(Minecraft mc) {
		return ((MinecraftAccessor) mc).afktape$getFocused()
			|| (AFKTape.afkController != null && AFKTape.afkController.isAfkEnabled());
	}
}

package io.github.samispoggers.afktape.mixin;

import io.github.samispoggers.afktape.AFKTape;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Redirect(
		method = "render(FJ)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/options/GameOptions;pauseOnUnfocus:Z",
			opcode = Opcodes.GETFIELD
		)
	)
	private boolean afktape$preventPauseOnUnfocus(GameOptions options) {
		if (AFKTape.afkController != null && AFKTape.afkController.isAfkEnabled()) {
			return false;
		}
		return options.pauseOnUnfocus;
	}
}

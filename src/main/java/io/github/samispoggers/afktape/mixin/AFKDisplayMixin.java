
package io.github.samispoggers.afktape.mixin;

import io.github.samispoggers.afktape.AFKController;
import io.github.samispoggers.afktape.AFKTape;
import io.github.samispoggers.afktape.config.AFKTapeConfig;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.Window;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameGui.class)
public abstract class AFKDisplayMixin {
	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "render(F)V", at = @At("TAIL"))
	private void afktape$renderOverlay(float tickDelta, CallbackInfo ci) {
		if (AFKTape.afkController == null
			|| !AFKTape.afkController.isAfkEnabled()) {
			return;
		}

		TextRenderer text = ((GameGui) (Object) this).getTextRenderer();
		Window window = new Window(minecraft);

		int centerX = window.getWidth() / 2;
		int centerY = window.getHeight() / 2;

		java.util.List<String> names = AFKTape.afkController.getHeldKeyNames();
		String keys = names.isEmpty() ? "none" : String.join(", ", names);

		String toggleName = AFKTape.toggleAfkKey != null
			? AFKController.getKeyName(AFKTape.toggleAfkKey.getKeyCode())
			: "K";

		String line1 = "§fTaped down §b" + keys;
		String line2 = AFKTapeConfig.isMuteWhenAfk()
			? "§fVolume (" + AFKTape.afkController.getSoundManager().getOldVolume() + "%) is §7MUTED"
			: "§fVolume is §aNOT MUTED";
		String line3 = "§fPress §cESCAPE §for §c" + toggleName + " §fto exit.";

		int lineHeight = text.fontHeight + 4;
		int y = centerY - 22;

		text.drawWithShadow(
			line1,
			centerX - text.getWidth(line1) / 2.0F,
			(float) y,
			0xFFFFFF
		);

		text.drawWithShadow(
			line2,
			centerX - text.getWidth(line2) / 2.0F,
			(float) (y + lineHeight),
			0xFFFFFF
		);

		text.drawWithShadow(
			line3,
			centerX - text.getWidth(line3) / 2.0F,
			(float) (y + lineHeight * 2 + 4),
			0xFFFFFF
		);
	}
}

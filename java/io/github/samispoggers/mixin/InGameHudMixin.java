package io.github.samispoggers.mixin;

import io.github.samispoggers.client.KeybindHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class InGameHudMixin {

    @Shadow
    public abstract Font getFont();

    @Inject(at = @At("TAIL"), method = "render")
    private void modifyRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (KeybindHandler.INSTANCE.isRunning()) {
            Font font = getFont();
            String[] lines = KeybindHandler.INSTANCE.getMessage();
            int scaledWidth = guiGraphics.guiWidth();
            int scaledHeight = guiGraphics.guiHeight();
            int additive = 0;

            for (int i = lines.length - 1; i >= 0; i--) {
                int width = font.width(lines[i]);
                guiGraphics.drawString(
                        font,
                        lines[i],
                        (scaledWidth / 2) - (width / 2),
                        ((scaledHeight / 2) - 20) + additive,
                        0xFFFFFFFF, // ARGB format - now visible!
                        true
                );
                additive -= 10;
            }
        }
    }
}
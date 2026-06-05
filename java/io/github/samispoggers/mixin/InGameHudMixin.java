package io.github.samispoggers.mixin;

import io.github.samispoggers.client.KeybindHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Inject(at = @At("TAIL"), method = "extractRenderState")
    private void modifyRender(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (KeybindHandler.INSTANCE.isRunning()) {
            String[] lines = KeybindHandler.INSTANCE.getMessage();
            int scaledWidth = guiGraphics.guiWidth();
            int scaledHeight = guiGraphics.guiHeight();
            int additive = 0;

            for (int i = lines.length - 1; i >= 0; i--) {
                guiGraphics.textRenderer().accept(
                        TextAlignment.CENTER,
                        scaledWidth / 2,
                        ((scaledHeight / 2) - 20) + additive,
                        Component.literal(lines[i])
                );
                additive -= 10;
            }
        }
    }
}

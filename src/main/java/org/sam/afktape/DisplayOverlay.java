package org.sam.afktape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayOverlay {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final AFKController afkController;

    public DisplayOverlay(AFKController controller) {
        this.afkController = controller;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!afkController.isAfkEnabled()) return;

        FontRenderer fr = mc.fontRendererObj;
        int screenWidth = event.resolution.getScaledWidth();
        int screenHeight = event.resolution.getScaledHeight();

        // Compose list of keys (keyboard + mouse keys combined) - keys themselves light blue
        List<String> allKeys = new ArrayList<>();

        for (KeyBinding key : afkController.getHeldKeys()) {
            // Get key name for mouse buttons and keyboard keys
            String keyName = getDisplayNameForKey(key);
            allKeys.add("§b" + keyName);  // Light blue for keys themselves
        }

        // Format keys list with commas and 'and' (connectors in white)
        String keysFormatted = formatKeysList(allKeys);

        // Line 1: "Taped down " (white) + keys (light blue)
        String line1 = "§fTaped down §f" + keysFormatted;

        // Line 2: "Press " (white) + toggle key (light red) + " to exit" (white)
        String toggleKeyName = Keyboard.getKeyName(AFKTape.toggleAfkKey.getKeyCode());
        String line2 = "§fPress §c" + toggleKeyName + " §fto exit";

        // Draw centered lines
        int line1Width = fr.getStringWidth(stripColorCodes(line1));
        int line2Width = fr.getStringWidth(stripColorCodes(line2));

        int centerX = screenWidth / 2;
        int baseY = screenHeight / 2 - 20;

        fr.drawStringWithShadow(line1, centerX - line1Width / 2, baseY, Color.WHITE.getRGB());
        fr.drawStringWithShadow(line2, centerX - line2Width / 2, baseY + 12, Color.WHITE.getRGB());
    }

    // Format list with commas and 'and', connectors white, keys already colored (light blue)
    private String formatKeysList(List<String> keys) {
        if (keys.isEmpty()) return "";

        if (keys.size() == 1) {
            return keys.get(0);
        }

        if (keys.size() == 2) {
            return keys.get(0) + " §fand " + keys.get(1);
        }

        // For 3+ keys: "A, B, and C"
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (i > 0) {
                if (i == keys.size() - 1) {
                    sb.append(" §fand ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(key);
        }
        return sb.toString();
    }

    // Get display name for keyboard or mouse button keybinding
    private String getDisplayNameForKey(KeyBinding key) {
        int code = key.getKeyCode();
        if (code < 0) {
            // Mouse button keys are negative codes starting at -100 for left click
            int mouseButtonIndex = code + 100;
            return mouseButtonName(mouseButtonIndex);
        } else {
            return Keyboard.getKeyName(code);
        }
    }

    // Mouse button names without color codes (we add color in the caller)
    private String mouseButtonName(int button) {
        switch (button) {
            case 0: return "Left";
            case 1: return "Right";
            case 2: return "Middle";
            default: return "Button" + button;
        }
    }

    // Remove color codes for width calculation (simple method)
    private String stripColorCodes(String input) {
        return input.replaceAll("§.", "");
    }
}

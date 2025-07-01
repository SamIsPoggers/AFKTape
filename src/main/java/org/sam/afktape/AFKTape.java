package org.sam.afktape;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(AFKTape.MODID)
public class AFKTape {

    public static final String MODID = "afktape";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static KeyMapping toggleAfkKey;
    public static KeyMapping unlockMouseKey;

    public AFKTape() {
        LOGGER.info("Hello World!");

        toggleAfkKey = new KeyMapping("key.afktape.toggle",
                GLFW.GLFW_KEY_K,
                "category.afktape"
        );

        unlockMouseKey = new KeyMapping("key.afktape.mouse",
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.afktape"
        );
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class clientInit {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(toggleAfkKey);
            event.register(unlockMouseKey);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("afktape_overlay", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
                if (KeybindHandler.INSTANCE.isRunning()) {
                    renderAfkTapeOverlay(guiGraphics, screenWidth, screenHeight);
                }
            });
        }

        private static void renderAfkTapeOverlay(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
            Font font = Minecraft.getInstance().font;
            String[] lines = KeybindHandler.INSTANCE.getMessage();
            int additive = 0;

            for (int i = lines.length - 1; i >= 0; i--) {
                int width = font.width(lines[i]);
                guiGraphics.drawString(
                        font,
                        lines[i],
                        (screenWidth / 2) - (width / 2),
                        ((screenHeight / 2) - 20) + additive,
                        0xFFFFFFFF,
                        true
                );
                additive -= 10;
            }
        }
    }
}

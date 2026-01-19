package io.github.samispoggers.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import java.nio.file.Path;

public class AFKTapeConfig {

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("afktape.json");

    public static final ConfigClassHandler<AFKTapeConfig> HANDLER = ConfigClassHandler.createBuilder(AFKTapeConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_PATH)
                    .build())
            .build();

    @SerialEntry
    public boolean muteWhenAfk = false;
    @SerialEntry
    public boolean kickOnDamageWhenAfk = false;
    @SerialEntry
    public boolean kickOnTotemPopWhenAfk = false;

    public static AFKTapeConfig get() {
        return HANDLER.instance();
    }

    public static void load() {
        HANDLER.load();
    }

    public static void save() {
        HANDLER.save();
    }

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("AFKTape Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("General"))
                        .tooltip(Component.literal("General settings"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("AFKTape Settings"))
                                .description(OptionDescription.of(Component.literal("Settings related to AFKTape")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Mute When AFK"))
                                        .description(OptionDescription.of(Component.literal("Mutes game when AFKTaped")))
                                        .binding(
                                                false,
                                                () -> get().muteWhenAfk,
                                                newVal -> get().muteWhenAfk = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Kick On Damage"))
                                        .description(OptionDescription.of(Component.literal("Automatically disconnects when you take damage while AFKTaped")))
                                        .binding(
                                                false,
                                                () -> get().kickOnDamageWhenAfk,
                                                newVal -> get().kickOnDamageWhenAfk = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Kick On Totem"))
                                        .description(OptionDescription.of(Component.literal("Automatically disconnects when your totem is popped while AFKTaped")))
                                        .binding(
                                                false,
                                                () -> get().kickOnTotemPopWhenAfk,
                                                newVal -> get().kickOnTotemPopWhenAfk = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .save(AFKTapeConfig::save)
                .build()
                .generateScreen(parent);
    }
}
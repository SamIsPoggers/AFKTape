package io.github.samispoggers.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Invoker("disconnect")
    void afktape$disconnect(Screen screen, boolean keepResourcePacks, boolean stopSound);
}
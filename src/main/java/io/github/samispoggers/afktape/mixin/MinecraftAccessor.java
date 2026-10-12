package io.github.samispoggers.afktape.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
	@Accessor("focused")
	void afktape$setFocused(boolean focused);
	@Accessor("focused")
	boolean afktape$getFocused();
}

package io.github.samispoggers.mixin;

import io.github.samispoggers.client.KeybindHandler;
import io.github.samispoggers.config.AFKTapeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "hurtTo", at = @At("HEAD"))
    private void onHurtTo(float health, CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer)(Object)this;

        if (health < player.getHealth()) {
            if (KeybindHandler.INSTANCE.isRunning() && AFKTapeConfig.get().kickOnDamageWhenAfk) {
                Minecraft client = Minecraft.getInstance();

                if (client.getConnection() != null) {
                    client.getConnection().getConnection().disconnect(
                            Component.literal("§cYou took damage and were disconnected!")
                    );
                }
            }
        }
    }
}
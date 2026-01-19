package io.github.samispoggers.mixin;

import io.github.samispoggers.client.KeybindHandler;
import io.github.samispoggers.config.AFKTapeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void onEntityStatus(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        byte status = packet.getEventId();

        // 35 = Totem of Undying activation
        if (status == 35) {
            if (KeybindHandler.INSTANCE.isRunning() && AFKTapeConfig.get().kickOnTotemPopWhenAfk) {
                Minecraft client = Minecraft.getInstance();

                if (client.getConnection() != null) {
                    client.getConnection().getConnection().disconnect(
                            Component.literal("§cYou popped a totem and were disconnected!")
                    );
                }
            }
        }
    }
}

package io.github.samispoggers.mixin;

import io.github.samispoggers.client.KeybindHandler;
import io.github.samispoggers.config.AFKTapeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    private static final byte TOTEM_OF_UNDYING_STATUS = 35;

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void onEntityStatus(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) {
            return;
        }

        Entity entity = packet.getEntity(client.level);
        if (packet.getEventId() == TOTEM_OF_UNDYING_STATUS && entity == client.player) {
            disconnectIfAfk(AFKTapeConfig.get().kickOnTotemPopWhenAfk, "You popped a totem and were disconnected!");
        }
    }

    @Inject(method = "handleDamageEvent", at = @At("HEAD"))
    private void onDamageEvent(ClientboundDamageEventPacket packet, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && packet.entityId() == client.player.getId()) {
            disconnectIfAfk(AFKTapeConfig.get().kickOnDamageWhenAfk, "You took damage and were disconnected!");
        }
    }

    @Inject(method = "handleSetHealth", at = @At("HEAD"))
    private void onSetHealth(ClientboundSetHealthPacket packet, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && packet.getHealth() < client.player.getHealth()) {
            disconnectIfAfk(AFKTapeConfig.get().kickOnDamageWhenAfk, "You took damage and were disconnected!");
        }
    }

    private void disconnectIfAfk(boolean enabled, String message) {
        if (!enabled || !KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            return;
        }

        KeybindHandler.INSTANCE.disable();

        Minecraft client = Minecraft.getInstance();

        client.execute(() -> {
            Screen parent = client.isLocalServer()
                    ? new TitleScreen()
                    : new JoinMultiplayerScreen(new TitleScreen());

            ((MinecraftAccessor) client).afktape$disconnect(
                    new DisconnectedScreen(
                            parent,
                            Component.translatable("disconnect.lost"),
                            Component.literal(message),
                            client.isLocalServer()
                                    ? Component.translatable("gui.toTitle")
                                    : Component.translatable("gui.toMenu")
                    ),
                    false,
                    true
            );
        });
    }
}

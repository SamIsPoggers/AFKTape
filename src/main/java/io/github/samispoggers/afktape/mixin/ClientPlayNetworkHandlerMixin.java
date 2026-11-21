package io.github.samispoggers.afktape.mixin;

import io.github.samispoggers.afktape.AFKTape;
import io.github.samispoggers.afktape.config.AFKTapeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.EntityEventS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerHealthS2CPacket;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	private static final byte ENTITY_HURT_EVENT = 2;

	@Shadow
	private Minecraft minecraft;

	@Shadow
	private ClientWorld world;

	@Inject(
		method = "handleEntityEvent",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/packet/s2c/play/EntityEventS2CPacket;getEntity(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"
		),
		cancellable = true
	)
	private void afktape$onEntityEvent(EntityEventS2CPacket packet, CallbackInfo ci) {
		if (this.world != null
			&& packet.getEvent() == ENTITY_HURT_EVENT
			&& packet.getEntity(this.world) == this.minecraft.player) {
			disconnectOnDamage(ci);
		}
	}

	@Inject(
		method = "handlePlayerHealth",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;damageTo(F)V"
		),
		cancellable = true
	)
	private void afktape$onPlayerHealth(PlayerHealthS2CPacket packet, CallbackInfo ci) {
		if (this.minecraft.player != null && packet.getHealth() < this.minecraft.player.getHealth()) {
			disconnectOnDamage(ci);
		}
	}

	private void disconnectOnDamage(CallbackInfo ci) {
		if (!AFKTapeConfig.isKickOnDamageWhenAfk()
			|| AFKTape.afkController == null
			|| !AFKTape.afkController.isAfkEnabled()) {
			return;
		}

		AFKTape.afkController.stopAFK();

		boolean singleplayer = this.minecraft.isIntegratedServerRunning();
		if (this.minecraft.world != null) {
			this.minecraft.world.disconnect();
		}
		this.minecraft.setWorld(null);

		Screen parent = singleplayer ? new TitleScreen() : new MultiplayerScreen(new TitleScreen());
		this.minecraft.openScreen(new DisconnectedScreen(
			parent,
			"disconnect.lost",
			new LiteralText("You took damage and were disconnected!")
		));
		ci.cancel();
	}
}

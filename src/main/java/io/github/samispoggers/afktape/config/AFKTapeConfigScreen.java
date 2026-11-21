package io.github.samispoggers.afktape.config;

import io.github.samispoggers.afktape.compat.OneConfigIntegration;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public final class AFKTapeConfigScreen extends Screen {
	private static final int BTN_MUTE_WHEN_AFK = 0;
	private static final int BTN_KICK_ON_DAMAGE = 1;
	private static final int BTN_UNLOCK_MOUSE = 2;
	private static final int BTN_DONE = 3;

	private final Screen parent;
	private ButtonWidget muteButton;
	private ButtonWidget kickOnDamageButton;
	private ButtonWidget unlockMouseButton;

	public AFKTapeConfigScreen(Screen parent) {
		this.parent = parent;
	}

	@Override
	public void init() {
		this.buttons.clear();

		int centerX = this.width / 2;
		int y = this.height / 2 - 42;

		this.muteButton = new ButtonWidget(
			BTN_MUTE_WHEN_AFK,
			centerX - 110,
			y,
			220,
			20,
			getMuteLabel()
		);
		this.buttons.add(this.muteButton);

		this.kickOnDamageButton = new ButtonWidget(
			BTN_KICK_ON_DAMAGE,
			centerX - 110,
			y + 24,
			220,
			20,
			getKickOnDamageLabel()
		);
		this.buttons.add(this.kickOnDamageButton);

		this.unlockMouseButton = new ButtonWidget(
			BTN_UNLOCK_MOUSE,
			centerX - 110,
			y + 48,
			220,
			20,
			getUnlockMouseLabel()
		);
		this.buttons.add(this.unlockMouseButton);

		this.buttons.add(new ButtonWidget(
			BTN_DONE,
			centerX - 100,
			this.height - 40,
			200,
			20,
			"Done"
		));
	}

	private static String getMuteLabel() {
		return "Mute When AFK: " + (AFKTapeConfig.isMuteWhenAfk() ? "ON" : "OFF");
	}

	private static String getKickOnDamageLabel() {
		return "Disconnect On Damage: " + (AFKTapeConfig.isKickOnDamageWhenAfk() ? "ON" : "OFF");
	}

	private static String getUnlockMouseLabel() {
		return "Unlock Mouse By Default: " + (AFKTapeConfig.isUnlockMouseByDefault() ? "ON" : "OFF");
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		if (!button.active) {
			return;
		}

		if (button.id == BTN_MUTE_WHEN_AFK) {
			AFKTapeConfig.setMuteWhenAfk(!AFKTapeConfig.isMuteWhenAfk());
			this.muteButton.message = getMuteLabel();
			syncOneConfig();
		} else if (button.id == BTN_KICK_ON_DAMAGE) {
			AFKTapeConfig.setKickOnDamageWhenAfk(!AFKTapeConfig.isKickOnDamageWhenAfk());
			this.kickOnDamageButton.message = getKickOnDamageLabel();
			syncOneConfig();
		} else if (button.id == BTN_UNLOCK_MOUSE) {
			AFKTapeConfig.setUnlockMouseByDefault(!AFKTapeConfig.isUnlockMouseByDefault());
			this.unlockMouseButton.message = getUnlockMouseLabel();
			syncOneConfig();
		} else if (button.id == BTN_DONE) {
			AFKTapeConfig.save();
			this.minecraft.openScreen(this.parent);
		}
	}

	private static void syncOneConfig() {
		if (FabricLoader.getInstance().isModLoaded("oneconfig")) {
			OneConfigIntegration.syncFromConfig();
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		this.renderBackground();
		this.drawCenteredString(this.textRenderer, "AFK Tape Config", this.width / 2, 20, 0xFFFFFF);
		this.drawCenteredString(
			this.textRenderer,
			"When Unlock Mouse is ON, hold Unlock Mouse key to lock cursor.",
			this.width / 2,
			this.height / 2 + 34,
			0xAAAAAA
		);
		super.render(mouseX, mouseY, tickDelta);
	}
}

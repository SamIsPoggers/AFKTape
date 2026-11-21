package io.github.samispoggers.afktape.compat;

import io.github.samispoggers.afktape.config.AFKTapeConfig;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

public final class OneConfigIntegration extends Config {
	private static OneConfigIntegration instance;

	@Switch(
		title = "Mute When AFK",
		description = "Mutes game volume while AFKTaped."
	)
	public static boolean muteWhenAfk = false;

	@Switch(
		title = "Disconnect On Damage",
		description = "Automatically disconnects when you take damage while AFKTaped."
	)
	public static boolean kickOnDamageWhenAfk = false;

	@Switch(
		title = "Unlock Mouse By Default",
		description = "Unlocks the mouse by default while AFKTaped; holding the Unlock Mouse key will lock it instead."
	)
	public static boolean unlockMouseByDefault = true;

	private OneConfigIntegration() {
		super("afktape.json", "/assets/afktape/icon.png", "AFK Tape", Category.UTILITY);
		addCallback("muteWhenAfk", () -> AFKTapeConfig.setMuteWhenAfk(muteWhenAfk));
		addCallback("kickOnDamageWhenAfk", () -> AFKTapeConfig.setKickOnDamageWhenAfk(kickOnDamageWhenAfk));
		addCallback("unlockMouseByDefault", () -> AFKTapeConfig.setUnlockMouseByDefault(unlockMouseByDefault));
		preload();
		AFKTapeConfig.setMuteWhenAfk(muteWhenAfk);
		AFKTapeConfig.setKickOnDamageWhenAfk(kickOnDamageWhenAfk);
		AFKTapeConfig.setUnlockMouseByDefault(unlockMouseByDefault);
	}

	public static void init() {
		if (instance == null) {
			instance = new OneConfigIntegration();
		}
	}

	public static void syncFromConfig() {
		muteWhenAfk = AFKTapeConfig.isMuteWhenAfk();
		kickOnDamageWhenAfk = AFKTapeConfig.isKickOnDamageWhenAfk();
		unlockMouseByDefault = AFKTapeConfig.isUnlockMouseByDefault();
		if (instance != null) {
			instance.save();
		}
	}
}

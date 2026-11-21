package io.github.samispoggers.afktape.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class AFKTapeConfig {
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("afktape.properties");
	private static final String KEY_MUTE_WHEN_AFK = "muteWhenAfk";
	private static final String KEY_KICK_ON_DAMAGE = "kickOnDamageWhenAfk";
	private static final String KEY_UNLOCK_MOUSE_BY_DEFAULT = "unlockMouseByDefault";

	private static boolean muteWhenAfk = false;
	private static boolean kickOnDamageWhenAfk = false;
	private static boolean unlockMouseByDefault = true;

	private AFKTapeConfig() {
	}

	public static boolean isMuteWhenAfk() {
		return muteWhenAfk;
	}

	public static void setMuteWhenAfk(boolean value) {
		muteWhenAfk = value;
		save();
	}

	public static boolean isKickOnDamageWhenAfk() {
		return kickOnDamageWhenAfk;
	}

	public static void setKickOnDamageWhenAfk(boolean value) {
		kickOnDamageWhenAfk = value;
		save();
	}

	public static boolean isUnlockMouseByDefault() {
		return unlockMouseByDefault;
	}

	public static void setUnlockMouseByDefault(boolean value) {
		unlockMouseByDefault = value;
		save();
	}

	public static void load() {
		if (!Files.exists(CONFIG_PATH)) {
			save();
			return;
		}

		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(CONFIG_PATH)) {
			props.load(in);
			muteWhenAfk = Boolean.parseBoolean(props.getProperty(KEY_MUTE_WHEN_AFK, "false"));
			kickOnDamageWhenAfk = Boolean.parseBoolean(props.getProperty(KEY_KICK_ON_DAMAGE, "false"));
			unlockMouseByDefault = Boolean.parseBoolean(props.getProperty(KEY_UNLOCK_MOUSE_BY_DEFAULT, "true"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		Properties props = new Properties();
		props.setProperty(KEY_MUTE_WHEN_AFK, Boolean.toString(muteWhenAfk));
		props.setProperty(KEY_KICK_ON_DAMAGE, Boolean.toString(kickOnDamageWhenAfk));
		props.setProperty(KEY_UNLOCK_MOUSE_BY_DEFAULT, Boolean.toString(unlockMouseByDefault));

		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			try (OutputStream out = Files.newOutputStream(CONFIG_PATH)) {
				props.store(out, "AFKTape Configuration");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

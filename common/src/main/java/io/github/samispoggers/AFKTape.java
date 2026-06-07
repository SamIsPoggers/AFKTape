package io.github.samispoggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AFKTape {
	public static final String MOD_ID = "afktape";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private AFKTape() {
	}

	public static void init() {
		LOGGER.info("AFKTape initialized");
	}
}

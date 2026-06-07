package io.github.samispoggers.platform;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PlatformHooks {
    private static Path configDir = Paths.get("config");

    private PlatformHooks() {
    }

    public static Path getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(Path configDir) {
        PlatformHooks.configDir = configDir;
    }
}

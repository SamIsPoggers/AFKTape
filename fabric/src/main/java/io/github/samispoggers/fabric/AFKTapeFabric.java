package io.github.samispoggers.fabric;

import io.github.samispoggers.AFKTape;
import net.fabricmc.api.ModInitializer;

public class AFKTapeFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AFKTape.init();
    }
}

package net.leashesp.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.leashesp.mod.utils.LeashESP;

public class AutoInitializer implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        // ...
    }

    @Override
    public void onInitializeClient() {
        new LeashESP().onInitializeClient();
        new KeybindRegister().onInitializeClient();
    }
}


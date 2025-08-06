package net.leashedmobtracker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.leashedmobtracker.utils.LeashESP;

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


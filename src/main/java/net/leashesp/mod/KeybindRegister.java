package net.leashesp.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;

public class KeybindRegister implements ClientModInitializer {

    private static KeyBinding configKey;

    @Override
    public void onInitializeClient() {
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open Config",
                GLFW.GLFW_KEY_M, // "M" is default keybind
                "LeashESP"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    Screen configScreen = new ModConfig().getModConfigScreenFactory().create(client.currentScreen);
                    client.setScreen(configScreen);
                }
            }
        });
    }
}

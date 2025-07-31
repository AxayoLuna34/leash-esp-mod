package net.leashesp.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeybindRegister implements ClientModInitializer {

    private static KeyBinding leashESPKey;

    @Override
    public void onInitializeClient() {
        // Register the Keybinding (default: U key)
        leashESPKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toggle Leash ESP",
                GLFW.GLFW_KEY_U,
                "LeashESP"
        ));

        // Listen for key presses every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (leashESPKey.wasPressed()) {
                boolean current = LeashESP.isEnabled;
                LeashESP.toggleBetterLead(!current);
                client.player.sendMessage(
                        net.minecraft.text.Text.literal("Leash ESP " + (current ? "disabled" : "enabled")),
                        true
                );
            }
        });
    }
}

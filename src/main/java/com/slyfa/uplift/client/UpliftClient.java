package com.slyfa.uplift.client;

import com.slyfa.uplift.util.ModKeybindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class UpliftClient implements ClientModInitializer {
    private static boolean upliftModeEnabled = true;
    
    @Override
    public void onInitializeClient() {
        ModKeybindings.register();
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ModKeybindings.toggleFlightMode.wasPressed()) {
                upliftModeEnabled = !upliftModeEnabled;
                
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.player != null) {
                    String status = upliftModeEnabled ? "enabled" : "disabled";
                    mc.player.sendMessage(Text.literal("Uplift flight mode " + status), true);
                }
            }
        });
    }
    
    public static boolean isUpliftModeEnabled() {
        return upliftModeEnabled;
    }
}

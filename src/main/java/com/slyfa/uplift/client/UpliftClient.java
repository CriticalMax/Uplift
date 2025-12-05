package com.slyfa.uplift.client;

import com.slyfa.uplift.util.ModKeybindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpliftClient implements ClientModInitializer {
    private static boolean upliftModeEnabled = false;
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("uplift-client.txt");
    
    @Override
    public void onInitializeClient() {
        ModKeybindings.register();
        
        // Load saved state
        loadConfig();
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ModKeybindings.toggleFlightMode.wasPressed()) {
                upliftModeEnabled = !upliftModeEnabled;
                
                // Save state when toggled
                saveConfig();
                
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.player != null) {
                    String status = upliftModeEnabled ? "enabled" : "disabled";
                    mc.player.sendMessage(Text.literal("Uplift flight mode " + status), true);
                }
            }
        });
    }
    
    private static void loadConfig() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String content = Files.readString(CONFIG_PATH).trim();
                upliftModeEnabled = Boolean.parseBoolean(content);
            }
        } catch (IOException e) {
            // If loading fails, keep default value
        }
    }
    
    private static void saveConfig() {
        try {
            Files.writeString(CONFIG_PATH, String.valueOf(upliftModeEnabled));
        } catch (IOException e) {
            // Silent fail on save error
        }
    }
    
    public static boolean isUpliftModeEnabled() {
        return upliftModeEnabled;
    }
}

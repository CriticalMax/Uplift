package com.slyfa.uplift.util;

import com.slyfa.uplift.Uplift;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    private static final KeyBinding.Category UPLIFT_CATEGORY = KeyBinding.Category.create(Identifier.of(Uplift.MOD_ID, "keybindings"));
    public static KeyBinding toggleFlightMode;
    
    public static void register() {
        toggleFlightMode = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.uplift.toggle_flight_mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                UPLIFT_CATEGORY
            )
        );
    }
}

package com.slyfa.uplift.enchantment;

import com.slyfa.uplift.Uplift;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> UPLIFT = of("uplift");

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Uplift.MOD_ID, id));
    }

    public static void register() {
        Uplift.LOGGER.info("Registering enchantments for " + Uplift.MOD_ID);
    }
}

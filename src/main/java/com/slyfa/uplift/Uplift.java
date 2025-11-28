package com.slyfa.uplift;

import com.slyfa.uplift.enchantment.ModEnchantments;
import com.slyfa.uplift.util.ModCustomTrades;
import net.fabricmc.api.ModInitializer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Uplift implements ModInitializer {
	public static final String MOD_ID = "uplift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEnchantments.register();
		ModCustomTrades.registerCustomTrades();
		
		LOGGER.info("Initialized {}", MOD_ID);
	}
}
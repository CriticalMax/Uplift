package com.slyfa.uplift.util;

import com.slyfa.uplift.Uplift;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;

import java.util.Optional;

public class ModCustomTrades {
    public static void registerCustomTrades() {
        Uplift.LOGGER.info("Registering Uplift villager trades...");
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 1, factories -> {
            Uplift.LOGGER.info("Adding Uplift trade factory to Librarian level 1");
            factories.add((entity, random) -> {
                // Only 12.5% chance to offer this trade (1 in 8 librarians)
                if (random.nextFloat() > 0.125f) {
                    return null;
                }
                
                Uplift.LOGGER.info("Trade factory called for entity: {}", entity.getClass().getSimpleName());
                try {
                    // Get the world from entity - villager entities have getEntityWorld()
                    if (!(entity instanceof net.minecraft.entity.passive.VillagerEntity villager)) {
                        Uplift.LOGGER.warn("Entity is not a villager!");
                        return null;
                    }
                    
                    World world = villager.getEntityWorld();
                    
                    Uplift.LOGGER.info("World type: {}, isClient: {}", world.getClass().getSimpleName(), world.isClient());
                    
                    // Only proceed if we're on the server
                    if (world.isClient()) {
                        Uplift.LOGGER.info("Client world detected, returning null");
                        return null;
                    }
                    
                    if (!(world instanceof ServerWorld serverWorld)) {
                        Uplift.LOGGER.info("Not a server world, returning null");
                        return null;
                    }
                    
                    // Get the registry manager and enchantment registry
                    DynamicRegistryManager registryManager = serverWorld.getRegistryManager();
                    var enchantmentRegistry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT);
                    
                    // Get the Uplift enchantment using the Identifier
                    Identifier upliftId = Identifier.of(Uplift.MOD_ID, "uplift");
                    Optional<RegistryEntry.Reference<Enchantment>> upliftEntry = 
                        enchantmentRegistry.getEntry(upliftId);
                    
                    if (upliftEntry.isPresent()) {
                        Uplift.LOGGER.info("Uplift enchantment found, creating trade offer");
                        // Create the enchanted book
                        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                        
                        // Add the enchantment
                        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(
                            ItemEnchantmentsComponent.DEFAULT
                        );
                        builder.add(upliftEntry.get(), 1);
                        enchantedBook.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
                        
                        Uplift.LOGGER.info("Created Uplift enchanted book trade successfully");
                        // Randomize emerald price between 14-30 with very slight upper bias
                        // Average four random values with one being the max of two for minimal bias
                        int emeraldCount = 14 + (random.nextInt(17) + random.nextInt(17) + random.nextInt(17) + Math.max(random.nextInt(17), random.nextInt(17))) / 4;
                        
                        // Create the trade offer
                        return new TradeOffer(
                            new TradedItem(Items.EMERALD, emeraldCount),
                            Optional.of(new TradedItem(Items.BOOK, 1)),
                            enchantedBook,
                            4,  // Max uses
                            30, // Villager XP
                            0.2f // Price multiplier
                        );
                    } else {
                        Uplift.LOGGER.warn("Uplift enchantment not found in registry!");
                    }
                } catch (Exception e) {
                    Uplift.LOGGER.error("Error creating Uplift enchanted book trade", e);
                }
                return null;
            });
        });
        Uplift.LOGGER.info("Uplift villager trades registered");
    }    
}

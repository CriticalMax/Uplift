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
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 1, factories -> {
            factories.add((entity, random) -> {
                try {
                    // Use reflection to access the protected world field
                    java.lang.reflect.Field worldField = net.minecraft.entity.Entity.class.getDeclaredField("world");
                    worldField.setAccessible(true);
                    World world = (World) worldField.get(entity);
                    
                    // Only proceed if we're on the server
                    if (!(world instanceof ServerWorld serverWorld)) {
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
                        // Create the enchanted book
                        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                        
                        // Add the enchantment using the component system
                        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(
                            ItemEnchantmentsComponent.DEFAULT
                        );
                        builder.add(upliftEntry.get(), 1);
                        enchantedBook.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
                        
                        // Create the trade offer
                        return new TradeOffer(
                            new TradedItem(Items.EMERALD, 22),
                            Optional.of(new TradedItem(Items.BOOK, 1)),
                            enchantedBook,
                            4,  // Max uses
                            30, // Villager XP
                            0.2f // Price multiplier
                        );
                    }
                } catch (Exception e) {
                    Uplift.LOGGER.error("Error creating Uplift enchanted book trade", e);
                }
                return null;
            });
        });
    }    
}

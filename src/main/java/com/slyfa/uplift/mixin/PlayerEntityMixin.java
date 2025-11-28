package com.slyfa.uplift.mixin;

import com.slyfa.uplift.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Unique
    private int uplift$flightTicks = 0;
    @Unique
    private boolean uplift$wasFlying = false;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        // Don't modify creative/spectator mode players
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
        
        // Check if wearing elytra with Uplift enchantment
        if (chestplate.isOf(Items.ELYTRA)) {
            boolean hasUplift = EnchantmentHelper.getEnchantments(chestplate)
                .getEnchantments()
                .stream()
                .anyMatch(entry -> entry.matchesKey(ModEnchantments.UPLIFT));
            
            if (hasUplift) {
                // Check if player has at least one rocket in inventory
                boolean hasRocket = player.getInventory().containsAny(stack -> 
                    stack.isOf(Items.FIREWORK_ROCKET));
                
                if (hasRocket) {
                    // Grant creative flight
                    if (!player.getAbilities().allowFlying) {
                        player.getAbilities().allowFlying = true;
                        player.sendAbilitiesUpdate();
                    }
                }
                
                // Track flight state and consume rockets
                if (player.getAbilities().flying) {
                    // Player just started flying
                    if (!uplift$wasFlying) {
                        uplift$wasFlying = true;
                        uplift$flightTicks = 0;
                        // Consume 1 rocket immediately
                        boolean consumed = uplift$consumeRocket(player);
                        if (!consumed) {
                            // No rocket available, disable flight
                            player.getAbilities().allowFlying = false;
                            player.getAbilities().flying = false;
                            player.sendAbilitiesUpdate();
                            uplift$wasFlying = false;
                        }
                    } else {
                        // Increment flight time
                        uplift$flightTicks++;
                        
                        // Every 30 seconds (600 ticks), consume another rocket
                        if (uplift$flightTicks >= 600) {
                            boolean consumed = uplift$consumeRocket(player);
                            if (!consumed) {
                                // No rocket available, disable flight
                                player.getAbilities().allowFlying = false;
                                player.getAbilities().flying = false;
                                player.sendAbilitiesUpdate();
                                uplift$wasFlying = false;
                            }
                            uplift$flightTicks = 0;
                        }
                    }
                } else {
                    // Player stopped flying
                    if (uplift$wasFlying) {
                        uplift$wasFlying = false;
                        uplift$flightTicks = 0;
                    }
                    
                    // If no rockets, disable flight ability
                    if (!hasRocket && player.getAbilities().allowFlying) {
                        player.getAbilities().allowFlying = false;
                        player.sendAbilitiesUpdate();
                    }
                }
            } else {
                // Remove creative flight if it was granted by this mod
                if (player.getAbilities().allowFlying && !player.getAbilities().flying) {
                    player.getAbilities().allowFlying = false;
                    player.sendAbilitiesUpdate();
                } else if (player.getAbilities().flying && player.getAbilities().allowFlying) {
                    // Player is currently flying, let them land first
                    player.getAbilities().allowFlying = true;
                }
            }
        } else {
            // Not wearing elytra with Uplift, remove creative flight
            if (player.getAbilities().allowFlying && !player.isCreative() && !player.isSpectator()) {
                player.getAbilities().allowFlying = false;
                player.getAbilities().flying = false;
                player.sendAbilitiesUpdate();
            }
        }
    }
    
    @Unique
    private boolean uplift$consumeRocket(PlayerEntity player) {
        // Find and consume one rocket from inventory
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(Items.FIREWORK_ROCKET)) {
                stack.decrement(1);
                return true;
            }
        }
        return false; // No rocket found
    }
}

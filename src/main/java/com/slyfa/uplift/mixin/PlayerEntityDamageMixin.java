package com.slyfa.uplift.mixin;

import com.slyfa.uplift.client.UpliftClient;
import com.slyfa.uplift.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class PlayerEntityDamageMixin {
    @Unique
    private int uplift$damageTicks = 0;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void applyFlyingDamage(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        // Only apply to players
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        
        // Don't apply in creative/spectator
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        // Check if flying with Uplift
        if (player.getAbilities().flying) {
            ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
            if (chestplate.isOf(Items.ELYTRA)) {
                boolean hasUplift = EnchantmentHelper.getEnchantments(chestplate)
                    .getEnchantments()
                    .stream()
                    .anyMatch(entry -> entry.matchesKey(ModEnchantments.UPLIFT));
                
                if (hasUplift) {
                    boolean upliftModeEnabled = player.getEntityWorld().isClient() ? UpliftClient.isUpliftModeEnabled() : true;
                    if (upliftModeEnabled) {
                        // Increment damage tick counter
                        uplift$damageTicks++;
                        
                        // Apply 1 durability damage every 120 ticks (6 seconds) while flying
                        if (uplift$damageTicks >= 120) {
                            chestplate.damage(1, player, EquipmentSlot.CHEST);
                            uplift$damageTicks = 0;
                        }
                    }
                } else {
                    uplift$damageTicks = 0;
                }
            } else {
                uplift$damageTicks = 0;
            }
        } else {
            uplift$damageTicks = 0;
        }
    }
}

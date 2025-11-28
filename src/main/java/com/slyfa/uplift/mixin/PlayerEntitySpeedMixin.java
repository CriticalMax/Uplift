package com.slyfa.uplift.mixin;

import com.slyfa.uplift.client.UpliftClient;
import com.slyfa.uplift.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntitySpeedMixin {
    
    @Inject(method = "travel", at = @At("HEAD"))
    private void preventSprintSpeed(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        // Force disable sprinting before travel calculation when flying with Uplift
        if (player.isSprinting() && player.getAbilities().flying && !player.isCreative() && !player.isSpectator()) {
            ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
            if (chest.isOf(Items.ELYTRA)) {
                boolean hasUplift = EnchantmentHelper.getEnchantments(chest)
                    .getEnchantments()
                    .stream()
                    .anyMatch(entry -> entry.matchesKey(ModEnchantments.UPLIFT));
                
                if (hasUplift) {
                    boolean upliftModeEnabled = player.getEntityWorld().isClient() ? UpliftClient.isUpliftModeEnabled() : true;
                    if (upliftModeEnabled) {
                        player.setSprinting(false);
                    }
                }
            }
        }
    }
}

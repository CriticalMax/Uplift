package com.slyfa.uplift.mixin;

import com.slyfa.uplift.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMiningMixin {
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void onGetBlockBreakingSpeed(CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        // Check if player is flying and wearing elytra with Uplift
        if (player.getAbilities().flying && !player.isCreative() && !player.isSpectator()) {
            ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
            
            if (chestplate.isOf(Items.ELYTRA)) {
                boolean hasUplift = EnchantmentHelper.getEnchantments(chestplate)
                    .getEnchantments()
                    .stream()
                    .anyMatch(entry -> entry.matchesKey(ModEnchantments.UPLIFT));
                
                if (hasUplift) {
                    // Multiply by 5 to counteract the flying penalty (which divides by 5)
                    float currentSpeed = cir.getReturnValue();
                    cir.setReturnValue(currentSpeed * 5.0f);
                }
            }
        }
    }
}

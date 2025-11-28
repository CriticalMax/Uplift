package com.slyfa.uplift.mixin;

import com.slyfa.uplift.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        if (entity instanceof PlayerEntity player) {
            if (player.getAbilities().flying && !player.isCreative() && !player.isSpectator()) {
                ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
                
                if (chestplate.isOf(Items.ELYTRA)) {
                    boolean hasUplift = EnchantmentHelper.getEnchantments(chestplate)
                        .getEnchantments()
                        .stream()
                        .anyMatch(entry -> entry.matchesKey(ModEnchantments.UPLIFT));
                    
                    if (hasUplift) {
                        // Apply velocity dampening for more responsive flight
                        Vec3d velocity = player.getVelocity();
                        player.setVelocity(velocity.multiply(0.5, 0.5, 0.5));
                    }
                }
            }
        }
    }
}

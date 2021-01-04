package com.oroarmor.multi_item_lib.mixin;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;

@Mixin(DigDurabilityEnchantment.class)
public class UnbreakingEnchantmentMixin {
    @Redirect(method = "shouldIgnoreDurabilityDrop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private static Item shouldPreventDamage(ItemStack stack) {
        return UniqueItemRegistry.ELYTRA.getDefaultItem(stack.getItem());
    }
}

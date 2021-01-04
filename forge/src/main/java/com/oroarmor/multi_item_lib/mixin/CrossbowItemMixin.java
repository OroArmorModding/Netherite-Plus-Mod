package com.oroarmor.multi_item_lib.mixin;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Redirect(method = "getShootingPower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private static Item tickMovement(ItemStack itemStack) {
        return UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
    }
}

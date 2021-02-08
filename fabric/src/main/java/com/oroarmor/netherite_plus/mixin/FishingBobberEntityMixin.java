package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    @Redirect(method = "removeIfInvalid", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item setFishingRodToNetherite(ItemStack stack){
        return stack.getItem() == NetheritePlusItems.NETHERITE_FISHING_ROD.get() ? Items.FISHING_ROD : stack.getItem();
    }
}

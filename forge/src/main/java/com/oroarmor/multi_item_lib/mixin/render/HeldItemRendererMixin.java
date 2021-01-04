package com.oroarmor.multi_item_lib.mixin.render;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


@Mixin(ItemInHandRenderer.class)
public class HeldItemRendererMixin {
    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private Item renderFirstPersonItem(ItemStack itemStack) {
        return UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
    }

    @Redirect(method = "renderHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private Item renderItem(ItemStack itemStack) {
        return UniqueItemRegistry.BOW.isItemInRegistry(itemStack.getItem()) ? Items.BOW : UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
    }
}

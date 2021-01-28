package com.oroarmor.netherite_plus.mixin.render;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    public Item redirectCapeBlocks(ItemStack stack){
        return UniqueItemRegistry.ELYTRA.getDefaultItem(stack.getItem());
    }
}

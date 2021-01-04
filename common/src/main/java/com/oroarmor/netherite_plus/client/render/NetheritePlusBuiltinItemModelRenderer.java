package com.oroarmor.netherite_plus.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShieldItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShulkerBoxItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteTridentItemRenderer;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class NetheritePlusBuiltinItemModelRenderer extends BlockEntityWithoutLevelRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (itemStack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT.get()) {
            NetheriteTridentItemRenderer.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
        } else if (itemStack.getItem() == NetheritePlusItems.NETHERITE_SHIELD.get()) {
            NetheriteShieldItemRenderer.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
        } else if (itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof NetheriteShulkerBoxBlock) {
            NetheriteShulkerBoxItemRenderer.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
        } else {
            super.renderByItem(itemStack, transformType, poseStack, multiBufferSource, i, j);
        }
    }
}

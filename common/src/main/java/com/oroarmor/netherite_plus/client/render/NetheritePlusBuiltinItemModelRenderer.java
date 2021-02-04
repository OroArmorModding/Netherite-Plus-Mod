package com.oroarmor.netherite_plus.client.render;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShieldItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShulkerBoxItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteTridentItemRenderer;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class NetheritePlusBuiltinItemModelRenderer extends BuiltinModelItemRenderer {
    @Override
    public void render(ItemStack itemStack, ModelTransformation.Mode transformType, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i, int j) {
        if (NetheritePlusConfig.ENABLED.ENABLED_TRIDENT.getValue() && itemStack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT.get()) {
            NetheriteTridentItemRenderer.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
        } else if (NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue() && itemStack.getItem() == NetheritePlusItems.NETHERITE_SHIELD.get()) {
            NetheriteShieldItemRenderer.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
        } else if (itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof NetheriteShulkerBoxBlock) {
            NetheriteShulkerBoxItemRenderer.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
        } else {
            super.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
        }
    }
}

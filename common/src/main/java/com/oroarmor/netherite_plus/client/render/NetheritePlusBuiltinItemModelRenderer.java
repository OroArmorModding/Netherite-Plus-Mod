/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

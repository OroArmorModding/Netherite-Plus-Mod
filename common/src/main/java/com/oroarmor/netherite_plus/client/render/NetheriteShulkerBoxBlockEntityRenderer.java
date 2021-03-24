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
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

@Environment(EnvType.CLIENT)
public class NetheriteShulkerBoxBlockEntityRenderer extends BlockEntityRenderer<NetheriteShulkerBoxBlockEntity> {
    private final ShulkerEntityModel<?> model;

    public NetheriteShulkerBoxBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
        model = new ShulkerEntityModel<>();
    }

    @Override
    public void render(NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction direction = Direction.UP;
        if (shulkerBoxBlockEntity.hasWorld()) {
            BlockState blockState = shulkerBoxBlockEntity.getWorld().getBlockState(shulkerBoxBlockEntity.getPos());
            if (blockState.getBlock() instanceof NetheriteShulkerBoxBlock) {
                direction = blockState.get(NetheriteShulkerBoxBlock.FACING);
            }
        }

        DyeColor dyeColor = shulkerBoxBlockEntity.getColor();
        SpriteIdentifier spriteIdentifier2;
        if (dyeColor == null) {
            spriteIdentifier2 = new SpriteIdentifier(NetheritePlusTextures.SHULKER_BOXES_ATLAS_TEXTURE, id("entity/netherite_shulker/netherite_shulker"));
        } else {
            spriteIdentifier2 = new SpriteIdentifier(NetheritePlusTextures.SHULKER_BOXES_ATLAS_TEXTURE, id("entity/netherite_shulker/netherite_shulker_" + dyeColor.getName()));
        }

        matrixStack.push();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.scale(0.9995F, 0.9995F, 0.9995F);
        matrixStack.multiply(direction.getRotationQuaternion());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0D, -1.0D, 0.0D);
        VertexConsumer vertexConsumer = spriteIdentifier2.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutoutNoCull);
        model.getBottomShell().render(matrixStack, vertexConsumer, i, j);
        matrixStack.translate(0.0D, -shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5F, 0.0D);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270.0F * shulkerBoxBlockEntity.getAnimationProgress(f)));
        model.getTopShell().render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
    }
}

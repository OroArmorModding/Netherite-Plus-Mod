/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
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

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class NetheriteShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<NetheriteShulkerBoxBlockEntity> {
    private final ShulkerModel<?> model;

    public NetheriteShulkerBoxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        model = new ShulkerModel<>(context.bakeLayer(ModelLayers.SHULKER));
    }

    @Override
    public void render(NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
        Direction direction = Direction.UP;
        if (shulkerBoxBlockEntity.hasLevel()) {
            BlockState blockState = shulkerBoxBlockEntity.getLevel().getBlockState(shulkerBoxBlockEntity.getBlockPos());
            if (blockState.getBlock() instanceof ShulkerBoxBlock) {
                direction = blockState.getValue(ShulkerBoxBlock.FACING);
            }
        }

        DyeColor dyeColor = shulkerBoxBlockEntity.getColor();
        Material spriteIdentifier;
        if (dyeColor == null) {
            spriteIdentifier = new Material(NetheritePlusTextures.NETHERITE_SHULKER_BOXES_ATLAS_TEXTURE, id("entity/netherite_shulker/netherite_shulker"));
        } else {
            spriteIdentifier = new Material(NetheritePlusTextures.NETHERITE_SHULKER_BOXES_ATLAS_TEXTURE, id("entity/netherite_shulker/netherite_shulker_" + dyeColor.getName()));
        }

        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        float scale = 0.9995F;
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(direction.getRotation());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0, -1.0, 0.0);
        ModelPart modelPart = this.model.getLid();
        modelPart.setPos(0.0F, 24.0F - shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5F * 16.0F, 0.0F);
        modelPart.yRot = 270.0F * shulkerBoxBlockEntity.getAnimationProgress(f) * (float) (Math.PI / 180.0);
        VertexConsumer vertexConsumer = spriteIdentifier.buffer(vertexConsumerProvider, RenderType::entityCutoutNoCull);
        this.model.renderToBuffer(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}

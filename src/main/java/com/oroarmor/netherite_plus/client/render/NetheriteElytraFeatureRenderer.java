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

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NetheriteElytraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public static final ResourceLocation NETHERITE_ELYTRA_SKIN = id("textures/entity/netherite_elytra.png");
    private final ElytraModel<T> elytra;

    public NetheriteElytraFeatureRenderer(RenderLayerParent<T, M> featureRendererContext, EntityModelSet entityModelLoader) {
        super(featureRendererContext);
        this.elytra = new ElytraModel<>(entityModelLoader.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (itemStack.is(NetheritePlusItems.NETHERITE_ELYTRA)) {
            ResourceLocation identifier4;
            if (livingEntity instanceof AbstractClientPlayer abstractClientPlayerEntity) {
                if (abstractClientPlayerEntity.isElytraLoaded() && abstractClientPlayerEntity.getElytraTextureLocation() != null) {
                    identifier4 = abstractClientPlayerEntity.getElytraTextureLocation();
                } else if (abstractClientPlayerEntity.isCapeLoaded() && abstractClientPlayerEntity.getCloakTextureLocation() != null && abstractClientPlayerEntity.isModelPartShown(PlayerModelPart.CAPE)) {
                    identifier4 = abstractClientPlayerEntity.getCloakTextureLocation();
                } else {
                    identifier4 = NETHERITE_ELYTRA_SKIN;
                }
            } else {
                identifier4 = NETHERITE_ELYTRA_SKIN;
            }

            matrixStack.pushPose();
            matrixStack.translate(0.0D, 0.0D, 0.125D);
            getParentModel().copyPropertiesTo(this.elytra);
            this.elytra.setupAnim(livingEntity, f, g, j, k, l);
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumerProvider, this.elytra.renderType(identifier4), false, itemStack.hasFoil());
            this.elytra.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }

}

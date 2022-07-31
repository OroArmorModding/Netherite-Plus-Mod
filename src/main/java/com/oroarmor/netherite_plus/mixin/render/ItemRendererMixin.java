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

package com.oroarmor.netherite_plus.mixin.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import static com.oroarmor.netherite_plus.NetheritePlusMod.MOD_ID;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow @Final private BuiltinModelItemRenderer builtinModelItemRenderer;

//    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), cancellable = true)
//    public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
//        if (stack.isOf(NetheritePlusItems.NETHERITE_TRIDENT)) {
//            if (!stack.isEmpty()) {
//                matrices.push();
//                boolean bl = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED;
//                if (bl) {
//                    model = ((ItemRendererAccessor) this).netherite_plus$getModels().getModelManager().getModel(new ModelIdentifier(MOD_ID + ":netherite_trident#inventory"));
//                }
//
//                model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
//                matrices.translate(-0.5, -0.5, -0.5);
//                if (!model.isBuiltin() && bl) {
//                    boolean direct = true;
//                    RenderLayer renderLayer = RenderLayers.getItemLayer(stack, direct);
//                    VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());
//
//                    ((ItemRendererAccessor) this).netherite_plus$renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
//                } else {
//                    this.builtinModelItemRenderer.render(stack, renderMode, matrices, vertexConsumers, light, overlay);
//                }
//
//                matrices.pop();
//            }
//            info.cancel();
//        }
//    }

    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedModelManager;getModel(Lnet/minecraft/client/util/ModelIdentifier;)Lnet/minecraft/client/render/model/BakedModel;", shift = At.Shift.BY, by = 2, ordinal = 0),
            index = 8)
    public BakedModel useNetheriteTridentModel(BakedModel value, ItemStack stack) {
        if (stack.isOf(NetheritePlusItems.NETHERITE_TRIDENT)) {
            return ((ItemRendererAccessor) this).netherite_plus$getModels().getModelManager().getModel(new ModelIdentifier(MOD_ID + ":netherite_trident#inventory"));
        }
        return value;
    }

    @ModifyVariable(method = "getHeldItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemModels;getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;", shift = At.Shift.BY, by = 2), index = 5)
    public BakedModel getHeldNetheriteTridentModel(BakedModel value, ItemStack stack) {
        if (stack.isOf(NetheritePlusItems.NETHERITE_TRIDENT)) {
            return ((ItemRendererAccessor) this).netherite_plus$getModels().getModelManager().getModel(new ModelIdentifier(MOD_ID + ":netherite_trident_in_hand#inventory"));
        }
        return value;
    }
}

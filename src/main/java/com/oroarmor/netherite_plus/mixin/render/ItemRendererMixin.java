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

package com.oroarmor.netherite_plus.mixin.render;

import static com.oroarmor.netherite_plus.NetheritePlusMod.MOD_ID;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelManager;getModel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)Lnet/minecraft/client/resources/model/BakedModel;", shift = At.Shift.BY, by = 2, ordinal = 0),
            index = 8)
    public BakedModel useNetheriteTridentModel(BakedModel value, ItemStack stack) {
        if (stack.is(NetheritePlusItems.NETHERITE_TRIDENT)) {
            return ((ItemRendererAccessor) this).netherite_plus$getModels().getModelManager().getModel(new ModelResourceLocation(MOD_ID, "netherite_trident", "inventory"));
        }
        return value;
    }

    @ModifyVariable(method = "getModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", shift = At.Shift.BY, by = 2), index = 5)
    public BakedModel getHeldNetheriteTridentModel(BakedModel value, ItemStack stack) {
        if (stack.is(NetheritePlusItems.NETHERITE_TRIDENT)) {
            return ((ItemRendererAccessor) this).netherite_plus$getModels().getModelManager().getModel(new ModelResourceLocation(MOD_ID, "netherite_trident_in_hand", "inventory"));
        }
        return value;
    }
}

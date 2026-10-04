/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.client.renderer.NetheritePlusSheets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.SpriteMapper;
import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

@Mixin(ShulkerBoxSpecialRenderer.Unbaked.class)
public class ShulkerBoxSpecialRendererUnbakedMixin {
    @WrapOperation(method = "bake(Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext;)Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SpriteMapper;apply(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/resources/model/sprite/SpriteId;"))
    SpriteId useNetheriteMapper(SpriteMapper instance, Identifier path, Operation<SpriteId> original) {
        if (path.getNamespace().contains(NetheritePlusMod.MOD_ID)) {
            return original.call(NetheritePlusSheets.NETHERITE_SHULKER_MAPPER, path);
        }
        return original.call(instance, path);
    }
}

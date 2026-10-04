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

package com.oroarmor.netherite_plus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlocks;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @SuppressWarnings("NotNullFieldNotInitialized")
    @Unique
    // Hack to not flash the non-discounted price on the client.
    // If the block state is updated to a vanilla anvil, the value won't be changed until the output is modified.
    // From my testing this only happens with a setblock or similar, so not a huge worry.
    private DataSlot isNetherite;

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    void addNetheriteDataSlot(int containerId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.isNetherite = DataSlot.standalone();
        this.addDataSlot(isNetherite);

        access.execute((level, blockPos) -> {
            if (!level.isClientSide()) this.isNetherite.set(level.getBlockState(blockPos).is(NetheritePlusBlocks.NETHERITE_ANVIL) ? 1 : 0);
        });
    }

    @ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(JJJ)J"))
    long discountPrice(long finalPrice) {
        access.execute((level, blockPos) -> {
            if (!level.isClientSide()) this.isNetherite.set(level.getBlockState(blockPos).is(NetheritePlusBlocks.NETHERITE_ANVIL) ? 1 : 0);
        });
        if (this.isNetherite.get() == 1) {
            finalPrice = (int) (finalPrice * (1d - NetheritePlusMod.CONFIG.anvil.xpReduction));
        }
        return finalPrice;
    }
}

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

package com.oroarmor.netherite_plus.mixin.forge;

import java.util.Map;

import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

@Mixin(NetheriteAnvilScreenHandler.class)
public abstract class NetheriteAnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow(remap = false)
    private String newItemName;

    public NetheriteAnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> arg, int i, PlayerInventory arg2, ScreenHandlerContext arg3) {
        super(arg, i, arg2, arg3);
    }

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/EnchantedBookItem;getEnchantmentTag(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/nbt/ListTag;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void injectAnvilUpdate(CallbackInfo info, ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3, Map<Enchantment, Integer> map) {
        if (!onAnvilChange((NetheriteAnvilScreenHandler) (Object) this, itemStack, itemStack2, this.output, this.newItemName, j, this.player)) {
            info.cancel();
        }
    }

    private static boolean onAnvilChange(NetheriteAnvilScreenHandler container, @Nonnull ItemStack left, @Nonnull ItemStack right, Inventory outputSlot, String name, int baseCost, PlayerEntity player) {
        AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost, player);
        if (MinecraftForge.EVENT_BUS.post(e)) {
            return false;
        } else if (e.getOutput().isEmpty()) {
            return true;
        } else {
            outputSlot.setStack(0, e.getOutput());
            container.levelCost.set(e.getCost());
            container.repairItemUsage = e.getMaterialCost();
            return false;
        }
    }
}

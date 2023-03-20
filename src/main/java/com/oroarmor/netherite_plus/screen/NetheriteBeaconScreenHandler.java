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

package com.oroarmor.netherite_plus.screen;

import java.util.Optional;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteBeaconScreenHandler extends ScreenHandler {
    private final Inventory payment = new SimpleInventory(1) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.getItem() == Items.NETHERITE_INGOT;
        }

        @Override
        public int getMaxCountPerStack() {
            return 1;
        }
    };
    private final PaymentSlot paymentSlot;
    private final ScreenHandlerContext context;
    private final PropertyDelegate propertyDelegate;

    public NetheriteBeaconScreenHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, new ArrayPropertyDelegate(4), ScreenHandlerContext.EMPTY);
    }

    public NetheriteBeaconScreenHandler(int syncId, Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        super(NetheritePlusScreenHandlers.NETHERITE_BEACON, syncId);
        checkDataCount(propertyDelegate, 3);
        this.propertyDelegate = propertyDelegate;
        this.context = context;
        this.paymentSlot = new PaymentSlot(this.payment, 0, 136, 110);
        addSlot(this.paymentSlot);
        addProperties(propertyDelegate);

        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                addSlot(new Slot(inventory, l + m * 9 + 9, 36 + l * 18, 137 + m * 18));
            }
        }

        for (int m = 0; m < 9; ++m) {
            addSlot(new Slot(inventory, m, 36 + m * 18, 195));
        }

    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if (!player.world.isClient) {
            ItemStack itemStack = paymentSlot.takeStack(paymentSlot.getMaxItemCount());
            if (!itemStack.isEmpty()) {
                player.dropItem(itemStack, false);
            }

        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, NetheritePlusBlocks.NETHERITE_BEACON);
    }

    @Override
    public void setProperty(int id, int value) {
        super.setProperty(id, value);
        sendContentUpdates();
    }

    @Override
    public ItemStack quickTransfer(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!insertItem(itemStack2, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (!paymentSlot.hasStack() && paymentSlot.canInsert(itemStack2) && itemStack2.getCount() == 1) {
                if (!insertItem(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 1 && index < 28) {
                if (!insertItem(itemStack2, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                if (!insertItem(itemStack2, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(itemStack2, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Environment(EnvType.CLIENT)
    public int getProperties() {
        return propertyDelegate.get(0);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public StatusEffect getPrimaryEffect() {
        return StatusEffect.byRawId(propertyDelegate.get(1));
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public StatusEffect getSecondaryEffect() {
        return StatusEffect.byRawId(propertyDelegate.get(2));
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public StatusEffect getTertiaryEffect() {
        return StatusEffect.byRawId(propertyDelegate.get(3));
    }

    public void setEffects(Optional<StatusEffect> primaryEffect, Optional<StatusEffect> secondaryEffect, Optional<StatusEffect> tertiaryEffect) {
        if (paymentSlot.hasStack()) {
            this.propertyDelegate.set(1, primaryEffect.map(StatusEffect::getRawId).orElse(-1));
            this.propertyDelegate.set(2, secondaryEffect.map(StatusEffect::getRawId).orElse(-1));
            this.propertyDelegate.set(3, tertiaryEffect.map(StatusEffect::getRawId).orElse(-1));
            this.paymentSlot.takeStack(1);
            this.context.run(World::markDirty);
        }
    }

    public boolean hasPayment() {
        return !payment.getStack(0).isEmpty();
    }

    class PaymentSlot extends Slot {
        public PaymentSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.getItem() == Items.NETHERITE_INGOT;
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }
    }

}

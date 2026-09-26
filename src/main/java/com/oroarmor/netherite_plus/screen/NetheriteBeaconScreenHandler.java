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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class NetheriteBeaconScreenHandler extends AbstractContainerMenu {
    private final Container payment = new SimpleContainer(1) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return stack.getItem() == Items.NETHERITE_INGOT;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };
    private final PaymentSlot paymentSlot;
    private final ContainerLevelAccess context;
    private final ContainerData propertyDelegate;

    public NetheriteBeaconScreenHandler(int syncId, Container inventory) {
        this(syncId, inventory, new SimpleContainerData(4), ContainerLevelAccess.NULL);
    }

    public NetheriteBeaconScreenHandler(int syncId, Container inventory, ContainerData propertyDelegate, ContainerLevelAccess context) {
        super(NetheritePlusScreenHandlers.NETHERITE_BEACON, syncId);
        checkContainerDataCount(propertyDelegate, 3);
        this.propertyDelegate = propertyDelegate;
        this.context = context;
        this.paymentSlot = new PaymentSlot(this.payment, 0, 136, 110);
        addSlot(this.paymentSlot);
        addDataSlots(propertyDelegate);

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
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            ItemStack itemStack = paymentSlot.remove(paymentSlot.getMaxStackSize());
            if (!itemStack.isEmpty()) {
                player.drop(itemStack, false);
            }

        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(context, player, NetheritePlusBlocks.NETHERITE_BEACON);
    }

    @Override
    public void setData(int id, int value) {
        super.setData(id, value);
        broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!moveItemStackTo(itemStack2, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (!paymentSlot.hasItem() && paymentSlot.mayPlace(itemStack2) && itemStack2.getCount() == 1) {
                if (!moveItemStackTo(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 1 && index < 28) {
                if (!moveItemStackTo(itemStack2, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                if (!moveItemStackTo(itemStack2, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemStack2, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    @Environment(EnvType.CLIENT)
    public int getProperties() {
        return propertyDelegate.get(0);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public MobEffect getPrimaryEffect() {
        return MobEffect.byId(propertyDelegate.get(1));
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public MobEffect getSecondaryEffect() {
        return MobEffect.byId(propertyDelegate.get(2));
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public MobEffect getTertiaryEffect() {
        return MobEffect.byId(propertyDelegate.get(3));
    }

    public void setEffects(Optional<MobEffect> primaryEffect, Optional<MobEffect> secondaryEffect, Optional<MobEffect> tertiaryEffect) {
        if (paymentSlot.hasItem()) {
            this.propertyDelegate.set(1, primaryEffect.map(MobEffect::getId).orElse(-1));
            this.propertyDelegate.set(2, secondaryEffect.map(MobEffect::getId).orElse(-1));
            this.propertyDelegate.set(3, tertiaryEffect.map(MobEffect::getId).orElse(-1));
            this.paymentSlot.remove(1);
            this.context.execute(Level::blockEntityChanged);
        }
    }

    public boolean hasPayment() {
        return !payment.getItem(0).isEmpty();
    }

    class PaymentSlot extends Slot {
        public PaymentSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == Items.NETHERITE_INGOT;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

}

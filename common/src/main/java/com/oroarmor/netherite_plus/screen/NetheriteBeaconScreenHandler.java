package com.oroarmor.netherite_plus.screen;

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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteBeaconScreenHandler extends ScreenHandler {
	private final Inventory payment;
	private final NetheriteBeaconScreenHandler.PaymentSlot paymentSlot;
	private final ScreenHandlerContext context;
	private final PropertyDelegate propertyDelegate;

	public NetheriteBeaconScreenHandler(int syncId, Inventory inventory) {
		this(syncId, inventory, new ArrayPropertyDelegate(4), ScreenHandlerContext.EMPTY);
	}

	public NetheriteBeaconScreenHandler(int syncId, Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
		super(NetheritePlusScreenHandlers.NETHERITE_BEACON, syncId);
		payment = new SimpleInventory(1) {
			@Override
			public boolean isValid(int slot, ItemStack stack) {
				return stack.getItem() == Items.NETHERITE_INGOT;
			}

			@Override
			public int getMaxCountPerStack() {
				return 1;
			}
		};
		checkDataCount(propertyDelegate, 3);
		this.propertyDelegate = propertyDelegate;
		this.context = context;
		paymentSlot = new NetheriteBeaconScreenHandler.PaymentSlot(payment, 0, 136, 110);
		addSlot(paymentSlot);
		addProperties(propertyDelegate);

		int m;
		for (m = 0; m < 3; ++m) {
			for (int l = 0; l < 9; ++l) {
				addSlot(new Slot(inventory, l + m * 9 + 9, 36 + l * 18, 137 + m * 18));
			}
		}

		for (m = 0; m < 9; ++m) {
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
		return canUse(context, player, NetheritePlusBlocks.NETHERITE_BEACON.get());
	}

	@Override
	public void setProperty(int id, int value) {
		super.setProperty(id, value);
		sendContentUpdates();
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index == 0) {
				if (!insertItem(itemStack2, 1, 37, true)) {
					return ItemStack.EMPTY;
				}

				slot.onStackChanged(itemStack2, itemStack);
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

	public void setEffects(int primaryEffectId, int secondaryEffectId, int tertiaryEffectId) {
		if (paymentSlot.hasStack()) {
			propertyDelegate.set(1, primaryEffectId);
			propertyDelegate.set(2, secondaryEffectId);
			propertyDelegate.set(3, tertiaryEffectId);
			paymentSlot.takeStack(1);
		}
	}

	@Environment(EnvType.CLIENT)
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

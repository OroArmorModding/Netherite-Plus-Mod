package com.oroarmor.netherite_plus.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NetheriteAnvilScreen extends ItemCombinerScreen<NetheriteAnvilScreenHandler> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
	private EditBox nameField;

	public NetheriteAnvilScreen(NetheriteAnvilScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title, TEXTURE);
		titleLabelX = 60;
	}

	@Override
	protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
		RenderSystem.disableBlend();
		super.renderLabels(matrices, mouseX, mouseY);
		int level = menu.getLevelCost();
		if (level > 0) {
			int color = 8453920;
			boolean bl = true;
			String string = I18n.get("container.repair.cost", level);
			if (level >= 40 && !minecraft.player.abilities.instabuild) {
				string = I18n.get("container.repair.expensive");
				color = 16736352;
			} else if (!menu.getSlot(2).hasItem()) {
				bl = false;
			} else if (!menu.getSlot(2).mayPickup(inventory.player)) {
				color = 16736352;
			}

			if (bl) {
				int k = imageWidth - 8 - font.width(string) - 2;
				fill(matrices, k - 2, 67, imageWidth - 8, 79, 1325400064);
				font.drawShadow(matrices, string, k, 69.0F, color);
			}
		}

	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) {
			minecraft.player.closeContainer();
		}

		return nameField.keyPressed(keyCode, scanCode, modifiers) || nameField.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
	}

	private void onRenamed(String name) {
		if (!name.isEmpty()) {
			String string = name;
			Slot slot = menu.getSlot(0);
			if (slot != null && slot.hasItem() && !slot.getItem().hasCustomHoverName() && name.equals(slot.getItem().getHoverName().getString())) {
				string = "";
			}

			menu.setNewItemName(string);
			minecraft.player.connection.send(new ServerboundRenameItemPacket(string));
		}
	}

	@Override
	public void slotChanged(AbstractContainerMenu handler, int slotId, ItemStack stack) {
		if (slotId == 0) {
			nameField.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
			nameField.setEditable(!stack.isEmpty());
			setFocused(nameField);
		}

	}

	@Override
	public void removed() {
		super.removed();
		minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void renderFg(PoseStack matrixStack, int mouseY, int i, float f) {
		nameField.render(matrixStack, mouseY, i, f);
	}

	@Override
	public void resize(Minecraft client, int width, int height) {
		String string = nameField.getValue();
		this.init(client, width, height);
		nameField.setValue(string);
	}

	@Override
	protected void subInit() {
		minecraft.keyboardHandler.setSendRepeatsToGui(true);
		int i = (width - imageWidth) / 2;
		int j = (height - imageHeight) / 2;
		nameField = new EditBox(font, i + 62, j + 24, 103, 12, new TranslatableComponent("container.repair"));
		nameField.setCanLoseFocus(false);
		nameField.setTextColor(-1);
		nameField.setTextColorUneditable(-1);
		nameField.setBordered(false);
		nameField.setMaxLength(35);
		nameField.setResponder(this::onRenamed);
		children.add(nameField);
		setInitialFocus(nameField);
	}
}

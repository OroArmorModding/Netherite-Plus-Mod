package com.oroarmor.netherite_plus.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class NetheriteAnvilScreen extends ForgingScreen<NetheriteAnvilScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/anvil.png");
	private TextFieldWidget nameField;

	public NetheriteAnvilScreen(NetheriteAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, TEXTURE);
		titleX = 60;
	}

	@Override
	protected void setup() {
		client.keyboard.enableRepeatEvents(true);
		int i = (width - backgroundWidth) / 2;
		int j = (height - backgroundHeight) / 2;
		nameField = new TextFieldWidget(textRenderer, i + 62, j + 24, 103, 12,
				new TranslatableText("container.repair"));
		nameField.setFocusUnlocked(false);
		nameField.setEditableColor(-1);
		nameField.setUneditableColor(-1);
		nameField.setHasBorder(false);
		nameField.setMaxLength(35);
		nameField.setChangedListener(this::onRenamed);
		children.add(nameField);
		setInitialFocus(nameField);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		String string = nameField.getText();
		this.init(client, width, height);
		nameField.setText(string);
	}

	@Override
	public void removed() {
		super.removed();
		client.keyboard.enableRepeatEvents(false);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) {
			client.player.closeHandledScreen();
		}

		return !nameField.keyPressed(keyCode, scanCode, modifiers) && !nameField.isActive()
				? super.keyPressed(keyCode, scanCode, modifiers)
				: true;
	}

	private void onRenamed(String name) {
		if (!name.isEmpty()) {
			String string = name;
			Slot slot = handler.getSlot(0);
			if (slot != null && slot.hasStack() && !slot.getStack().hasCustomName()
					&& name.equals(slot.getStack().getName().getString())) {
				string = "";
			}

			handler.setNewItemName(string);
			client.player.networkHandler.sendPacket(new RenameItemC2SPacket(string));
		}
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		RenderSystem.disableBlend();
		super.drawForeground(matrices, mouseX, mouseY);
		int level = handler.getLevelCost();
		if (level > 0) {
			int color = 8453920;
			boolean bl = true;
			String string = I18n.translate("container.repair.cost", level);
			if (level >= 40 && !client.player.abilities.creativeMode) {
				string = I18n.translate("container.repair.expensive");
				color = 16736352;
			} else if (!handler.getSlot(2).hasStack()) {
				bl = false;
			} else if (!handler.getSlot(2).canTakeItems(playerInventory.player)) {
				color = 16736352;
			}

			if (bl) {
				int k = backgroundWidth - 8 - textRenderer.getWidth(string) - 2;
				fill(matrices, k - 2, 67, backgroundWidth - 8, 79, 1325400064);
				textRenderer.drawWithShadow(matrices, string, k, 69.0F, color);
			}
		}

	}

	@Override
	public void renderForeground(MatrixStack matrixStack, int mouseY, int i, float f) {
		nameField.render(matrixStack, mouseY, i, f);
	}

	@Override
	public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
		if (slotId == 0) {
			nameField.setText(stack.isEmpty() ? "" : stack.getName().getString());
			nameField.setEditable(!stack.isEmpty());
			setFocused(nameField);
		}

	}
}

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

package com.oroarmor.netherite_plus.client.gui.screen;

import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ItemRenameC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NetheriteAnvilScreen extends ForgingScreen<NetheriteAnvilScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/anvil.png");
    private static final Text TOO_EXPENSIVE_TEXT = Text.translatable("container.repair.expensive");
    private final PlayerEntity player;
    private TextFieldWidget nameField;

    public NetheriteAnvilScreen(NetheriteAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, TEXTURE);
        this.player = inventory.player;
        this.titleX = 60;
    }

    public void handledScreenTick() {
        super.handledScreenTick();
        this.nameField.tick();
    }

    @Override
    protected void drawForeground(GuiGraphics graphics, int mouseX, int mouseY) {
        super.drawForeground(graphics, mouseX, mouseY);
        int level = handler.getLevelCost();
        if (level > 0) {
            int color = 0x80ff20;
            Text text;
            if (level >= 40 && !player.getAbilities().creativeMode) {
                text = TOO_EXPENSIVE_TEXT;
                color = 0xff6060;
            } else if (!handler.getSlot(2).hasStack()) {
                text = null;
            } else {
                text = Text.translatable("container.repair.cost", level);
                if (!handler.getSlot(2).canTakeItems(player)) {
                    color = 0xff6060;
                }
            }

            if (text != null) {
                int k = backgroundWidth - 8 - textRenderer.getWidth(text) - 2;
                graphics.fill(k - 2, 67, backgroundWidth - 8, 79, 0x4f000000);
                graphics.drawShadowedText(textRenderer, text, k, 69, color);
            }
        }

    }

    protected void drawBackground(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        super.drawBackground(graphics, delta, mouseX, mouseY);
        graphics.drawTexture(TEXTURE, this.x + 59, this.y + 20, 0, this.backgroundHeight + (this.handler.getSlot(0).hasStack() ? 0 : 16), 110, 16);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.client.player.closeHandledScreen();
        }

        return this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.isActive() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onRenamed(String name) {
        if (!name.isEmpty()) {
            String s = name;
            Slot slot = this.handler.getSlot(0);
            if (slot != null && slot.hasStack() && !slot.getStack().hasCustomName() && name.equals(slot.getStack().getName().getString())) {
                s = "";
            }

            this.handler.setNewItemName(s);
            this.client.player.networkHandler.sendPacket(new ItemRenameC2SPacket(s));
        }
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
        if (slotId == 0) {
            nameField.setText(stack.isEmpty() ? "" : stack.getName().getString());
            nameField.setEditable(!stack.isEmpty());
            setFocusedChild(nameField);
        }

    }

    @Override
    public void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.nameField.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.nameField.getText();
        this.init(client, width, height);
        this.nameField.setText(string);
    }

    @Override
    protected void renderIcon(GuiGraphics graphics, int i, int j) {
        if ((this.handler.getSlot(0).hasStack() || this.handler.getSlot(1).hasStack())
            && !this.handler.getSlot(this.handler.getResultSlotIndex()).hasStack()) {
            graphics.drawTexture(TEXTURE, i + 99, j + 45, this.backgroundWidth, 0, 28, 21);
        }
    }

    @Override
    protected void setup() {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.nameField = new TextFieldWidget(this.textRenderer, i + 62, j + 24, 103, 12, Text.translatable("container.repair"));
        this.nameField.setFocusUnlocked(false);
        this.nameField.setEditableColor(-1);
        this.nameField.setUneditableColor(-1);
        this.nameField.setDrawsBackground(false);
        this.nameField.setMaxLength(50);
        this.nameField.setChangedListener(this::onRenamed);
        this.nameField.setText("");
        this.addSelectableChild(this.nameField);
        this.setInitialFocus(this.nameField);
        this.nameField.setEditable(false);
    }
}

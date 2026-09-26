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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NetheriteAnvilScreen extends ItemCombinerScreen<NetheriteAnvilScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
    private static final Component TOO_EXPENSIVE_TEXT = Component.translatable("container.repair.expensive");
    private final Player player;
    private EditBox nameField;

    public NetheriteAnvilScreen(NetheriteAnvilScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, TEXTURE);
        this.player = inventory.player;
        this.titleLabelX = 60;
    }

    public void containerTick() {
        super.containerTick();
        this.nameField.tick();
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        int level = menu.getLevelCost();
        if (level > 0) {
            int color = 0x80ff20;
            Component text;
            if (level >= 40 && !player.getAbilities().instabuild) {
                text = TOO_EXPENSIVE_TEXT;
                color = 0xff6060;
            } else if (!menu.getSlot(2).hasItem()) {
                text = null;
            } else {
                text = Component.translatable("container.repair.cost", level);
                if (!menu.getSlot(2).mayPickup(player)) {
                    color = 0xff6060;
                }
            }

            if (text != null) {
                int k = imageWidth - 8 - font.width(text) - 2;
                graphics.fill(k - 2, 67, imageWidth - 8, 79, 0x4f000000);
                graphics.drawString(font, text, k, 69, color);
            }
        }

    }

    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        super.renderBg(graphics, delta, mouseX, mouseY);
        graphics.blit(TEXTURE, this.leftPos + 59, this.topPos + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onRenamed(String name) {
        if (!name.isEmpty()) {
            String s = name;
            Slot slot = this.menu.getSlot(0);
            if (slot != null && slot.hasItem() && !slot.getItem().hasCustomHoverName() && name.equals(slot.getItem().getHoverName().getString())) {
                s = "";
            }

            this.menu.setNewItemName(s);
            this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
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
    public void renderFg(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.nameField.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void resize(Minecraft client, int width, int height) {
        String string = this.nameField.getValue();
        this.init(client, width, height);
        this.nameField.setValue(string);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics graphics, int i, int j) {
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem())
            && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            graphics.blit(TEXTURE, i + 99, j + 45, this.imageWidth, 0, 28, 21);
        }
    }

    @Override
    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.nameField = new EditBox(this.font, i + 62, j + 24, 103, 12, Component.translatable("container.repair"));
        this.nameField.setCanLoseFocus(false);
        this.nameField.setTextColor(-1);
        this.nameField.setTextColorUneditable(-1);
        this.nameField.setBordered(false);
        this.nameField.setMaxLength(50);
        this.nameField.setResponder(this::onRenamed);
        this.nameField.setValue("");
        this.addWidget(this.nameField);
        this.setInitialFocus(this.nameField);
        this.nameField.setEditable(false);
    }
}

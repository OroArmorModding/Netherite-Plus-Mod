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

package com.oroarmor.netherite_plus.client.gui.screens.inventory;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.oroarmor.netherite_plus.network.protocol.game.ServerboundSetNetheriteBeacon;
import com.oroarmor.netherite_plus.world.inventory.NetheriteBeaconMenu;
import com.oroarmor.netherite_plus.world.level.block.entity.NetheriteBeaconBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NetheriteBeaconScreen extends AbstractContainerScreen<NetheriteBeaconMenu> {
    private static final Identifier BEACON_LOCATION = id("textures/gui/container/netherite_beacon.png");
    private static final Identifier BUTTON_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_disabled");
    private static final Identifier BUTTON_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_selected");
    private static final Identifier BUTTON_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_highlighted");
    private static final Identifier BUTTON_SPRITE = Identifier.withDefaultNamespace("container/beacon/button");
    private static final Identifier CONFIRM_SPRITE = Identifier.withDefaultNamespace("container/beacon/confirm");
    private static final Identifier CANCEL_SPRITE = Identifier.withDefaultNamespace("container/beacon/cancel");
    private static final Component PRIMARY_TEXT = Component.translatable("block.minecraft.beacon.primary");
    private static final Component SECONDARY_TEXT = Component.translatable("block.minecraft.beacon.secondary");
    private static final Component TERTIARY_TEXT = Component.translatable("block.netherite_plus.netherite_beacon.tertiary");
    private final List<BeaconButton> beaconButtons = Lists.newArrayList();
    private @Nullable Holder<MobEffect> primaryEffect;
    private @Nullable Holder<MobEffect> secondaryEffect;
    private @Nullable Holder<MobEffect> tertiaryEffect;

    public NetheriteBeaconScreen(final NetheriteBeaconMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title, 230, 219);
        handler.addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu _handler, int slotId, ItemStack stack) {
            }

            @Override
            public void dataChanged(AbstractContainerMenu _handler, int property, int value) {
                primaryEffect = handler.getPrimaryEffect();
                secondaryEffect = handler.getSecondaryEffect();
                tertiaryEffect = handler.getTertiaryEffect();
            }
        });
    }

    private <T extends AbstractWidget & BeaconButton> void addButton(T button) {
        this.addRenderableWidget(button);
        this.beaconButtons.add(button);
    }

    @Override
    protected void init() {
        super.init();
        this.beaconButtons.clear();

        this.addButton(new BeaconConfirmButton(leftPos + 164, topPos + 107));
        this.addButton(new BeaconCancelButton(leftPos + 190, topPos + 107));

        for (int mainEffectIndex = 0; mainEffectIndex <= 2; ++mainEffectIndex) {
            int levelEffectCount = NetheriteBeaconBlockEntity.BEACON_EFFECTS.get(mainEffectIndex).size();
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount; ++levelEffectIndex) {
                Holder<MobEffect> effect = NetheriteBeaconBlockEntity.BEACON_EFFECTS.get(mainEffectIndex).get(levelEffectIndex);
                BeaconPowerButton widget = new BeaconPowerButton(
                        this.leftPos + 76 + levelEffectIndex * 24 - spacing / 2, this.topPos + 22 + mainEffectIndex * 25, effect, 0, mainEffectIndex
                );
                widget.active = false;
                this.addButton(widget);
            }
        }

        int additionalEffectsStartIndex = 3;
        for (int additionalEffectIndex = additionalEffectsStartIndex; additionalEffectIndex < NetheriteBeaconBlockEntity.BEACON_EFFECTS.size(); additionalEffectIndex++) {
            int levelEffectCount = NetheriteBeaconBlockEntity.BEACON_EFFECTS.get(additionalEffectIndex).size() + 1;
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount - 1; ++levelEffectIndex) {
                Holder<MobEffect> effect = NetheriteBeaconBlockEntity.BEACON_EFFECTS.get(additionalEffectIndex).get(levelEffectIndex);
                BeaconPowerButton widget = new BeaconPowerButton(
                        this.leftPos + 175 + levelEffectIndex * 24 - spacing / 2, this.topPos + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, effect, (additionalEffectIndex - additionalEffectsStartIndex) + 1, additionalEffectIndex
                );
                widget.active = false;
                this.addButton(widget);
            }

            BeaconPowerButton widget = new BeaconUpgradePowerButton(
                    this.leftPos + 175 + (levelEffectCount - 1) * 24 - spacing / 2, this.topPos + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, NetheriteBeaconBlockEntity.BEACON_EFFECTS.getFirst().getFirst(), (additionalEffectIndex - additionalEffectsStartIndex) + 1, additionalEffectIndex
            );
            widget.visible = false;
            this.addButton(widget);
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.updateButtons();
    }

    void updateButtons() {
        int levels = this.menu.getLevels();
        this.beaconButtons.forEach(button -> button.updateStatus(levels));
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.centeredText(this.font, PRIMARY_TEXT, 62, 10, CommonColors.TEXT_GRAY);
        graphics.centeredText(this.font, SECONDARY_TEXT, 169, 10, CommonColors.TEXT_GRAY);
        graphics.centeredText(this.font, TERTIARY_TEXT, 169, 58, CommonColors.TEXT_GRAY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        int xo = (width - imageWidth) / 2;
        int yo = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BEACON_LOCATION, xo, yo, 0f, 0f, imageWidth, imageHeight, 256, 256);
        graphics.item(new ItemStack(Items.NETHERITE_INGOT), xo + 42 + 66, yo + 109);
    }

    @Environment(EnvType.CLIENT)
    interface BeaconButton {
        void updateStatus(int level);
    }

    @Environment(EnvType.CLIENT)
    private abstract static class BeaconScreenButton extends AbstractButton implements BeaconButton {
        private boolean selected;

        protected BeaconScreenButton(final int x, final int y) {
            super(x, y, 22, 22, CommonComponents.EMPTY);
        }

        protected BeaconScreenButton(final int x, final int y, final Component component) {
            super(x, y, 22, 22, component);
        }

        @Override
        public void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
            Identifier sprite;
            if (!this.active) {
                sprite = BUTTON_DISABLED_SPRITE;
            } else if (this.selected) {
                sprite = BUTTON_SELECTED_SPRITE;
            } else if (this.isHoveredOrFocused()) {
                sprite = BUTTON_HIGHLIGHTED_SPRITE;
            } else {
                sprite = BUTTON_SPRITE;
            }

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX(), this.getY(), this.width, this.height);
            this.extractIcon(graphics);
        }

        protected abstract void extractIcon(final GuiGraphicsExtractor graphics);

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(final boolean selected) {
            this.selected = selected;
        }

        @Override
        public void updateWidgetNarration(final NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }

    @Environment(EnvType.CLIENT)
    private abstract static class BeaconSpriteScreenButton extends BeaconScreenButton {
        private final Identifier sprite;

        protected BeaconSpriteScreenButton(final int x, final int y, final Identifier sprite, final Component label) {
            super(x, y, label);
            this.setTooltip(Tooltip.create(label));
            this.sprite = sprite;
        }

        @Override
        protected void extractIcon(final GuiGraphicsExtractor graphics) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, this.getX() + 2, this.getY() + 2, 18, 18);
        }
    }

    @Environment(EnvType.CLIENT)
    private class BeaconCancelButton extends BeaconSpriteScreenButton {
        public BeaconCancelButton(final int x, final int y) {
            super(x, y, CANCEL_SPRITE, CommonComponents.GUI_CANCEL);
        }

        @Override
        public void onPress(final InputWithModifiers input) {
            //noinspection DataFlowIssue
            NetheriteBeaconScreen.this.minecraft.player.closeContainer();
        }

        @Override
        public void updateStatus(final int levels) {
        }
    }

    @Environment(EnvType.CLIENT)
    class BeaconConfirmButton extends BeaconSpriteScreenButton {
        public BeaconConfirmButton(int x, int y) {
            super(x, y, CONFIRM_SPRITE, CommonComponents.GUI_DONE);
        }

        @Override
        public void onPress(final InputWithModifiers input) {
            ClientPlayNetworking.send(
                    new ServerboundSetNetheriteBeacon(
                            Optional.ofNullable(primaryEffect),
                            Optional.ofNullable(secondaryEffect),
                            Optional.ofNullable(tertiaryEffect)
                    ));
            //noinspection DataFlowIssue
            NetheriteBeaconScreen.this.minecraft.player.closeContainer();
        }

        @Override
        public void updateStatus(int level) {
            this.active = NetheriteBeaconScreen.this.menu.hasPayment() && NetheriteBeaconScreen.this.primaryEffect != null;
        }
    }

    @Environment(EnvType.CLIENT)
    class BeaconPowerButton extends BeaconScreenButton {
        protected final int effectIndex;
        private final int level;
        private Holder<MobEffect> effect;
        private Identifier sprite;

        public BeaconPowerButton(int x, int y, Holder<MobEffect> effect, int effectIndex, int level) {
            super(x, y);
            this.effectIndex = effectIndex;
            this.level = level;
            this.setEffect(effect);
        }

        protected void setEffect(Holder<MobEffect> effect) {
            this.effect = effect;
            this.sprite = Hud.getMobEffectSprite(effect);
            this.setTooltip(Tooltip.create(this.createEffectDescription(effect), null));
        }

        protected MutableComponent createEffectDescription(final Holder<MobEffect> effect) {
            return Component.translatable(effect.value().getDescriptionId());
        }

        public void onPress(final InputWithModifiers input) {
            if (!this.isSelected()) {
                switch (this.effectIndex) {
                    case 0 -> NetheriteBeaconScreen.this.primaryEffect = this.effect;
                    case 1 -> NetheriteBeaconScreen.this.secondaryEffect = this.effect;
                    case 2 -> NetheriteBeaconScreen.this.tertiaryEffect = this.effect;
                    default -> throw new RuntimeException("Unknown Netherite Beacon effect index");
                }

                NetheriteBeaconScreen.this.updateButtons();
            }
        }

        @Override
        protected void extractIcon(final GuiGraphicsExtractor graphics) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, this.getX() + 2, this.getY() + 2, 18, 18);
        }

        @Override
        public void updateStatus(int level) {
            this.active = this.level < level;

            boolean shouldSelect = this.effect == switch (this.effectIndex) {
                case 0 -> NetheriteBeaconScreen.this.primaryEffect;
                case 1 -> NetheriteBeaconScreen.this.secondaryEffect;
                case 2 -> NetheriteBeaconScreen.this.tertiaryEffect;
                default -> throw new RuntimeException("Unknown Netherite Beacon effect index");
            };

            if (this.isSelected() && !shouldSelect) {
                if (this.effectIndex == 0 && this.effect == NetheriteBeaconScreen.this.secondaryEffect) {
                    NetheriteBeaconScreen.this.secondaryEffect = null;
                }

                if (this.effectIndex < 2 && this.effect == NetheriteBeaconScreen.this.tertiaryEffect) {
                    NetheriteBeaconScreen.this.tertiaryEffect = null;
                }
            }

            this.setSelected(shouldSelect);
        }

        @Override
        protected MutableComponent createNarrationMessage() {
            return this.createEffectDescription(this.effect);
        }
    }

    @Environment(EnvType.CLIENT)
    class BeaconUpgradePowerButton extends BeaconPowerButton {
        public BeaconUpgradePowerButton(int i, int j, Holder<MobEffect> effect, int effectIndex, int level) {
            super(i, j, effect, effectIndex, level);
        }

        @Override
        protected MutableComponent createEffectDescription(Holder<MobEffect> effect) {
            if (effect.is(MobEffects.REGENERATION.unwrapKey().orElseThrow())) {
                return Component.translatable(effect.value().getDescriptionId()).append(" " + "I".repeat(this.effectIndex));
            }
            return Component.translatable(effect.value().getDescriptionId()).append(" " + "I".repeat(this.effectIndex + 1));
        }

        @Override
        public void updateStatus(int level) {
            if (NetheriteBeaconScreen.this.primaryEffect != null) {
                this.visible = true;
                if (this.effectIndex == 1 || NetheriteBeaconScreen.this.secondaryEffect == null) {
                    this.setEffect(NetheriteBeaconScreen.this.primaryEffect);
                } else if (this.effectIndex == 2) {
                    this.setEffect(NetheriteBeaconScreen.this.secondaryEffect);
                }
                super.updateStatus(level);
            } else {
                this.visible = false;
            }
        }
    }
}

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

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class NetheriteBeaconScreen extends AbstractContainerScreen<NetheriteBeaconScreenHandler> {
    public static final MobEffect[][] EFFECTS_BY_LEVEL = new MobEffect[][]{
            {MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED}, {MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP}, {MobEffects.DAMAGE_BOOST}, {MobEffects.REGENERATION}, {MobEffects.GLOWING}
    };
    private static final ResourceLocation TEXTURE = id("textures/gui/container/netherite_beacon.png");
    private static final Component PRIMARY_TEXT = Component.translatable("block.minecraft.beacon.primary");
    private static final Component SECONDARY_TEXT = Component.translatable("block.minecraft.beacon.secondary");
    private static final Component TERTIARY_TEXT = Component.translatable("block.netherite_plus.netherite_beacon.tertiary");
    private final List<BeaconButtonWidget> buttons = Lists.newArrayList();
    private MobEffect primaryEffect;
    private MobEffect secondaryEffect;
    private MobEffect tertiaryEffect;

    public NetheriteBeaconScreen(final NetheriteBeaconScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        imageWidth = 230;
        imageHeight = 219;
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

    private <T extends AbstractWidget & BeaconButtonWidget> void addButton(T button) {
        this.addRenderableWidget(button);
        this.buttons.add(button);
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();

        this.addButton(new DoneButtonWidget(leftPos + 164, topPos + 107));
        this.addButton(new CancelButtonWidget(leftPos + 190, topPos + 107));

        for (int mainEffectIndex = 0; mainEffectIndex <= 2; ++mainEffectIndex) {
            int levelEffectCount = EFFECTS_BY_LEVEL[mainEffectIndex].length;
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount; ++levelEffectIndex) {
                MobEffect effect = EFFECTS_BY_LEVEL[mainEffectIndex][levelEffectIndex];
                EffectButtonWidget widget = new EffectButtonWidget(
                        this.leftPos + 76 + levelEffectIndex * 24 - spacing / 2, this.topPos + 22 + mainEffectIndex * 25, effect, 0, mainEffectIndex
                );
                widget.active = false;
                this.addButton(widget);
            }
        }

        int additionalEffectsStartIndex = 3;
        for (int additionalEffectIndex = additionalEffectsStartIndex; additionalEffectIndex < EFFECTS_BY_LEVEL.length; additionalEffectIndex++) {
            int levelEffectCount = EFFECTS_BY_LEVEL[additionalEffectIndex].length + 1;
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount - 1; ++levelEffectIndex) {
                MobEffect effect = EFFECTS_BY_LEVEL[additionalEffectIndex][levelEffectIndex];
                EffectButtonWidget widget = new EffectButtonWidget(
                        this.leftPos + 175 + levelEffectIndex * 24 - spacing / 2, this.topPos + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, effect, (additionalEffectIndex - additionalEffectsStartIndex) + 1, 3
                );
                widget.active = false;
                this.addButton(widget);
            }

            EffectButtonWidget widget = new AdditionalEffectButtonWidget(
                    this.leftPos + 175 + (levelEffectCount - 1) * 24 - spacing / 2, this.topPos + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, (additionalEffectIndex - additionalEffectsStartIndex) + 1, EFFECTS_BY_LEVEL[0][0]
            );
            widget.visible = false;
            this.addButton(widget);
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.tickButtons();
    }

    void tickButtons() {
        int i = this.menu.getProperties();
        this.buttons.forEach(button -> button.tick(i));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawCenteredString(this.font, PRIMARY_TEXT, 62, 10, 14737632);
        graphics.drawCenteredString(this.font, SECONDARY_TEXT, 169, 10, 14737632);
        graphics.drawCenteredString(this.font, TERTIARY_TEXT, 169, 58, 14737632);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (width - imageWidth) / 2;
        int j = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, i, j, 0, 0, imageWidth, imageHeight);
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 100.0F);
        graphics.renderItem(new ItemStack(Items.NETHERITE_INGOT), i + 42 + 66, j + 109);
        graphics.pose().popPose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    interface BeaconButtonWidget {
        void tick(int level);
    }

    @Environment(EnvType.CLIENT)
    abstract static class IconButtonWidget extends BaseButtonWidget {
        private final int u;
        private final int v;

        protected IconButtonWidget(int x, int y, int u, int v, Component text) {
            super(x, y, text);
            this.u = u;
            this.v = v;
        }

        @Override
        protected void renderExtra(GuiGraphics graphics) {
            graphics.blit(TEXTURE, getX() + 2, getY() + 2, u, v, 18, 18);
        }
    }

    @Environment(EnvType.CLIENT)
    abstract static class BaseButtonWidget extends AbstractButton implements BeaconButtonWidget {
        private boolean disabled;

        protected BaseButtonWidget(int x, int y) {
            super(x, y, 22, 22, CommonComponents.EMPTY);
        }

        protected BaseButtonWidget(int x, int y, Component text) {
            super(x, y, 22, 22, text);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderTexture(0, NetheriteBeaconScreen.TEXTURE);
            int uStart = 0;
            if (!active) {
                uStart += width * 2;
            } else if (disabled) {
                uStart += width;
            } else if (isHoveredOrFocused()) {
                uStart += width * 3;
            }

            graphics.blit(TEXTURE, this.getX(), this.getY(), uStart, 219, width, height);
            this.renderExtra(graphics);
        }

        protected abstract void renderExtra(GuiGraphics graphics);

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput builder) {
            this.defaultButtonNarrationText(builder);
        }
    }

    @Environment(EnvType.CLIENT)
    class CancelButtonWidget extends IconButtonWidget {
        public CancelButtonWidget(int x, int y) {
            super(x, y, 112, 220, CommonComponents.GUI_CANCEL);
        }

        @Override
        public void onPress() {
            NetheriteBeaconScreen.this.minecraft.player.closeContainer();
        }

        @Override
        public void tick(int level) {
        }
    }

    @Environment(EnvType.CLIENT)
    class DoneButtonWidget extends IconButtonWidget {
        public DoneButtonWidget(int x, int y) {
            super(x, y, 90, 220, CommonComponents.GUI_DONE);
        }

        @Override
        public void onPress() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            new UpdateNetheriteBeaconC2SPacket(Optional.ofNullable(primaryEffect),
                    Optional.ofNullable(secondaryEffect),
                    Optional.ofNullable(tertiaryEffect))
                    .write(buf);
            ClientPlayNetworking.send(UpdateNetheriteBeaconC2SPacket.ID, buf);
            NetheriteBeaconScreen.this.minecraft.player.closeContainer();
        }

        @Override
        public void tick(int level) {
            this.active = NetheriteBeaconScreen.this.menu.hasPayment() && NetheriteBeaconScreen.this.primaryEffect != null;
        }
    }

    @Environment(EnvType.CLIENT)
    class EffectButtonWidget extends BaseButtonWidget {
        protected final int effectIndex;
        private final int level;
        private MobEffect effect;
        private TextureAtlasSprite sprite;

        public EffectButtonWidget(int x, int y, MobEffect statusEffect, int effectIndex, int level) {
            super(x, y);
            this.effectIndex = effectIndex;
            this.level = level;
            this.init(statusEffect);
        }

        protected void init(MobEffect statusEffect) {
            this.effect = statusEffect;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(statusEffect);
        }

        protected MutableComponent getEffectName(MobEffect statusEffect) {
            return Component.translatable(statusEffect.getDescriptionId());
        }

        public void onPress() {
            if (!this.isDisabled()) {
                switch (this.effectIndex) {
                    case 0 -> NetheriteBeaconScreen.this.primaryEffect = this.effect;
                    case 1 -> NetheriteBeaconScreen.this.secondaryEffect = this.effect;
                    case 2 -> NetheriteBeaconScreen.this.tertiaryEffect = this.effect;
                    default -> throw new RuntimeException("Unknown Netherite Beacon effect index");
                }

                NetheriteBeaconScreen.this.tickButtons();
            }
        }

        @Override
        protected void renderExtra(GuiGraphics graphics) {
            RenderSystem.setShaderTexture(0, this.sprite.atlasLocation());
            graphics.blit(getX() + 2, getY() + 2, 0, 18, 18, sprite);
        }

        @Override
        public void tick(int level) {
            this.active = this.level < level;
            this.setDisabled(this.effect == switch (this.effectIndex) {
                case 0 -> NetheriteBeaconScreen.this.primaryEffect;
                case 1 -> NetheriteBeaconScreen.this.secondaryEffect;
                case 2 -> NetheriteBeaconScreen.this.tertiaryEffect;
                default -> throw new RuntimeException("Unknown Netherite Beacon effect index");
            });
        }

        protected MutableComponent createNarrationMessage() {
            return this.getEffectName(this.effect);
        }
    }

    @Environment(EnvType.CLIENT)
    class AdditionalEffectButtonWidget extends EffectButtonWidget {
        public AdditionalEffectButtonWidget(int i, int j, int effectIndex, MobEffect statusEffect) {
            super(i, j, statusEffect, effectIndex, 3);
        }

        @Override
        protected MutableComponent getEffectName(MobEffect statusEffect) {
            return Component.translatable(statusEffect.getDescriptionId()).append(" " + "I".repeat(this.effectIndex + 1));
        }

        @Override
        public void tick(int level) {
            if (NetheriteBeaconScreen.this.primaryEffect != null) {
                this.visible = true;
                this.init(this.effectIndex == 1 || NetheriteBeaconScreen.this.secondaryEffect == null ? NetheriteBeaconScreen.this.primaryEffect : NetheriteBeaconScreen.this.secondaryEffect);
                super.tick(level);
            } else {
                this.visible = false;
            }
        }
    }
}

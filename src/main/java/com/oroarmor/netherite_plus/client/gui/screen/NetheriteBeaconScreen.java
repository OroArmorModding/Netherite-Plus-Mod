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

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import io.netty.buffer.Unpooled;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheriteBeaconScreen extends HandledScreen<NetheriteBeaconScreenHandler> {
    private static final Identifier TEXTURE = id("textures/gui/container/netherite_beacon.png");
    private static final Text PRIMARY_TEXT = Text.translatable("block.minecraft.beacon.primary");
    private static final Text SECONDARY_TEXT = Text.translatable("block.minecraft.beacon.secondary");
    private static final Text TERTIARY_TEXT = Text.translatable("block.netherite_plus.netherite_beacon.tertiary");

    public static final StatusEffect[][] EFFECTS_BY_LEVEL = new StatusEffect[][]{
            {StatusEffects.SPEED, StatusEffects.HASTE}, {StatusEffects.RESISTANCE, StatusEffects.JUMP_BOOST}, {StatusEffects.STRENGTH}, {StatusEffects.REGENERATION}, {StatusEffects.GLOWING}
    };

    private StatusEffect primaryEffect;
    private StatusEffect secondaryEffect;
    private StatusEffect tertiaryEffect;

    private final List<BeaconButtonWidget> buttons = Lists.newArrayList();

    public NetheriteBeaconScreen(final NetheriteBeaconScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 230;
        backgroundHeight = 219;
        handler.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler _handler, int slotId, ItemStack stack) {
            }

            @Override
            public void onPropertyUpdate(ScreenHandler _handler, int property, int value) {
                primaryEffect = handler.getPrimaryEffect();
                secondaryEffect = handler.getSecondaryEffect();
                tertiaryEffect = handler.getTertiaryEffect();
            }
        });
    }

    private <T extends ClickableWidget & BeaconButtonWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add(button);
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();

        this.addButton(new DoneButtonWidget(x + 164, y + 107));
        this.addButton(new CancelButtonWidget(x + 190, y + 107));

        for (int mainEffectIndex = 0; mainEffectIndex <= 2; ++mainEffectIndex) {
            int levelEffectCount = EFFECTS_BY_LEVEL[mainEffectIndex].length;
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount; ++levelEffectIndex) {
                StatusEffect effect = EFFECTS_BY_LEVEL[mainEffectIndex][levelEffectIndex];
                EffectButtonWidget widget = new EffectButtonWidget(
                        this.x + 76 + levelEffectIndex * 24 - spacing / 2, this.y + 22 + mainEffectIndex * 25, effect, 0, mainEffectIndex
                );
                widget.active = false;
                this.addButton(widget);
            }
        }

        int additionalEffectsStartIndex = 3;
        for (int additionalEffectIndex = additionalEffectsStartIndex; additionalEffectIndex < EFFECTS_BY_LEVEL.length; additionalEffectIndex ++) {
            int levelEffectCount = EFFECTS_BY_LEVEL[additionalEffectIndex].length + 1;
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount - 1; ++levelEffectIndex) {
                StatusEffect effect = EFFECTS_BY_LEVEL[additionalEffectIndex][levelEffectIndex];
                EffectButtonWidget widget = new EffectButtonWidget(
                        this.x + 175 + levelEffectIndex * 24 - spacing / 2, this.y + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, effect, (additionalEffectIndex - additionalEffectsStartIndex) + 1, 3
                );
                widget.active = false;
                this.addButton(widget);
            }

            EffectButtonWidget widget = new AdditionalEffectButtonWidget(
                    this.x + 175 + (levelEffectCount - 1) * 24 - spacing / 2, this.y + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, (additionalEffectIndex - additionalEffectsStartIndex) + 1, EFFECTS_BY_LEVEL[0][0]
            );
            widget.visible = false;
            this.addButton(widget);
        }
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.tickButtons();
    }

    void tickButtons() {
        int i = this.handler.getProperties();
        this.buttons.forEach(button -> button.tick(i));
    }

    @Override
    protected void drawForeground(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawCenteredShadowedText(this.textRenderer, PRIMARY_TEXT, 62, 10, 14737632);
        graphics.drawCenteredShadowedText(this.textRenderer, SECONDARY_TEXT, 169, 10, 14737632);
        graphics.drawCenteredShadowedText(this.textRenderer, TERTIARY_TEXT, 169, 58, 14737632);
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (width - backgroundWidth) / 2;
        int j = (height - backgroundHeight) / 2;
        graphics.drawTexture(TEXTURE, i, j, 0, 0, backgroundWidth, backgroundHeight);
        graphics.getMatrices().push();
        graphics.getMatrices().translate(0.0F, 0.0F, 100.0F);
        graphics.drawItem(new ItemStack(Items.NETHERITE_INGOT), i + 42 + 66, j + 109);
        graphics.getMatrices().pop();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(graphics, mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    interface BeaconButtonWidget {
        void tick(int level);
    }

    @Environment(EnvType.CLIENT)
    abstract static class IconButtonWidget extends BaseButtonWidget {
        private final int u;
        private final int v;

        protected IconButtonWidget(int x, int y, int u, int v, Text text) {
            super(x, y, text);
            this.u = u;
            this.v = v;
        }

        @Override
        protected void renderExtra(GuiGraphics graphics) {
            graphics.drawTexture(TEXTURE, getX() + 2, getY() + 2, u, v, 18, 18);
        }
    }

    @Environment(EnvType.CLIENT)
    abstract static class BaseButtonWidget extends PressableWidget implements BeaconButtonWidget {
        private boolean disabled;

        protected BaseButtonWidget(int x, int y) {
            super(x, y, 22, 22, CommonTexts.EMPTY);
        }

        protected BaseButtonWidget(int x, int y, Text text) {
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

            graphics.drawTexture(TEXTURE, this.getX(), this.getY(), uStart, 219, width, height);
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
        protected void updateNarration(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }

    @Environment(EnvType.CLIENT)
    class CancelButtonWidget extends IconButtonWidget {
        public CancelButtonWidget(int x, int y) {
            super(x, y, 112, 220, CommonTexts.CANCEL);
        }

        @Override
        public void onPress() {
            NetheriteBeaconScreen.this.client.player.closeHandledScreen();
        }

        @Override
        public void tick(int level) {
        }
    }

    @Environment(EnvType.CLIENT)
    class DoneButtonWidget extends IconButtonWidget {
        public DoneButtonWidget(int x, int y) {
            super(x, y, 90, 220, CommonTexts.DONE);
        }

        @Override
        public void onPress() {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            new UpdateNetheriteBeaconC2SPacket(Optional.ofNullable(primaryEffect),
                    Optional.ofNullable(secondaryEffect),
                    Optional.ofNullable(tertiaryEffect))
                    .write(buf);
            ClientPlayNetworking.send(UpdateNetheriteBeaconC2SPacket.ID, buf);
            NetheriteBeaconScreen.this.client.player.closeHandledScreen();
        }

        @Override
        public void tick(int level) {
            this.active = NetheriteBeaconScreen.this.handler.hasPayment() && NetheriteBeaconScreen.this.primaryEffect != null;
        }
    }

    @Environment(EnvType.CLIENT)
    class EffectButtonWidget extends BaseButtonWidget {
        protected final int effectIndex;
        private final int level;
        private StatusEffect effect;
        private Sprite sprite;

        public EffectButtonWidget(int x, int y, StatusEffect statusEffect, int effectIndex, int level) {
            super(x, y);
            this.effectIndex = effectIndex;
            this.level = level;
            this.init(statusEffect);
        }

        protected void init(StatusEffect statusEffect) {
            this.effect = statusEffect;
            this.sprite = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect);
        }

        protected MutableText getEffectName(StatusEffect statusEffect) {
            return Text.translatable(statusEffect.getTranslationKey());
        }

        public void onPress() {
            if (!this.isDisabled()) {
                switch (this.effectIndex) {
                    case 0 -> NetheriteBeaconScreen.this.primaryEffect = this.effect;
                    case 1-> NetheriteBeaconScreen.this.secondaryEffect = this.effect;
                    case 2-> NetheriteBeaconScreen.this.tertiaryEffect = this.effect;
                    default-> throw new RuntimeException("Unknown Netherite Beacon effect index");
                }

                NetheriteBeaconScreen.this.tickButtons();
            }
        }

        @Override
        protected void renderExtra(GuiGraphics graphics) {
            RenderSystem.setShaderTexture(0, this.sprite.getId());
            graphics.drawSprite(getX() + 2, getY() + 2, 0, 18, 18, sprite);
        }

        @Override
        public void tick(int level) {
            this.active = this.level < level;
            this.setDisabled(this.effect == switch (this.effectIndex) {
                case 0 -> NetheriteBeaconScreen.this.primaryEffect;
                case 1-> NetheriteBeaconScreen.this.secondaryEffect;
                case 2-> NetheriteBeaconScreen.this.tertiaryEffect;
                default-> throw new RuntimeException("Unknown Netherite Beacon effect index");
            });
        }

        protected MutableText getNarrationMessage() {
            return this.getEffectName(this.effect);
        }
    }

    @Environment(EnvType.CLIENT)
    class AdditionalEffectButtonWidget extends EffectButtonWidget {
        public AdditionalEffectButtonWidget(int i, int j, int effectIndex, StatusEffect statusEffect) {
            super(i, j, statusEffect, effectIndex, 3);
        }

        @Override
        protected MutableText getEffectName(StatusEffect statusEffect) {
            return Text.translatable(statusEffect.getTranslationKey()).append(" " + "I".repeat(this.effectIndex + 1));
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

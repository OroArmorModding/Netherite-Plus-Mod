package com.oroarmor.netherite_plus.client.gui.screen;

import java.io.IOException;
import java.util.Iterator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheriteBeaconScreen extends AbstractContainerScreen<NetheriteBeaconScreenHandler> {
    private static final ResourceLocation TEXTURE = id("textures/gui/container/netherite_beacon.png");
    private static final Component PRIMARY_TEXT = new TranslatableComponent("block.minecraft.beacon.primary");
    private static final Component SECONDARY_TEXT = new TranslatableComponent("block.minecraft.beacon.secondary");
    private static final Component TERTIARY_TEXT = new TranslatableComponent("block.netherite_plus.netherite_beacon.tertiary");
    private DoneButtonWidget doneButton;
    private boolean consumeGem;
    private MobEffect primaryEffect;
    private MobEffect secondaryEffect;
    private MobEffect tertiaryEffect;

    public NetheriteBeaconScreen(final NetheriteBeaconScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        imageWidth = 230;
        imageHeight = 219;
        handler.addSlotListener(new ContainerListener() {
            @Override
            public void refreshContainer(AbstractContainerMenu handlerx, NonNullList<ItemStack> stacks) {
            }

            @Override
            public void slotChanged(AbstractContainerMenu handlerx, int slotId, ItemStack stack) {
            }

            @Override
            public void setContainerData(AbstractContainerMenu handlerx, int property, int value) {
                primaryEffect = handler.getPrimaryEffect();
                secondaryEffect = handler.getSecondaryEffect();
                tertiaryEffect = handler.getTertiaryEffect();
                consumeGem = true;
            }
        });

    }

    @Override
    protected void init() {
        super.init();
        doneButton = this.addButton(new DoneButtonWidget(leftPos + 164, topPos + 107));
        this.addButton(new CancelButtonWidget(leftPos + 190, topPos + 107));
        consumeGem = true;
        doneButton.active = false;
    }

    @Override
    public void tick() {
        super.tick();
        int handlerProperties = menu.getProperties();
        if (consumeGem && handlerProperties >= 0) {
            consumeGem = false;

            for (int primaryEffectLevel = 0; primaryEffectLevel <= 2; ++primaryEffectLevel) {
                int levelEffects = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[primaryEffectLevel].length;
                int xLocation = levelEffects * 22 + (levelEffects - 1) * 2;

                for (int levelEffectIndex = 0; levelEffectIndex < levelEffects; ++levelEffectIndex) {
                    MobEffect level1and2Effects = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[primaryEffectLevel][levelEffectIndex];
                    EffectButtonWidget effectButtonWidget = new EffectButtonWidget(leftPos + 76 + levelEffectIndex * 24 - xLocation / 2, topPos + 22 + primaryEffectLevel * 25, level1and2Effects, 1);
                    this.addButton(effectButtonWidget);
                    if (primaryEffectLevel >= handlerProperties) {
                        effectButtonWidget.active = false;
                    } else if (level1and2Effects == primaryEffect) {
                        effectButtonWidget.setDisabled(true);
                    }
                }
            }

            // Regen and Level 2
            int levelThreeEffects = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[3].length;
            int xLocation = levelThreeEffects * 22 + levelThreeEffects * 2;
            for (int q = 0; q < levelThreeEffects; ++q) {
                MobEffect level3Effect = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[3][q];
                EffectButtonWidget effectButtonWidget = new EffectButtonWidget(leftPos + 167 + q * 24 - xLocation / 2, topPos + 22, level3Effect, 2);
                this.addButton(effectButtonWidget);
                if (1 >= handlerProperties) {
                    effectButtonWidget.active = false;
                } else if (level3Effect == secondaryEffect) {
                    effectButtonWidget.setDisabled(true);
                }
            }

            if (primaryEffect != null) {
                EffectButtonWidget effectButtonWidget = new EffectButtonWidget(leftPos + 167 + levelThreeEffects * 24 - xLocation / 2, topPos + 22, primaryEffect, 2);
                this.addButton(effectButtonWidget);
                if (1 >= handlerProperties) {
                    effectButtonWidget.active = false;
                } else if (primaryEffect == secondaryEffect) {
                    effectButtonWidget.setDisabled(true);
                }
            }

            // Level 3 and Glowing
            int levelFourEffects = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[3].length;
            for (int q = 0; q < levelFourEffects; ++q) {
                MobEffect level4Effect = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[4][q];
                EffectButtonWidget effectButtonWidget = new EffectButtonWidget(leftPos + 167 + q * 24 - xLocation / 2, topPos + 72, level4Effect, 3);
                this.addButton(effectButtonWidget);
                if (3 >= handlerProperties) {
                    effectButtonWidget.active = false;
                } else if (level4Effect == tertiaryEffect) {
                    effectButtonWidget.setDisabled(true);
                }
            }

            if (secondaryEffect != null) {
                EffectButtonWidget effectButtonWidget = new EffectButtonWidget(leftPos + 167 + levelFourEffects * 24 - xLocation / 2, topPos + 72, secondaryEffect, 3);
                this.addButton(effectButtonWidget);
                if (3 >= handlerProperties) {
                    effectButtonWidget.active = false;
                } else if (tertiaryEffect == secondaryEffect) {
                    effectButtonWidget.setDisabled(true);
                }
            }
        }

        doneButton.active = menu.hasPayment() && primaryEffect != null;
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        drawCenteredString(matrices, font, PRIMARY_TEXT, 62, 10, 14737632);
        drawCenteredString(matrices, font, SECONDARY_TEXT, 169, 10, 14737632);
        drawCenteredString(matrices, font, TERTIARY_TEXT, 169, 58, 14737632);
        Iterator<AbstractWidget> var4 = buttons.iterator();

        while (var4.hasNext()) {
            AbstractWidget abstractButtonWidget = var4.next();
            if (abstractButtonWidget.isHovered()) {
                abstractButtonWidget.renderToolTip(matrices, mouseX - leftPos, mouseY - topPos);
                break;
            }
        }

    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(TEXTURE);
        int i = (width - imageWidth) / 2;
        int j = (height - imageHeight) / 2;
        this.blit(matrices, i, j, 0, 0, imageWidth, imageHeight);
        itemRenderer.blitOffset = 100.0F;
        itemRenderer.renderAndDecorateItem(new ItemStack(Items.NETHERITE_INGOT), i + 42 + 66, j + 109);
        itemRenderer.blitOffset = 0.0F;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        renderTooltip(matrices, mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    class CancelButtonWidget extends NetheriteBeaconScreen.IconButtonWidget {
        public CancelButtonWidget(int x, int y) {
            super(x, y, 112, 220);
        }

        @Override
        public void onPress() {
            NetheriteBeaconScreen.this.minecraft.player.connection.send(new ServerboundContainerClosePacket(NetheriteBeaconScreen.this.minecraft.player.containerMenu.containerId));
            NetheriteBeaconScreen.this.minecraft.setScreen(null);
        }

        @Override
        public void renderToolTip(PoseStack matrices, int mouseX, int mouseY) {
            NetheriteBeaconScreen.this.renderTooltip(matrices, CommonComponents.GUI_CANCEL, mouseX, mouseY);
        }
    }

    @Environment(EnvType.CLIENT)
    class DoneButtonWidget extends NetheriteBeaconScreen.IconButtonWidget {
        public DoneButtonWidget(int x, int y) {
            super(x, y, 90, 220);
        }

        @Override
        public void onPress() {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            try {
                new UpdateNetheriteBeaconC2SPacket(MobEffect.getId(primaryEffect), MobEffect.getId(secondaryEffect), MobEffect.getId(tertiaryEffect)).write(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            NetheritePlusModPlatform.sendBeaconUpdatePacket(buf);
            NetheriteBeaconScreen.this.minecraft.player.connection.send(new ServerboundContainerClosePacket(NetheriteBeaconScreen.this.minecraft.player.containerMenu.containerId));
            NetheriteBeaconScreen.this.minecraft.setScreen(null);
        }

        @Override
        public void renderToolTip(PoseStack matrices, int mouseX, int mouseY) {
            NetheriteBeaconScreen.this.renderTooltip(matrices, CommonComponents.GUI_DONE, mouseX, mouseY);
        }
    }

    @Environment(EnvType.CLIENT)
    abstract static class IconButtonWidget extends BaseButtonWidget {
        private final int u;
        private final int v;

        protected IconButtonWidget(int x, int y, int u, int v) {
            super(x, y);
            this.u = u;
            this.v = v;
        }

        @Override
        protected void renderExtra(PoseStack matrixStack) {
            this.blit(matrixStack, x + 2, y + 2, u, v, 18, 18);
        }
    }

    @Environment(EnvType.CLIENT)
    class EffectButtonWidget extends BaseButtonWidget {
        private final MobEffect effect;
        private final TextureAtlasSprite sprite;
        private final int level;
        private final Component title;

        public EffectButtonWidget(int x, int y, MobEffect statusEffect, int level) {
            super(x, y);
            effect = statusEffect;
            sprite = Minecraft.getInstance().getMobEffectTextures().get(statusEffect);
            this.level = level;
            title = generateTooltip(statusEffect, level);
        }

        private Component generateTooltip(MobEffect statusEffect, int level) {
            MutableComponent mutableText = new TranslatableComponent(statusEffect.getDescriptionId());
            if (level == 2 && statusEffect != MobEffects.REGENERATION) {
                mutableText.append(" II");
            }

            if (level == 3 && statusEffect != MobEffects.GLOWING) {
                if (statusEffect != MobEffects.REGENERATION) {
                    mutableText.append(" III");
                } else {
                    mutableText.append(" II");
                }
            }

            return mutableText;
        }

        @Override
        public void onPress() {
            if (!isDisabled()) {
                if (level == 1) {
                    primaryEffect = effect;
                    if (secondaryEffect != MobEffects.REGENERATION) {
                        secondaryEffect = effect;
                    }
                } else if (level == 2) {
                    secondaryEffect = effect;
                } else if (level == 3) {
                    tertiaryEffect = effect;
                }

                NetheriteBeaconScreen.this.buttons.clear();
                NetheriteBeaconScreen.this.children.clear();
                NetheriteBeaconScreen.this.init();
                tick();
            }
        }

        @Override
        public void renderToolTip(PoseStack matrices, int mouseX, int mouseY) {
            NetheriteBeaconScreen.this.renderTooltip(matrices, title, mouseX, mouseY);
        }

        @Override
        protected void renderExtra(PoseStack matrixStack) {
            Minecraft.getInstance().getTextureManager().bind(sprite.atlas().location());
            blit(matrixStack, x + 2, y + 2, getBlitOffset(), 18, 18, sprite);
        }
    }

    @Environment(EnvType.CLIENT)
    abstract static class BaseButtonWidget extends AbstractButton {
        private boolean disabled;

        protected BaseButtonWidget(int x, int y) {
            super(x, y, 22, 22, TextComponent.EMPTY);
        }

        @Override
        public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
            Minecraft.getInstance().getTextureManager().bind(NetheriteBeaconScreen.TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = 0;
            if (!active) {
                j += width * 2;
            } else if (disabled) {
                j += width * 1;
            } else if (isHovered()) {
                j += width * 3;
            }

            this.blit(matrices, x, y, j, 219, width, height);
            renderExtra(matrices);
        }

        protected abstract void renderExtra(PoseStack matrixStack);

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
    }
}

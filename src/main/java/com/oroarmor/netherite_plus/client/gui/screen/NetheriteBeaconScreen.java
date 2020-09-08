package com.oroarmor.netherite_plus.client.gui.screen;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.io.IOException;
import java.util.Iterator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.GuiCloseC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class NetheriteBeaconScreen extends HandledScreen<NetheriteBeaconScreenHandler> {
	private static final Identifier TEXTURE = id("textures/gui/container/netherite_beacon.png");
	private static final Text PRIMARY_TEXT = new TranslatableText("block.minecraft.beacon.primary");
	private static final Text SECONDARY_TEXT = new TranslatableText("block.minecraft.beacon.secondary");
	private static final Text TERTIARY_TEXT = new TranslatableText("block.netherite_plus.netherite_beacon.tertiary");
	private DoneButtonWidget doneButton;
	private boolean consumeGem;
	private StatusEffect primaryEffect;
	private StatusEffect secondaryEffect;
	private StatusEffect tertiaryEffect;

	public NetheriteBeaconScreen(final NetheriteBeaconScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		backgroundWidth = 230;
		backgroundHeight = 219;
		handler.addListener(new ScreenHandlerListener() {
			@Override
			public void onHandlerRegistered(ScreenHandler handlerx, DefaultedList<ItemStack> stacks) {
			}

			@Override
			public void onSlotUpdate(ScreenHandler handlerx, int slotId, ItemStack stack) {
			}

			@Override
			public void onPropertyUpdate(ScreenHandler handlerx, int property, int value) {
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
		doneButton = this.addButton(new DoneButtonWidget(x + 164, y + 107));
		this.addButton(new CancelButtonWidget(x + 190, y + 107));
		consumeGem = true;
		doneButton.active = false;
	}

	@Override
	public void tick() {
		super.tick();
		int handlerProperties = handler.getProperties();
		if (consumeGem && handlerProperties >= 0) {
			consumeGem = false;

			for (int primaryEffectLevel = 0; primaryEffectLevel <= 2; ++primaryEffectLevel) {
				int levelEffects = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[primaryEffectLevel].length;
				int xLocation = levelEffects * 22 + (levelEffects - 1) * 2;

				for (int levelEffectIndex = 0; levelEffectIndex < levelEffects; ++levelEffectIndex) {
					StatusEffect level1and2Effects = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[primaryEffectLevel][levelEffectIndex];
					EffectButtonWidget effectButtonWidget = new EffectButtonWidget(
							x + 76 + levelEffectIndex * 24 - xLocation / 2, y + 22 + primaryEffectLevel * 25,
							level1and2Effects, 1);
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
				StatusEffect level3Effect = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[3][q];
				EffectButtonWidget effectButtonWidget = new EffectButtonWidget(x + 167 + q * 24 - xLocation / 2, y + 22,
						level3Effect, 2);
				this.addButton(effectButtonWidget);
				if (1 >= handlerProperties) {
					effectButtonWidget.active = false;
				} else if (level3Effect == secondaryEffect) {
					effectButtonWidget.setDisabled(true);
				}
			}

			if (primaryEffect != null) {
				EffectButtonWidget effectButtonWidget = new EffectButtonWidget(
						x + 167 + levelThreeEffects * 24 - xLocation / 2, y + 22, primaryEffect, 2);
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
				StatusEffect level4Effect = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[4][q];
				EffectButtonWidget effectButtonWidget = new EffectButtonWidget(x + 167 + q * 24 - xLocation / 2, y + 72,
						level4Effect, 3);
				this.addButton(effectButtonWidget);
				if (3 >= handlerProperties) {
					effectButtonWidget.active = false;
				} else if (level4Effect == tertiaryEffect) {
					effectButtonWidget.setDisabled(true);
				}
			}

			if (secondaryEffect != null) {
				EffectButtonWidget effectButtonWidget = new EffectButtonWidget(
						x + 167 + levelFourEffects * 24 - xLocation / 2, y + 72, secondaryEffect, 3);
				this.addButton(effectButtonWidget);
				if (3 >= handlerProperties) {
					effectButtonWidget.active = false;
				} else if (tertiaryEffect == secondaryEffect) {
					effectButtonWidget.setDisabled(true);
				}
			}
		}

		doneButton.active = handler.hasPayment() && primaryEffect != null;
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		drawCenteredText(matrices, textRenderer, PRIMARY_TEXT, 62, 10, 14737632);
		drawCenteredText(matrices, textRenderer, SECONDARY_TEXT, 169, 10, 14737632);
		drawCenteredText(matrices, textRenderer, TERTIARY_TEXT, 169, 58, 14737632);
		Iterator<AbstractButtonWidget> var4 = buttons.iterator();

		while (var4.hasNext()) {
			AbstractButtonWidget abstractButtonWidget = var4.next();
			if (abstractButtonWidget.isHovered()) {
				abstractButtonWidget.renderToolTip(matrices, mouseX - x, mouseY - y);
				break;
			}
		}

	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(TEXTURE);
		int i = (width - backgroundWidth) / 2;
		int j = (height - backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight);
		itemRenderer.zOffset = 100.0F;
		itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.NETHERITE_INGOT), i + 42 + 66, j + 109);
		itemRenderer.zOffset = 0.0F;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Environment(EnvType.CLIENT)
	class CancelButtonWidget extends NetheriteBeaconScreen.IconButtonWidget {
		public CancelButtonWidget(int x, int y) {
			super(x, y, 112, 220);
		}

		@Override
		public void onPress() {
			NetheriteBeaconScreen.this.client.player.networkHandler.sendPacket(
					new GuiCloseC2SPacket(NetheriteBeaconScreen.this.client.player.currentScreenHandler.syncId));
			NetheriteBeaconScreen.this.client.openScreen((Screen) null);
		}

		@Override
		public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
			NetheriteBeaconScreen.this.renderTooltip(matrices, ScreenTexts.CANCEL, mouseX, mouseY);
		}
	}

	@Environment(EnvType.CLIENT)
	class DoneButtonWidget extends NetheriteBeaconScreen.IconButtonWidget {
		public DoneButtonWidget(int x, int y) {
			super(x, y, 90, 220);
		}

		@Override
		public void onPress() {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			try {
				new UpdateNetheriteBeaconC2SPacket(StatusEffect.getRawId(primaryEffect),
						StatusEffect.getRawId(secondaryEffect), StatusEffect.getRawId(tertiaryEffect)).write(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ClientSidePacketRegistry.INSTANCE.sendToServer(UpdateNetheriteBeaconC2SPacket.ID, buf);
			NetheriteBeaconScreen.this.client.player.networkHandler.sendPacket(
					new GuiCloseC2SPacket(NetheriteBeaconScreen.this.client.player.currentScreenHandler.syncId));
			NetheriteBeaconScreen.this.client.openScreen((Screen) null);
		}

		@Override
		public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
			NetheriteBeaconScreen.this.renderTooltip(matrices, ScreenTexts.DONE, mouseX, mouseY);
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
		protected void renderExtra(MatrixStack matrixStack) {
			this.drawTexture(matrixStack, x + 2, y + 2, u, v, 18, 18);
		}
	}

	@Environment(EnvType.CLIENT)
	class EffectButtonWidget extends BaseButtonWidget {
		private final StatusEffect effect;
		private final Sprite sprite;
		private final int level;
		private final Text title;

		public EffectButtonWidget(int x, int y, StatusEffect statusEffect, int level) {
			super(x, y);
			effect = statusEffect;
			sprite = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect);
			this.level = level;
			title = generateTooltip(statusEffect, level);
		}

		private Text generateTooltip(StatusEffect statusEffect, int level) {
			MutableText mutableText = new TranslatableText(statusEffect.getTranslationKey());
			if (level == 2 && statusEffect != StatusEffects.REGENERATION) {
				mutableText.append(" II");
			}

			if (level == 3 && statusEffect != StatusEffects.GLOWING) {
				if (statusEffect != StatusEffects.REGENERATION) {
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
					if (secondaryEffect != StatusEffects.REGENERATION) {
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
		public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
			NetheriteBeaconScreen.this.renderTooltip(matrices, title, mouseX, mouseY);
		}

		@Override
		protected void renderExtra(MatrixStack matrixStack) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(sprite.getAtlas().getId());
			drawSprite(matrixStack, x + 2, y + 2, getZOffset(), 18, 18, sprite);
		}
	}

	@Environment(EnvType.CLIENT)
	abstract static class BaseButtonWidget extends AbstractPressableButtonWidget {
		private boolean disabled;

		protected BaseButtonWidget(int x, int y) {
			super(x, y, 22, 22, LiteralText.EMPTY);
		}

		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(NetheriteBeaconScreen.TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			int j = 0;
			if (!active) {
				j += width * 2;
			} else if (disabled) {
				j += width * 1;
			} else if (isHovered()) {
				j += width * 3;
			}

			this.drawTexture(matrices, x, y, j, 219, width, height);
			renderExtra(matrices);
		}

		protected abstract void renderExtra(MatrixStack matrixStack);

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
	}
}

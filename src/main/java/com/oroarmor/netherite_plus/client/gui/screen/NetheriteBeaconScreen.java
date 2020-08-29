package com.oroarmor.netherite_plus.client.gui.screen;

import java.util.Iterator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.network.packet.c2s.play.GuiCloseC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class NetheriteBeaconScreen extends HandledScreen<NetheriteBeaconScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/beacon.png");
	private static final Text PRIMARY_TEXT = new TranslatableText("block.minecraft.beacon.primary");
	private static final Text SECONDARY_TEXT = new TranslatableText("block.minecraft.beacon.secondary");
	private DoneButtonWidget doneButton;
	private boolean consumeGem;
	private StatusEffect primaryEffect;
	private StatusEffect secondaryEffect;

	public NetheriteBeaconScreen(final NetheriteBeaconScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundWidth = 230;
		this.backgroundHeight = 219;
		handler.addListener(new ScreenHandlerListener() {
			@Override
			public void onHandlerRegistered(ScreenHandler handlerx, DefaultedList<ItemStack> stacks) {
			}

			@Override
			public void onSlotUpdate(ScreenHandler handlerx, int slotId, ItemStack stack) {
			}

			@Override
			public void onPropertyUpdate(ScreenHandler handlerx, int property, int value) {
				NetheriteBeaconScreen.this.primaryEffect = handler.getPrimaryEffect();
				NetheriteBeaconScreen.this.secondaryEffect = handler.getSecondaryEffect();
				NetheriteBeaconScreen.this.consumeGem = true;
			}
		});
	}

	@Override
	protected void init() {
		super.init();
		this.doneButton = this.addButton(new DoneButtonWidget(this.x + 164, this.y + 107));
		this.addButton(new CancelButtonWidget(this.x + 190, this.y + 107));
		this.consumeGem = true;
		this.doneButton.active = false;
	}

	@Override
	public void tick() {
		super.tick();
		int i = this.handler.getProperties();
		if (this.consumeGem && i >= 0) {
			this.consumeGem = false;

			int o;
			int p;
			int q;
			StatusEffect statusEffect2;
			EffectButtonWidget effectButtonWidget2;
			for (int j = 0; j <= 2; ++j) {
				o = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[j].length;
				p = o * 22 + (o - 1) * 2;

				for (q = 0; q < o; ++q) {
					statusEffect2 = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[j][q];
					effectButtonWidget2 = new EffectButtonWidget(this.x + 76 + q * 24 - p / 2, this.y + 22 + j * 25,
							statusEffect2, true);
					this.addButton(effectButtonWidget2);
					if (j >= i) {
						effectButtonWidget2.active = false;
					} else if (statusEffect2 == this.primaryEffect) {
						effectButtonWidget2.setDisabled(true);
					}
				}
			}

			o = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[3].length + 1;
			p = o * 22 + (o - 1) * 2;

			for (q = 0; q < o - 1; ++q) {
				statusEffect2 = NetheriteBeaconBlockEntity.EFFECTS_BY_LEVEL[3][q];
				effectButtonWidget2 = new EffectButtonWidget(this.x + 167 + q * 24 - p / 2, this.y + 47, statusEffect2,
						false);
				this.addButton(effectButtonWidget2);
				if (3 >= i) {
					effectButtonWidget2.active = false;
				} else if (statusEffect2 == this.secondaryEffect) {
					effectButtonWidget2.setDisabled(true);
				}
			}

			if (this.primaryEffect != null) {
				EffectButtonWidget effectButtonWidget3 = new EffectButtonWidget(this.x + 167 + (o - 1) * 24 - p / 2,
						this.y + 47, this.primaryEffect, false);
				this.addButton(effectButtonWidget3);
				if (3 >= i) {
					effectButtonWidget3.active = false;
				} else if (this.primaryEffect == this.secondaryEffect) {
					effectButtonWidget3.setDisabled(true);
				}
			}
		}

		this.doneButton.active = this.handler.hasPayment() && this.primaryEffect != null;
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		drawCenteredText(matrices, this.textRenderer, PRIMARY_TEXT, 62, 10, 14737632);
		drawCenteredText(matrices, this.textRenderer, SECONDARY_TEXT, 169, 10, 14737632);
		Iterator<AbstractButtonWidget> var4 = this.buttons.iterator();

		while (var4.hasNext()) {
			AbstractButtonWidget abstractButtonWidget = var4.next();
			if (abstractButtonWidget.isHovered()) {
				abstractButtonWidget.renderToolTip(matrices, mouseX - this.x, mouseY - this.y);
				break;
			}
		}

	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
		this.itemRenderer.zOffset = 100.0F;
		this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.NETHERITE_INGOT), i + 20, j + 109);
		this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.EMERALD), i + 41, j + 109);
		this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.DIAMOND), i + 41 + 22, j + 109);
		this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.GOLD_INGOT), i + 42 + 44, j + 109);
		this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.IRON_INGOT), i + 42 + 66, j + 109);
		this.itemRenderer.zOffset = 0.0F;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
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
			NetheriteBeaconScreen.this.client.getNetworkHandler().sendPacket(
					new UpdateBeaconC2SPacket(StatusEffect.getRawId(NetheriteBeaconScreen.this.primaryEffect),
							StatusEffect.getRawId(NetheriteBeaconScreen.this.secondaryEffect)));
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
			this.drawTexture(matrixStack, this.x + 2, this.y + 2, this.u, this.v, 18, 18);
		}
	}

	@Environment(EnvType.CLIENT)
	class EffectButtonWidget extends BaseButtonWidget {
		private final StatusEffect effect;
		private final Sprite sprite;
		private final boolean primary;
		private final Text field_26562;

		public EffectButtonWidget(int x, int y, StatusEffect statusEffect, boolean primary) {
			super(x, y);
			this.effect = statusEffect;
			this.sprite = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect);
			this.primary = primary;
			this.field_26562 = this.method_30902(statusEffect, primary);
		}

		private Text method_30902(StatusEffect statusEffect, boolean bl) {
			MutableText mutableText = new TranslatableText(statusEffect.getTranslationKey());
			if (!bl && statusEffect != StatusEffects.REGENERATION) {
				mutableText.append(" II");
			}

			return mutableText;
		}

		@Override
		public void onPress() {
			if (!this.isDisabled()) {
				if (this.primary) {
					NetheriteBeaconScreen.this.primaryEffect = this.effect;
				} else {
					NetheriteBeaconScreen.this.secondaryEffect = this.effect;
				}

				NetheriteBeaconScreen.this.buttons.clear();
				NetheriteBeaconScreen.this.children.clear();
				NetheriteBeaconScreen.this.init();
				NetheriteBeaconScreen.this.tick();
			}
		}

		@Override
		public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
			NetheriteBeaconScreen.this.renderTooltip(matrices, this.field_26562, mouseX, mouseY);
		}

		@Override
		protected void renderExtra(MatrixStack matrixStack) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(this.sprite.getAtlas().getId());
			drawSprite(matrixStack, this.x + 2, this.y + 2, this.getZOffset(), 18, 18, this.sprite);
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
			if (!this.active) {
				j += this.width * 2;
			} else if (this.disabled) {
				j += this.width * 1;
			} else if (this.isHovered()) {
				j += this.width * 3;
			}

			this.drawTexture(matrices, this.x, this.y, j, 219, this.width, this.height);
			this.renderExtra(matrices);
		}

		protected abstract void renderExtra(MatrixStack matrixStack);

		public boolean isDisabled() {
			return this.disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
	}
}

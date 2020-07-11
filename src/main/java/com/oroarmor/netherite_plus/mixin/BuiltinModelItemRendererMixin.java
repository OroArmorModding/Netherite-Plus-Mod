package com.oroarmor.netherite_plus.mixin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

	@Shadow
	@Final
	private static ShulkerBoxBlockEntity[] RENDER_SHULKER_BOX_DYED;
	@Shadow
	@Final
	private static ShulkerBoxBlockEntity RENDER_SHULKER_BOX;
	@Shadow
	@Final
	private ChestBlockEntity renderChestNormal;
	@Shadow
	@Final
	private ChestBlockEntity renderChestTrapped;
	@Shadow
	@Final
	private EnderChestBlockEntity renderChestEnder;
	@Shadow
	@Final
	private BannerBlockEntity renderBanner;
	@Shadow
	@Final
	private BedBlockEntity renderBed;
	@Shadow
	@Final
	private ConduitBlockEntity renderConduit;
	@Shadow
	@Final
	private ShieldEntityModel modelShield;
	@Shadow
	@Final
	private TridentEntityModel modelTrident;

	private static final NetheriteShulkerBoxBlockEntity[] RENDER_NETHERITE_SHULKER_BOX_DYED = Arrays
			.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId))
			.map(NetheriteShulkerBoxBlockEntity::new).toArray((i) -> {
				return new NetheriteShulkerBoxBlockEntity[i];
			});
	private static final NetheriteShulkerBoxBlockEntity RENDER_NETHERITE_SHULKER_BOX = new NetheriteShulkerBoxBlockEntity(
			(DyeColor) null);

	@SuppressWarnings("unused")
	@Overwrite
	public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		Item item = stack.getItem();
		if (item instanceof BlockItem) {
			Block block = ((BlockItem) item).getBlock();
			if (block instanceof AbstractSkullBlock) {
				GameProfile gameProfile = null;
				if (stack.hasTag()) {
					CompoundTag compoundTag = stack.getTag();
					if (compoundTag.contains("SkullOwner", 10)) {
						gameProfile = NbtHelper.toGameProfile(compoundTag.getCompound("SkullOwner"));
					} else if (compoundTag.contains("SkullOwner", 8)
							&& !StringUtils.isBlank(compoundTag.getString("SkullOwner"))) {
						gameProfile = new GameProfile((UUID) null, compoundTag.getString("SkullOwner"));
						gameProfile = SkullBlockEntity.loadProperties(gameProfile);
						compoundTag.remove("SkullOwner");
						compoundTag.put("SkullOwner", NbtHelper.fromGameProfile(new CompoundTag(), gameProfile));
					}
				}

				SkullBlockEntityRenderer.render((Direction) null, 180.0F, ((AbstractSkullBlock) block).getSkullType(),
						gameProfile, 0.0F, matrixStack, vertexConsumerProvider, i);
			} else {
				Object blockEntity9;
				if (block instanceof AbstractBannerBlock) {
					renderBanner.readFrom(stack, ((AbstractBannerBlock) block).getColor());
					blockEntity9 = renderBanner;
				} else if (block instanceof BedBlock) {
					renderBed.setColor(((BedBlock) block).getColor());
					blockEntity9 = renderBed;
				} else if (block == Blocks.CONDUIT) {
					blockEntity9 = renderConduit;
				} else if (block == Blocks.CHEST) {
					blockEntity9 = renderChestNormal;
				} else if (block == Blocks.ENDER_CHEST) {
					blockEntity9 = renderChestEnder;
				} else if (block == Blocks.TRAPPED_CHEST) {
					blockEntity9 = renderChestTrapped;
				} else if (block instanceof NetheriteShulkerBoxBlock) {

					DyeColor dyeColor = NetheriteShulkerBoxBlock.getColor(item);
					if (dyeColor == null) {
						blockEntity9 = RENDER_NETHERITE_SHULKER_BOX;
					} else {
						blockEntity9 = RENDER_NETHERITE_SHULKER_BOX_DYED[dyeColor.getId()];
					}

				} else {
					if (!(block instanceof ShulkerBoxBlock)) {
						return;
					}

					DyeColor dyeColor = ShulkerBoxBlock.getColor(item);
					if (dyeColor == null) {
						blockEntity9 = RENDER_SHULKER_BOX;
					} else {
						blockEntity9 = RENDER_SHULKER_BOX_DYED[dyeColor.getId()];
					}
				}

				BlockEntityRenderDispatcher.INSTANCE.renderEntity((BlockEntity) blockEntity9, matrixStack,
						vertexConsumerProvider, i, j);
			}
		} else {
			if (item == Items.SHIELD) {
				boolean bl = stack.getSubTag("BlockEntityTag") != null;
				matrixStack.push();
				matrixStack.scale(1.0F, -1.0F, -1.0F);
				SpriteIdentifier spriteIdentifier = bl ? ModelLoader.SHIELD_BASE : ModelLoader.SHIELD_BASE_NO_PATTERN;
				VertexConsumer vertexConsumer = spriteIdentifier.getSprite()
						.getTextureSpecificVertexConsumer(ItemRenderer.method_29711(vertexConsumerProvider,
								modelShield.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
				modelShield.method_23775().render(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
				if (bl) {
					List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity
							.method_24280(ShieldItem.getColor(stack), BannerBlockEntity.getPatternListTag(stack));
					BannerBlockEntityRenderer.renderCanvas(matrixStack, vertexConsumerProvider, i, j,
							modelShield.method_23774(), spriteIdentifier, false, list, stack.hasGlint());
				} else {
					modelShield.method_23774().render(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
				}

				matrixStack.pop();
			} else if (item == Items.TRIDENT) {
				matrixStack.push();
				matrixStack.scale(1.0F, -1.0F, -1.0F);
				VertexConsumer vertexConsumer2 = ItemRenderer.method_29711(vertexConsumerProvider,
						modelTrident.getLayer(TridentEntityModel.TEXTURE), false, stack.hasGlint());
				modelTrident.render(matrixStack, vertexConsumer2, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
				matrixStack.pop();
			}

		}
	}
}

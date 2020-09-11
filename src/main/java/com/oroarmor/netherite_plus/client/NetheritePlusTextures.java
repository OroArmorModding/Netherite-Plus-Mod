package com.oroarmor.netherite_plus.client;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Arrays;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class NetheritePlusTextures {

	public static final SpriteIdentifier NETHERITE_SHIELD_BASE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, id("entity/netherite_shield_base"));

	public static final SpriteIdentifier NETHERITE_SHIELD_BASE_NO_PATTERN = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, id("entity/netherite_shield_base_nopattern"));
	public static final Identifier SHULKER_BOXES_ATLAS_TEXTURE = id("textures/atlas/shulker_boxes.png");

	public static void makeAtlases(Consumer<SpriteIdentifier> consumer) {
		consumer.accept(new SpriteIdentifier(SHULKER_BOXES_ATLAS_TEXTURE, id(NetheritePlusTextures.makePath(null))));
		Arrays.stream(DyeColor.values()).forEach(c -> consumer.accept(new SpriteIdentifier(SHULKER_BOXES_ATLAS_TEXTURE, id(NetheritePlusTextures.makePath(c)))));
	}

	static String makePath(DyeColor color) {
		if (color != null) {
			return "entity/netherite_shulker/netherite_shulker_" + color.getName();
		}

		return "entity/netherite_shulker/netherite_shulker";
	}

	public static void register() {
		ClientSpriteRegistryCallback.event(NetheritePlusTextures.SHULKER_BOXES_ATLAS_TEXTURE).register(NetheritePlusTextures::registerShulkerBoxTextures);
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register(NetheritePlusTextures::registerShieldTextures);
	}

	public static void registerShieldTextures(SpriteAtlasTexture atlas, ClientSpriteRegistryCallback.Registry registry) {
		registry.register(NETHERITE_SHIELD_BASE.getTextureId());
		registry.register(NETHERITE_SHIELD_BASE_NO_PATTERN.getTextureId());
	}

	public static void registerShulkerBoxTextures(SpriteAtlasTexture atlas, ClientSpriteRegistryCallback.Registry registry) {
		registry.register(id(makePath(null)));
		Arrays.stream(DyeColor.values()).forEach(c -> registry.register(id(makePath(c))));
	}

}

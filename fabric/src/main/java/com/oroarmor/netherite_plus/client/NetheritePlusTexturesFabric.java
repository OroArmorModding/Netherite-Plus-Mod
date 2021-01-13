package com.oroarmor.netherite_plus.client;

import java.util.Arrays;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.DyeColor;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
import static com.oroarmor.netherite_plus.client.NetheritePlusTextures.*;

public class NetheritePlusTexturesFabric {
	public static void register() {
		ClientSpriteRegistryCallback.event(SHULKER_BOXES_ATLAS_TEXTURE).register(NetheritePlusTexturesFabric::registerShulkerBoxTextures);
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(NetheritePlusTexturesFabric::registerShieldTextures);
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

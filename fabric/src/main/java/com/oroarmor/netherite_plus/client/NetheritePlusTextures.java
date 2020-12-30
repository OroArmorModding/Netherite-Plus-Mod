package com.oroarmor.netherite_plus.client;

import java.util.Arrays;
import java.util.function.Consumer;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusTextures {

	public static final Material NETHERITE_SHIELD_BASE = new Material(TextureAtlas.LOCATION_BLOCKS, id("entity/netherite_shield_base"));

	public static final Material NETHERITE_SHIELD_BASE_NO_PATTERN = new Material(TextureAtlas.LOCATION_BLOCKS, id("entity/netherite_shield_base_nopattern"));
	public static final ResourceLocation SHULKER_BOXES_ATLAS_TEXTURE = id("textures/atlas/shulker_boxes.png");

	public static void makeAtlases(Consumer<Material> consumer) {
		consumer.accept(new Material(SHULKER_BOXES_ATLAS_TEXTURE, id(NetheritePlusTextures.makePath(null))));
		Arrays.stream(DyeColor.values()).forEach(c -> consumer.accept(new Material(SHULKER_BOXES_ATLAS_TEXTURE, id(NetheritePlusTextures.makePath(c)))));
	}

	static String makePath(DyeColor color) {
		if (color != null) {
			return "entity/netherite_shulker/netherite_shulker_" + color.getName();
		}

		return "entity/netherite_shulker/netherite_shulker";
	}

	public static void register() {
		ClientSpriteRegistryCallback.event(NetheritePlusTextures.SHULKER_BOXES_ATLAS_TEXTURE).register(NetheritePlusTextures::registerShulkerBoxTextures);
		ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register(NetheritePlusTextures::registerShieldTextures);
	}

	public static void registerShieldTextures(TextureAtlas atlas, ClientSpriteRegistryCallback.Registry registry) {
		registry.register(NETHERITE_SHIELD_BASE.texture());
		registry.register(NETHERITE_SHIELD_BASE_NO_PATTERN.texture());
	}

	public static void registerShulkerBoxTextures(TextureAtlas atlas, ClientSpriteRegistryCallback.Registry registry) {
		registry.register(id(makePath(null)));
		Arrays.stream(DyeColor.values()).forEach(c -> registry.register(id(makePath(c))));
	}

}

package com.oroarmor.netherite_plus.client;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_BLACK_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_BLUE_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_BROWN_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_CYAN_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_GRAY_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_GREEN_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_LIGHT_BLUE_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_LIGHT_GRAY_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_LIME_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_MAGENTA_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_ORANGE_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_PINK_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_PURPLE_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_RED_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_SHIELD;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_TRIDENT;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_WHITE_SHULKER_BOX;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_YELLOW_SHULKER_BOX;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.client.render.NetheriteBeaconBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShieldItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShulkerBoxItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteTridentItemRenderer;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class NetheritePlusClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY,
				NetheriteShulkerBoxBlockEntityRenderer::new);

		BlockEntityRendererRegistry.INSTANCE.register(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY,
				NetheriteBeaconBlockEntityRenderer::new);

		NetheritePlusTextures.register();

		NetheritePlusModelProvider.registerItemsWithModelProvider();

		NetheritePlusScreenHandlers.initializeClient();

		BlockRenderLayerMap.INSTANCE.putBlock(NetheritePlusBlocks.NETHERITE_BEACON, RenderLayer.getCutout());

		BuiltinItemRenderer shulkerRenderer = new NetheriteShulkerBoxItemRenderer();

		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_WHITE_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_ORANGE_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_MAGENTA_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_LIGHT_BLUE_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_YELLOW_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_LIME_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_PINK_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_GRAY_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_LIGHT_GRAY_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_CYAN_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_PURPLE_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_BLUE_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_BROWN_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_GREEN_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_RED_SHULKER_BOX, shulkerRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_BLACK_SHULKER_BOX, shulkerRenderer);

		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_SHIELD, new NetheriteShieldItemRenderer());
		BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_TRIDENT, new NetheriteTridentItemRenderer());

	}
}

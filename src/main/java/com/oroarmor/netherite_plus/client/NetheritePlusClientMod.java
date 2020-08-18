package com.oroarmor.netherite_plus.client;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class NetheritePlusClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY,
				NetheriteShulkerBoxBlockEntityRenderer::new);

		NetheritePlusTextures.register();

		NetheritePlusModelProvider.registerItemsWithModelProvider();

		NetheritePlusScreenHandlers.initializeClient();
	}
}

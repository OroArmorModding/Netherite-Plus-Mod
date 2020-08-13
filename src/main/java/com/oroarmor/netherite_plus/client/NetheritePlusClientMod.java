package com.oroarmor.netherite_plus.client;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteAnvilScreen;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteTridentEntityRenderer;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.Factory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.entity.EntityType;

public class NetheritePlusClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY,
				NetheriteShulkerBoxBlockEntityRenderer::new);

		NetheritePlusTextures.register();

		NetheritePlusModelProvider.registerItemsWithModelProvider();

		ScreenRegistry.register(NetheritePlusMod.NETHERITE_ANVIL,
				(Factory<NetheriteAnvilScreenHandler, NetheriteAnvilScreen>) (handler, inventory,
						title) -> new NetheriteAnvilScreen(handler, inventory, title));

		EntityRendererRegistry.INSTANCE.register(EntityType.TRIDENT, NetheriteTridentEntityRenderer::new);

	}
}

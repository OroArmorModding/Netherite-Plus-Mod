/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.netherite_plus.client;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.render.NetheriteBeaconBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteElytraFeatureRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShieldItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShulkerBoxItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteTridentItemRenderer;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.TranslatableText;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

public class NetheritePlusClientModFabric implements ClientModInitializer {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onInitializeClient() {
		if(NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) BlockEntityRendererRegistry.INSTANCE.register((BlockEntityType<NetheriteShulkerBoxBlockEntity>) NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), NetheriteShulkerBoxBlockEntityRenderer::new);
		if(NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) BlockEntityRendererRegistry.INSTANCE.register((BlockEntityType<NetheriteBeaconBlockEntity>) NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), NetheriteBeaconBlockEntityRenderer::new);

		NetheritePlusClientMod.init();
		NetheritePlusTexturesFabric.register();
		NetheritePlusModelProviderFabric.registerItemsWithModelProvider();
		NetheritePlusScreenHandlers.initializeClient();

		if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
			BlockRenderLayerMap.INSTANCE.putBlock(NetheritePlusBlocks.NETHERITE_BEACON.get(), RenderLayer.getCutout());
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) {
			BuiltinItemRendererRegistry.DynamicItemRenderer shulkerRenderer = NetheriteShulkerBoxItemRenderer::render;

			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_WHITE_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_ORANGE_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_MAGENTA_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_YELLOW_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_LIME_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_PINK_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_GRAY_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_CYAN_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_PURPLE_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_BLUE_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_BROWN_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_GREEN_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_RED_SHULKER_BOX.get(), shulkerRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_BLACK_SHULKER_BOX.get(), shulkerRenderer);
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue())
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_SHIELD.get(), NetheriteShieldItemRenderer::render);
		if (NetheritePlusConfig.ENABLED.ENABLED_TRIDENT.getValue())
			BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_TRIDENT.get(), NetheriteTridentItemRenderer::render);

		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> entityRenderer, RegistrationHelper registrationHelper) -> {
			if (entityRenderer.getModel() instanceof PlayerEntityModel || entityRenderer.getModel() instanceof BipedEntityModel || entityRenderer.getModel() instanceof ArmorStandEntityModel) {
				registrationHelper.register(new NetheriteElytraFeatureRenderer(entityRenderer));
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(id("lava_vision_update"), (minecraft, listener, buf, sender) -> {
			NetheritePlusClientMod.LAVA_VISION_DISTANCE = buf.getDouble(0);
		});

		if (FabricLoader.getInstance().isModLoaded("trinkets") && NetheritePlusConfig.ENABLED.ENABLED_ELYTRA.getValue()) {
			ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
				if (stack.getItem() == NETHERITE_ELYTRA.get()) {
					lines.add(new TranslatableText("warning.netherite_elytra.trinkets"));
				}
			});
		}
	}
}

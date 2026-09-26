/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
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

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.LinkedList;
import java.util.Queue;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.client.render.NetheriteBeaconBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteElytraFeatureRenderer;
import com.oroarmor.netherite_plus.client.render.NetheritePlusBuiltinItemModelRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.network.LavaVisionUpdatePacket;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class NetheritePlusClientMod implements ClientModInitializer {
    public static final Queue<Integer> TRIDENT_QUEUE = new LinkedList<>();
    public static double LAVA_VISION_DISTANCE = NetheritePlusMod.CONFIG.graphics.lava_vision_distance.value();

    public static void registerBuiltinItemRenderers(Minecraft client) {
        NetheritePlusBuiltinItemModelRenderer builtinItemModelRenderer = new NetheritePlusBuiltinItemModelRenderer(client.getBlockEntityRenderDispatcher(), client.getEntityModels());

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(builtinItemModelRenderer);

        BuiltinItemRendererRegistry.DynamicItemRenderer dynamicItemRenderer = builtinItemModelRenderer::render;
        if (NetheritePlusMod.CONFIG.enabled.shulker_boxes.value()) {
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_WHITE_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_ORANGE_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_MAGENTA_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_LIGHT_BLUE_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_YELLOW_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_LIME_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_PINK_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_GRAY_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_LIGHT_GRAY_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_CYAN_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_PURPLE_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_BLUE_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_BROWN_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_GREEN_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_RED_SHULKER_BOX, dynamicItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_BLACK_SHULKER_BOX, dynamicItemRenderer);
        }

        if (NetheritePlusMod.CONFIG.enabled.shields.value()) {
//            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_SHIELD, dynamicItemRenderer);
        }
        if (NetheritePlusMod.CONFIG.enabled.trident.value()) {
            BuiltinItemRendererRegistry.INSTANCE.register(NetheritePlusItems.NETHERITE_TRIDENT, dynamicItemRenderer);
        }
    }

    public void onInitializeClient(ModContainer mod) {
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            ClientPlayNetworking.registerReceiver(LavaVisionUpdatePacket.ID, (minecraft, listener, buf, responseSender) -> {
                LAVA_VISION_DISTANCE = buf.readDouble();
            });

            ClientPlayNetworking.registerReceiver(id("lava_vision_update"), (minecraft, listener, buf, sender) -> {
                NetheritePlusClientMod.LAVA_VISION_DISTANCE = buf.getDouble(0);
            });

            ClientPlayNetworking.registerReceiver(id("netherite_trident"), (minecraft, listener, buf, responseSender) -> TRIDENT_QUEUE.add(buf.readInt()));
        });

//        NetheritePlusTextures.register();

        BlockEntityRendererRegistry.register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY, NetheriteShulkerBoxBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY, NetheriteBeaconBlockEntityRenderer::new);

        NetheritePlusModelProvider.registerItemsWithModelProvider();
        NetheritePlusScreenHandlers.initializeClient();

        if (NetheritePlusMod.CONFIG.enabled.beacon.value()) {
            BlockRenderLayerMap.put(RenderType.cutout(), NetheritePlusBlocks.NETHERITE_BEACON);
        }

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(((EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> entityRenderer, LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper registrationHelper, EntityRendererProvider.Context context) -> {
            if (entityRenderer.getModel() instanceof PlayerModel || entityRenderer.getModel() instanceof HumanoidModel || entityRenderer.getModel() instanceof ArmorStandModel) {
                registrationHelper.register(new NetheriteElytraFeatureRenderer<>(entityRenderer, context.getModelSet()));
            }
        }));
    }
}

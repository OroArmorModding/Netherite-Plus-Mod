package com.oroarmor.netherite_plus.client;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
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
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class NetheritePlusClientMod implements ClientModInitializer {
    public static double LAVA_VISION_DISTANCE = NetheritePlusConfig.GRAPHICS.LAVA_VISION_DISTANCE.getValue();

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register((BlockEntityType<NetheriteShulkerBoxBlockEntity>) NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), NetheriteShulkerBoxBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register((BlockEntityType<NetheriteBeaconBlockEntity>) NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), NetheriteBeaconBlockEntityRenderer::new);

        NetheritePlusTextures.register();
        NetheritePlusModelProvider.registerItemsWithModelProvider();
        NetheritePlusScreenHandlers.initializeClient();

        if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
            BlockRenderLayerMap.INSTANCE.putBlock(NetheritePlusBlocks.NETHERITE_BEACON.get(), RenderType.cutout());
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) {
            BuiltinItemRendererRegistry.DynamicItemRenderer shulkerRenderer = new NetheriteShulkerBoxItemRenderer();

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
            BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_SHIELD.get(), new NetheriteShieldItemRenderer());
        if (NetheritePlusConfig.ENABLED.ENABLED_TRIDENT.getValue())
            BuiltinItemRendererRegistry.INSTANCE.register(NETHERITE_TRIDENT.get(), new NetheriteTridentItemRenderer());

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> entityRenderer, RegistrationHelper registrationHelper) -> {
            if (entityRenderer.getModel() instanceof PlayerModel || entityRenderer.getModel() instanceof HumanoidModel || entityRenderer.getModel() instanceof ArmorStandModel) {
                registrationHelper.register(new NetheriteElytraFeatureRenderer(entityRenderer));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(id("lava_vision_packet"), (minecraft, listener, buf, sender) -> {
            LAVA_VISION_DISTANCE = buf.getDouble(0);
        });
    }
}

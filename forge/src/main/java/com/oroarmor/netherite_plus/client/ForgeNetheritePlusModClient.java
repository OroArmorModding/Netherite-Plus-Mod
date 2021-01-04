package com.oroarmor.netherite_plus.client;

import java.util.Arrays;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.render.NetheriteBeaconBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteShieldItemRenderer;
import com.oroarmor.netherite_plus.client.render.item.NetheriteTridentItemRenderer;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
import static com.oroarmor.netherite_plus.client.NetheritePlusTextures.makePath;

public class ForgeNetheritePlusModClient {
    @SubscribeEvent
    public void registerClient(FMLClientSetupEvent event) {
        NetheritePlusScreenHandlers.init();
        NetheritePlusScreenHandlers.initializeClient();

        ClientRegistry.bindTileEntityRenderer((BlockEntityType<NetheriteBeaconBlockEntity>)NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), NetheriteBeaconBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType<NetheriteShulkerBoxBlockEntity>) NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), NetheriteShulkerBoxBlockEntityRenderer::new);

        NetheritePlusModelProvider.registerItemsWithModelProvider();

        BlockEntityRenderDispatcher.instance.setSpecialRendererInternal((BlockEntityType<NetheriteBeaconBlockEntity>)NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), new NetheriteBeaconBlockEntityRenderer(BlockEntityRenderDispatcher.instance));
        BlockEntityRenderDispatcher.instance.setSpecialRendererInternal((BlockEntityType<NetheriteShulkerBoxBlockEntity>)NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), new NetheriteShulkerBoxBlockEntityRenderer(BlockEntityRenderDispatcher.instance));
    }

    @SubscribeEvent
    public void registerTextureAtlas(TextureStitchEvent.Pre event) {
        event.addSprite(NetheritePlusTextures.NETHERITE_SHIELD_BASE.texture());
        event.addSprite(NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN.texture());
        event.addSprite(id(makePath(null)));
        Arrays.stream(DyeColor.values()).forEach(c -> event.addSprite(id(makePath(c))));
    }
}

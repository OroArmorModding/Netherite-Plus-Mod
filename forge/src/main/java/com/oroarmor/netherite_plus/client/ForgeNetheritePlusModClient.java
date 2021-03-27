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

import java.util.Arrays;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.render.NetheriteBeaconBlockEntityRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteElytraFeatureRenderer;
import com.oroarmor.netherite_plus.client.render.NetheritePlusBuiltinItemModelRenderer;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import me.shedaniel.architectury.registry.RenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
import static com.oroarmor.netherite_plus.client.NetheritePlusTextures.makePath;

@OnlyIn(Dist.CLIENT)
public class ForgeNetheritePlusModClient {
    public static void addISTER(Item.Settings properties) {
        properties.setISTER(() -> () -> new NetheritePlusBuiltinItemModelRenderer());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerClient(FMLClientSetupEvent event) {
        NetheritePlusClientMod.init();
        NetheritePlusScreenHandlers.init();
        NetheritePlusScreenHandlers.initializeClient();

        if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue())
            ClientRegistry.bindTileEntityRenderer((BlockEntityType<NetheriteBeaconBlockEntity>) NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), NetheriteBeaconBlockEntityRenderer::new);
        if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue())
            ClientRegistry.bindTileEntityRenderer((BlockEntityType<NetheriteShulkerBoxBlockEntity>) NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), NetheriteShulkerBoxBlockEntityRenderer::new);

        NetheritePlusModelProviderForge.registerItemsWithModelProvider();

        if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue())
            BlockEntityRenderDispatcher.INSTANCE.setSpecialRendererInternal((BlockEntityType<NetheriteBeaconBlockEntity>) NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), new NetheriteBeaconBlockEntityRenderer(BlockEntityRenderDispatcher.INSTANCE));
        if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue())
            BlockEntityRenderDispatcher.INSTANCE.setSpecialRendererInternal((BlockEntityType<NetheriteShulkerBoxBlockEntity>) NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), new NetheriteShulkerBoxBlockEntityRenderer(BlockEntityRenderDispatcher.INSTANCE));

        MinecraftClient.getInstance().getEntityRenderDispatcher().renderers.forEach((type, renderer) -> {
            if (!(renderer instanceof LivingEntityRenderer)) {
                return;
            }
            if (((LivingEntityRenderer<?, ?>) renderer).getModel() instanceof ArmorStandEntityModel || ((LivingEntityRenderer<?, ?>) renderer).getModel() instanceof BipedEntityModel || ((LivingEntityRenderer<?, ?>) renderer).getModel() instanceof PlayerEntityModel) {
                ((LivingEntityRenderer<?, ?>) renderer).addFeature(new NetheriteElytraFeatureRenderer((FeatureRendererContext<?, ?>) renderer));
            }
        });

        MinecraftClient.getInstance().getEntityRenderDispatcher().getSkinMap().values().forEach(player -> player.addFeature(new NetheriteElytraFeatureRenderer(player)));

        if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
            RenderTypes.register(RenderLayer.getCutout(), NetheritePlusBlocks.NETHERITE_BEACON.getOrNull());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerTextureAtlas(TextureStitchEvent.Pre event) {
        event.addSprite(NetheritePlusTextures.NETHERITE_SHIELD_BASE.getTextureId());
        event.addSprite(NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN.getTextureId());
        event.addSprite(id(makePath(null)));
        Arrays.stream(DyeColor.values()).forEach(c -> event.addSprite(id(makePath(c))));
    }
}

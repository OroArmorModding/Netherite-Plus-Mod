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

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
import static com.oroarmor.netherite_plus.client.NetheritePlusTextures.makePath;

@OnlyIn(Dist.CLIENT)
public class ForgeNetheritePlusModClient {
    public static void addISTER(Item.Properties properties) {
        properties.setISTER(() -> () -> new NetheritePlusBuiltinItemModelRenderer());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerClient(FMLClientSetupEvent event) {
        NetheritePlusClientMod.init();
        NetheritePlusScreenHandlers.init();
        NetheritePlusScreenHandlers.initializeClient();

        ClientRegistry.bindTileEntityRenderer((BlockEntityType<NetheriteBeaconBlockEntity>) NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), NetheriteBeaconBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType<NetheriteShulkerBoxBlockEntity>) NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), NetheriteShulkerBoxBlockEntityRenderer::new);

        NetheritePlusModelProvider.registerItemsWithModelProvider();

        BlockEntityRenderDispatcher.instance.setSpecialRendererInternal((BlockEntityType<NetheriteBeaconBlockEntity>) NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get(), new NetheriteBeaconBlockEntityRenderer(BlockEntityRenderDispatcher.instance));
        BlockEntityRenderDispatcher.instance.setSpecialRendererInternal((BlockEntityType<NetheriteShulkerBoxBlockEntity>) NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), new NetheriteShulkerBoxBlockEntityRenderer(BlockEntityRenderDispatcher.instance));


        Minecraft.getInstance().getEntityRenderDispatcher().renderers.forEach(
                (type, renderer) -> {
                    if (!(renderer instanceof LivingEntityRenderer)) {
                        return;
                    }
                    if (((LivingEntityRenderer) renderer).getModel() instanceof ArmorStandModel || ((LivingEntityRenderer) renderer).getModel() instanceof HumanoidModel || ((LivingEntityRenderer) renderer).getModel() instanceof PlayerModel) {
                        ((LivingEntityRenderer) renderer).addLayer(new NetheriteElytraFeatureRenderer((RenderLayerParent) renderer));
                    }
                });


        if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
            RenderTypes.register(RenderType.cutout(), NetheritePlusBlocks.NETHERITE_BEACON.getOrNull());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerTextureAtlas(TextureStitchEvent.Pre event) {
        event.addSprite(NetheritePlusTextures.NETHERITE_SHIELD_BASE.texture());
        event.addSprite(NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN.texture());
        event.addSprite(id(makePath(null)));
        Arrays.stream(DyeColor.values()).forEach(c -> event.addSprite(id(makePath(c))));
    }
}

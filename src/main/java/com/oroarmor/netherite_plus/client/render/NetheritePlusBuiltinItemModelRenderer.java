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

package com.oroarmor.netherite_plus.client.render;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import org.quiltmc.qsl.resource.loader.api.reloader.SimpleSynchronousResourceReloader;

public class NetheritePlusBuiltinItemModelRenderer implements SimpleSynchronousResourceReloader {
    private static final NetheriteShulkerBoxBlockEntity RENDER_NETHERITE_SHULKER_BOX = new NetheriteShulkerBoxBlockEntity(BlockPos.ZERO, NetheritePlusBlocks.NETHERITE_SHULKER_BOX.defaultBlockState());
    private static final NetheriteShulkerBoxBlockEntity[] RENDER_NETHERITE_SHULKER_BOX_DYED = Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(dyeColor -> new NetheriteShulkerBoxBlockEntity(dyeColor, BlockPos.ZERO, NetheritePlusBlocks.NETHERITE_SHULKER_BOX.defaultBlockState())).toArray(NetheriteShulkerBoxBlockEntity[]::new);

    private final EntityModelSet entityModelLoader;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    private ShieldModel modelShield;
    private TridentModel modelTrident;

    public NetheritePlusBuiltinItemModelRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelLoader) {
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        this.entityModelLoader = entityModelLoader;
    }

    public static void renderTrident(TridentModel model, ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        matrices.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer vertexConsumer2 = ItemRenderer.getFoilBufferDirect(vertexConsumers, model.renderType(id("textures/entity/netherite_trident.png")), false, stack.hasFoil());
        model.renderToBuffer(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.popPose();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.modelShield = new ShieldModel(this.entityModelLoader.bakeLayer(ModelLayers.SHIELD));
        this.modelTrident = new TridentModel(this.entityModelLoader.bakeLayer(ModelLayers.TRIDENT));
    }

    public void render(ItemStack itemStack, ItemDisplayContext transformType, PoseStack matrices, MultiBufferSource vertices, int light, int overlay) {
        if (itemStack.is(NetheritePlusItems.NETHERITE_TRIDENT)) {
            renderTrident(modelTrident, itemStack, transformType, matrices, vertices, light, overlay);
        } else if (itemStack.is(NetheritePlusItems.NETHERITE_SHIELD)) {
            renderShield(modelShield, itemStack, transformType, matrices, vertices, light, overlay);
        } else if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof NetheriteShulkerBoxBlock block) {
            NetheriteShulkerBoxBlockEntity entity;
            DyeColor dyecolor = block.getColor();
            if (dyecolor == null) {
                entity = RENDER_NETHERITE_SHULKER_BOX;
            } else {
                entity = RENDER_NETHERITE_SHULKER_BOX_DYED[dyecolor.getId()];
            }
            blockEntityRenderDispatcher.renderItem(entity, matrices, vertices, light, overlay);
        }
    }

    public void renderShield(ShieldModel model, ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        boolean bl = stack.getTagElement("BlockEntityTag") != null;
        matrices.pushPose();
        matrices.scale(1.0F, -1.0F, -1.0F);
        Material material = bl ? NetheritePlusTextures.NETHERITE_SHIELD_BASE : NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN;
        VertexConsumer vertexConsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(vertexConsumers, model.renderType(material.texture()), true, stack.hasFoil()));
        model.handle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        if (bl) {
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
            BannerRenderer.renderPatterns(matrices, vertexConsumers, light, overlay, model.plate(), material, false, list, stack.hasFoil());
        } else {
            model.plate().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        matrices.popPose();
    }

    @Override
    public ResourceLocation getQuiltId() {
        return id("netherite_plus_builtin_item_model_reloader");
    }
}

package com.oroarmor.netherite_plus.client.render.item;

import java.util.Arrays;
import java.util.Comparator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NetheriteShulkerBoxItemRenderer {

    private static final NetheriteShulkerBoxBlockEntity[] RENDER_NETHERITE_SHULKER_BOX_DYED = Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(NetheriteShulkerBoxBlockEntity::new).toArray((i) -> {
        return new NetheriteShulkerBoxBlockEntity[i];
    });
    private static final NetheriteShulkerBoxBlockEntity RENDER_NETHERITE_SHULKER_BOX = new NetheriteShulkerBoxBlockEntity(null);

    public static void render(ItemStack stack, TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        BlockEntity blockEntity9;
        DyeColor dyeColor = NetheriteShulkerBoxBlock.getColor(stack.getItem());
        if (dyeColor == null) {
            blockEntity9 = RENDER_NETHERITE_SHULKER_BOX;
        } else {
            blockEntity9 = RENDER_NETHERITE_SHULKER_BOX_DYED[dyeColor.getId()];
        }

        BlockEntityRenderDispatcher.instance.renderItem(blockEntity9, matrices, vertexConsumers, light, overlay);
    }

}

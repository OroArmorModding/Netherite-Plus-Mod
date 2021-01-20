package com.oroarmor.netherite_plus.client.render.item;

import java.util.Arrays;
import java.util.Comparator;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

public class NetheriteShulkerBoxItemRenderer {

    private static final NetheriteShulkerBoxBlockEntity[] RENDER_NETHERITE_SHULKER_BOX_DYED = Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(NetheriteShulkerBoxBlockEntity::new).toArray((i) -> {
        return new NetheriteShulkerBoxBlockEntity[i];
    });
    private static final NetheriteShulkerBoxBlockEntity RENDER_NETHERITE_SHULKER_BOX = new NetheriteShulkerBoxBlockEntity(null);

    public static void render(ItemStack stack, Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockEntity blockEntity9;
        DyeColor dyeColor = NetheriteShulkerBoxBlock.getColor(stack.getItem());
        if (dyeColor == null) {
            blockEntity9 = RENDER_NETHERITE_SHULKER_BOX;
        } else {
            blockEntity9 = RENDER_NETHERITE_SHULKER_BOX_DYED[dyeColor.getId()];
        }

        BlockEntityRenderDispatcher.INSTANCE.renderEntity(blockEntity9, matrices, vertexConsumers, light, overlay);
    }

}

package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (!(itemStack.getItem() instanceof BlockItem)) {
            return;
        }

        int i = state.get(CauldronBlock.LEVEL);
        Block block = ((BlockItem) itemStack.getItem()).getBlock();

        if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue() && block instanceof NetheriteShulkerBoxBlock && !world.isClient() && i > 0) {
            ItemStack itemStack5 = new ItemStack(NetheritePlusBlocks.NETHERITE_SHULKER_BOX.get(), 1);
            if (itemStack.hasTag()) {
                itemStack5.setTag(itemStack.getTag().copy());
            }

            player.setStackInHand(hand, itemStack5);
            ((CauldronBlock) (Object) this).setLevel(world, pos, state, i - 1);
            player.incrementStat(Stats.CLEAN_SHULKER_BOX);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}

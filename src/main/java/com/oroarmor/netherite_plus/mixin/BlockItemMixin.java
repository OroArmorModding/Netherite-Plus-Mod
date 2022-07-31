package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Shadow public abstract Block getBlock();

    @Inject(method = "canBeNested", at = @At("HEAD"), cancellable = true)
    public void cannotNestNetheriteBoxes(CallbackInfoReturnable<Boolean> cir) {
        if (this.getBlock() instanceof NetheriteShulkerBoxBlock) {
            cir.setReturnValue(false);
        }
    }
}

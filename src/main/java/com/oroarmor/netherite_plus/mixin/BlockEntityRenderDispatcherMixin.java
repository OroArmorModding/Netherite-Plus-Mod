package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.block.NetheritePlusModBlocks;
import com.oroarmor.netherite_plus.render.NetheriteShulkerBoxBlockEntityRenderer;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {

	@Shadow
	public abstract <E extends BlockEntity> void register(BlockEntityType<E> blockEntityType,
			BlockEntityRenderer<E> blockEntityRenderer);

	@Inject(method = "<init>", at = @At("RETURN"))
	public void addNetheriteShulkerBoxRenderer(CallbackInfo info) {
		this.register(NetheritePlusModBlocks.NETHERITE_SHULKER_BOX_ENTITY, new NetheriteShulkerBoxBlockEntityRenderer(
				(BlockEntityRenderDispatcher) (Object) this));
	}
}

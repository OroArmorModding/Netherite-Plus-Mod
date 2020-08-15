package com.oroarmor.netherite_plus.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.client.render.NetheriteShulkerBoxBlockEntityRenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

@Environment(EnvType.CLIENT)
@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {

	@SuppressWarnings("unused")
	@Inject(method = "<init>", at = @At("RETURN"))
	public void addNetheriteShulkerBoxRenderer(CallbackInfo info) {
		this.register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY,
				new NetheriteShulkerBoxBlockEntityRenderer((BlockEntityRenderDispatcher) (Object) this));
	}

	@Shadow
	public abstract <E extends BlockEntity> void register(BlockEntityType<E> blockEntityType,
			BlockEntityRenderer<E> blockEntityRenderer);
}

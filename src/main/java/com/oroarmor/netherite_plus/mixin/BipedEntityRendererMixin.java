package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.render.NetheriteElytraFeatureRenderer;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.MobEntity;

@Mixin(BipedEntityRenderer.class)
public abstract class BipedEntityRendererMixin<T extends MobEntity, M extends BipedEntityModel<T>>
		extends MobEntityRenderer<T, M> {

	public BipedEntityRendererMixin(EntityRenderDispatcher entityRenderDispatcher, M entityModel, float f) {
		super(entityRenderDispatcher, entityModel, f);
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Lnet/minecraft/client/render/entity/model/BipedEntityModel;FFFF)V", at = @At("RETURN"))
	private void addNetheriteElytraRenderer(CallbackInfo info) {
		addFeature(new NetheriteElytraFeatureRenderer(this));
	}

}

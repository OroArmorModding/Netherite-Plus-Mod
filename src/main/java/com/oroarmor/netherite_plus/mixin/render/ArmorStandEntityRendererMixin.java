package com.oroarmor.netherite_plus.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.client.render.NetheriteElytraFeatureRenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.entity.decoration.ArmorStandEntity;

@Environment(EnvType.CLIENT)
@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin
		extends LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel> {

	public ArmorStandEntityRendererMixin(EntityRenderDispatcher dispatcher, ArmorStandArmorEntityModel model,
			float shadowRadius) {
		super(dispatcher, model, shadowRadius);
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;)V", at = @At("RETURN"))
	private void addNetheriteElytraRenderer(CallbackInfo info) {
		addFeature(new NetheriteElytraFeatureRenderer(this));
	}

}

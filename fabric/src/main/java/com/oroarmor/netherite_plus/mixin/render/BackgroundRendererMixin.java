package com.oroarmor.netherite_plus.mixin.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.oroarmor.netherite_plus.client.NetheritePlusClientMod;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
	@Inject(at = @At("HEAD"), method = "setupFog", cancellable = true)
	private static void applyFog(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, CallbackInfo info) {
		FluidState fluidState = camera.getFluidInCamera();
		Entity entity = camera.getEntity();
		if (fluidState.is(FluidTags.LAVA)) {
			float s;
			float v;
			if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(NetheritePlusStatusEffects.LAVA_VISION.get())) {
				s = 0.0F;
				v = (float) (3.0F + NetheritePlusClientMod.LAVA_VISION_DISTANCE * ((LivingEntity) entity).getEffect(NetheritePlusStatusEffects.LAVA_VISION.get()).getAmplifier());
			} else if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.FIRE_RESISTANCE)) {
				s = 0.0F;
				v = 3.0F;
			} else {
				s = 0.25F;
				v = 1.0F;
			}
			RenderSystem.fogStart(s);
			RenderSystem.fogEnd(v);
			RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
			RenderSystem.setupNvFogDistance();
			info.cancel();
		}

	}

}

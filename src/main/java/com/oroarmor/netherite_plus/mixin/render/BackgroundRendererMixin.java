package com.oroarmor.netherite_plus.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.oroarmor.netherite_plus.client.NetheritePlusClientMod;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	@SuppressWarnings("unused")
	@Inject(at = @At("HEAD"), method = "applyFog", cancellable = true)
	private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance,
			boolean thickFog, CallbackInfo info) {
		FluidState fluidState = camera.getSubmergedFluidState();
		Entity entity = camera.getFocusedEntity();
		if (fluidState.isIn(FluidTags.LAVA)) {
			float s;
			float v;
			if (entity instanceof LivingEntity
					&& ((LivingEntity) entity).hasStatusEffect(NetheritePlusStatusEffects.LAVA_VISION)) {
				s = 0.0F;
				v = (float) (3.0F + NetheritePlusClientMod.LAVA_VISION_DISTANCE * ((LivingEntity) entity)
						.getStatusEffect(NetheritePlusStatusEffects.LAVA_VISION).getAmplifier());
			} else if (entity instanceof LivingEntity
					&& ((LivingEntity) entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
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

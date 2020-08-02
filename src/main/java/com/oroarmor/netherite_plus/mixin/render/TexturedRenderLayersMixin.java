package com.oroarmor.netherite_plus.mixin.render;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.client.NetheritePlusTextures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;

@Environment(EnvType.CLIENT)
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
	@SuppressWarnings("unused")
	@Inject(at = @At("HEAD"), method = "addDefaultTextures(Ljava/util/function/Consumer;)V")
	private static void onAddDefaultTextures(Consumer<SpriteIdentifier> consumer, CallbackInfo info) {
		NetheritePlusTextures.makeAtlases(consumer);
	}
}

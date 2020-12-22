package com.oroarmor.netherite_plus.mixin.render;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.client.NetheritePlusTextures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;

@Environment(EnvType.CLIENT)
@Mixin(Sheets.class)
public class TexturedRenderLayersMixin {
	@Inject(at = @At("HEAD"), method = "getAllMaterials")
	private static void onAddDefaultTextures(Consumer<Material> consumer, CallbackInfo info) {
		NetheritePlusTextures.makeAtlases(consumer);
	}
}

package com.oroarmor.netherite_plus.mixin.render;

import java.util.function.Consumer;

import com.oroarmor.netherite_plus.client.NetheritePlusTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Inject(at = @At("HEAD"), method = "addDefaultTextures")
    private static void onAddDefaultTextures(Consumer<SpriteIdentifier> consumer, CallbackInfo info) {
        NetheritePlusTextures.makeAtlases(consumer);
    }
}

package com.oroarmor.netherite_plus.mixin.render;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrownTrident;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

@Mixin(ThrownTridentRenderer.class)
public class TridentEntityRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ThrownTridentRenderer;getTextureLocation(Lnet/minecraft/world/entity/projectile/ThrownTrident;)Lnet/minecraft/resources/ResourceLocation;"))
    public ResourceLocation getTextureMixin(ThrownTridentRenderer renderer, ThrownTrident entity) {
        return entity.tridentItem.getItem() == NetheritePlusItems.NETHERITE_TRIDENT.get() ? id("textures/entity/netherite_trident.png") : ThrownTridentRenderer.TRIDENT_LOCATION;
    }
}

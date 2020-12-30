package com.oroarmor.netherite_plus.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {

	@Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
	private static void getArmPose(AbstractClientPlayer abstractClientPlayerEntity, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
		ItemStack itemStack = abstractClientPlayerEntity.getItemInHand(hand);
		if (!abstractClientPlayerEntity.swinging && itemStack.getItem() == NetheritePlusItems.NETHERITE_CROSSBOW.get() && CrossbowItem.isCharged(itemStack)) {
			cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
		}
	}

}

package com.oroarmor.netherite_plus.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.oroarmor.netherite_plus.client.render.NetheriteElytraFeatureRenderer;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
		extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	@Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
	private static void getArmPose(AbstractClientPlayerEntity abstractClientPlayerEntity, Hand hand,
			CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
		ItemStack itemStack = abstractClientPlayerEntity.getStackInHand(hand);
		if (!abstractClientPlayerEntity.handSwinging && itemStack.getItem() == NetheritePlusItems.NETHERITE_CROSSBOW
				&& CrossbowItem.isCharged(itemStack)) {
			cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
		}
	}

	public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher,
			PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(dispatcher, model, shadowRadius);
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V", at = @At("RETURN"))
	private void addNetheriteElytraRenderer(CallbackInfo info) {
		addFeature(new NetheriteElytraFeatureRenderer(this));
	}

}

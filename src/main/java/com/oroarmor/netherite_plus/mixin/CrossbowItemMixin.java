package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

	@Redirect(method = "getSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private static Item tickMovement(ItemStack itemStack) {
		return UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
	}

	@SuppressWarnings("unused")
	@Inject(method = "createArrow", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private static void createArrow(World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow,
			CallbackInfoReturnable<PersistentProjectileEntity> cir, ArrowItem arrowItem,
			PersistentProjectileEntity persistentProjectileEntity) {

		if (crossbow.getItem() != NetheritePlusItems.NETHERITE_CROSSBOW) {
			return;
		}

		persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage()
				* NetheritePlusConfig.DAMAGE.CROSSBOW_DAMAGE_MULTIPLIER.getValue()
				+ NetheritePlusConfig.DAMAGE.CROSSBOW_DAMAGE_ADDITION.getValue());
		cir.setReturnValue(persistentProjectileEntity);
	}
}

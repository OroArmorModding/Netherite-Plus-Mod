package com.oroarmor.netherite_elytra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.oroarmor.netherite_elytra.NetheriteElytraMod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Overwrite
	public boolean checkFallFlying() {
		if (!onGround && !isFallFlying() && !isTouchingWater()
				&& !hasStatusEffect(StatusEffects.LEVITATION)) {
			ItemStack itemStack = getEquippedStack(EquipmentSlot.CHEST);
			if (NetheriteElytraMod.isStackUsableAsElytra(itemStack)) {
				startFallFlying();
				return true;
			}
		}
		return false;
	}

	@Shadow
	protected abstract void startFallFlying();
}

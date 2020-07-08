package com.oroarmor.netherite_plus.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.oroarmor.netherite_plus.item.NetheriteElytraItem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Shadow
	private int roll;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Overwrite
	private void initAi() {
		boolean bl = getFlag(7);
		if (bl && !onGround && !hasVehicle() && !hasStatusEffect(StatusEffects.LEVITATION)) {
			ItemStack itemStack = getEquippedStack(EquipmentSlot.CHEST);
			if (NetheriteElytraItem.isStackUsableAsElytra(itemStack)) {
				bl = true;
				if (!world.isClient && (roll + 1) % 20 == 0) {
					itemStack.damage(1, (LivingEntity) (Object) this, (Consumer<LivingEntity>) (livingEntity) -> {
						livingEntity.sendEquipmentBreakStatus(EquipmentSlot.CHEST);
					});
				}
			} else {
				bl = false;
			}
		} else {
			bl = false;
		}

		if (!world.isClient) {
			setFlag(7, bl);
		}
	}

	@Shadow
	protected abstract boolean hasStatusEffect(StatusEffect levitation);

	@Shadow
	protected abstract ItemStack getEquippedStack(EquipmentSlot chest);

}

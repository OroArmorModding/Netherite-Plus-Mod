package net.fabricmc.example.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.fabricmc.example.NetheriteElytraMod;
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
		boolean bl = this.getFlag(7);
		if (bl && !this.onGround && !this.hasVehicle() && !this.hasStatusEffect(StatusEffects.LEVITATION)) {
			ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
			if (NetheriteElytraMod.isStackUsableAsElytra(itemStack)) {
				bl = true;
				if (!this.world.isClient && (this.roll + 1) % 20 == 0) {
					itemStack.damage(1, (LivingEntity) ((Object) this), (Consumer<LivingEntity>) ((livingEntity) -> {
						livingEntity.sendEquipmentBreakStatus(EquipmentSlot.CHEST);
					}));
				}
			} else {
				bl = false;
			}
		} else {
			bl = false;
		}

		if (!this.world.isClient) {
			this.setFlag(7, bl);
		}
	}

	@Shadow
	protected abstract boolean hasStatusEffect(StatusEffect levitation);

	@Shadow
	protected abstract ItemStack getEquippedStack(EquipmentSlot chest);

}

package com.oroarmor.netherite_plus.item;

import com.oroarmor.netherite_plus.advancement.criterion.NetheritePlusCriteria;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.entity.NetheriteTridentEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NetheriteTridentItem extends TridentItem {
	public NetheriteTridentItem(Properties settings) {
		super(settings);
		defaultModifiers.get(Attributes.ATTACK_DAMAGE).forEach(eam -> {
			eam.amount = eam.getAmount() * NetheritePlusConfig.DAMAGE.TRIDENT_DAMAGE_MULTIPLIER.getValue() + NetheritePlusConfig.DAMAGE.TRIDENT_DAMAGE_ADDITION.getValue();
		});
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof Player) {
			Player playerEntity = (Player) user;
			int i = getUseDuration(stack) - remainingUseTicks;
			if (i >= 10) {
				int riptideLevel = EnchantmentHelper.getRiptide(stack);
				if (riptideLevel <= 0 || playerEntity.isInWaterOrRain() || playerEntity.isInLava()) {
					if (!world.isClientSide) {
						stack.hurtAndBreak(1, playerEntity, (p) -> {
							p.broadcastBreakEvent(user.getUsedItemHand());
						});
						if (riptideLevel == 0) {
							NetheriteTridentEntity tridentEntity = new NetheriteTridentEntity(world, playerEntity, stack);
							tridentEntity.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 0.0F, 2.5F + riptideLevel * 0.5F, 1.0F);
							if (playerEntity.abilities.instabuild) {
								tridentEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							}

							world.addFreshEntity(tridentEntity);
							world.playSound(null, tridentEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
							if (!playerEntity.abilities.instabuild) {
								playerEntity.inventory.removeItem(stack);
							}
						} else {
							NetheritePlusCriteria.RIPTIDE_NETHERITE_TRIDENT.trigger((ServerPlayer) playerEntity);
						}
					}

					playerEntity.awardStat(Stats.ITEM_USED.get(this));
					if (riptideLevel > 0) {
						float f = playerEntity.yRot;
						float g = playerEntity.xRot;
						float h = -Mth.sin(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
						float k = -Mth.sin(g * 0.017453292F);
						float l = Mth.cos(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
						float m = Mth.sqrt(h * h + k * k + l * l);
						float n = 3.0F * ((1.0F + riptideLevel) / 4.0F);
						h *= n / m;
						k *= n / m;
						l *= n / m;
						playerEntity.push(h, k, l);
						playerEntity.startAutoSpinAttack(20);
						if (playerEntity.isOnGround()) {
							float o = 1.1999999F;
							playerEntity.move(MoverType.SELF, new Vec3(0.0D, o, 0.0D));
						}

						SoundEvent soundEvent3;
						if (riptideLevel >= 3) {
							soundEvent3 = SoundEvents.TRIDENT_RIPTIDE_3;
						} else if (riptideLevel == 2) {
							soundEvent3 = SoundEvents.TRIDENT_RIPTIDE_2;
						} else {
							soundEvent3 = SoundEvents.TRIDENT_RIPTIDE_1;
						}

						world.playSound(null, playerEntity, soundEvent3, SoundSource.PLAYERS, 1.0F, 1.0F);

					}

				}
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(itemStack);
		} else if (EnchantmentHelper.getRiptide(itemStack) > 0 && !(user.isInWaterOrRain() || user.isInLava())) {
			return InteractionResultHolder.fail(itemStack);
		} else {
			user.startUsingItem(hand);
			return InteractionResultHolder.consume(itemStack);
		}
	}
}

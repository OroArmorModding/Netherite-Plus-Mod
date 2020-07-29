package com.oroarmor.netherite_plus.item;

import java.util.function.Consumer;

import com.oroarmor.netherite_plus.NetheritePlusConfigManager;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

public class NetheriteBowItem extends BowItem {
	public NetheriteBowItem(Settings settings) {
		super(settings);
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity) {
			PlayerEntity playerEntity = (PlayerEntity) user;
			boolean bl = playerEntity.abilities.creativeMode
					|| EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
			ItemStack itemStack = playerEntity.getArrowType(stack);
			if (!itemStack.isEmpty() || bl) {
				if (itemStack.isEmpty()) {
					itemStack = new ItemStack(Items.ARROW);
				}

				int i = getMaxUseTime(stack) - remainingUseTicks;
				float f = getPullProgress(i);
				if (f >= 0.1D) {
					boolean bl2 = bl && itemStack.getItem() == Items.ARROW;
					if (!world.isClient) {
						ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem
								? itemStack.getItem()
								: Items.ARROW);
						PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack,
								playerEntity);
						persistentProjectileEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw,
								0.0F, f * 3.0F, 1.0F);
						if (f == 1.0F) {
							persistentProjectileEntity.setCritical(true);
						}

						int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
						if (j > 0) {
							persistentProjectileEntity
									.setDamage((persistentProjectileEntity.getDamage() + j * 0.5D + 0.5D)
											* NetheritePlusConfigManager.DAMAGE.BOW_DAMAGE_MULTIPLIER.getDoubleValue()
											+ NetheritePlusConfigManager.DAMAGE.BOW_DAMAGE_ADDITION.getDoubleValue());
						}

						int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
						if (k > 0) {
							persistentProjectileEntity.setPunch(k);
						}

						if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
							persistentProjectileEntity.setOnFireFor(100);
						}

						stack.damage(1, (LivingEntity) playerEntity, (Consumer<LivingEntity>) (p) -> {
							p.sendToolBreakStatus(playerEntity.getActiveHand());
						});
						if (bl2 || playerEntity.abilities.creativeMode && (itemStack.getItem() == Items.SPECTRAL_ARROW
								|| itemStack.getItem() == Items.TIPPED_ARROW)) {
							persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
						}

						world.spawnEntity(persistentProjectileEntity);
					}

					world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
							SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
							1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!bl2 && !playerEntity.abilities.creativeMode) {
						itemStack.decrement(1);
						if (itemStack.isEmpty()) {
							playerEntity.inventory.removeOne(itemStack);
						}
					}

					playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
				}
			}
		}
	}
}
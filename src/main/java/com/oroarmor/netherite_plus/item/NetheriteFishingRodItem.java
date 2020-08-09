package com.oroarmor.netherite_plus.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NetheriteFishingRodItem extends FishingRodItem {

	public NetheriteFishingRodItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		int i;
		if (user.fishHook != null) {
			if (!world.isClient) {
				i = user.fishHook.use(itemStack);
				itemStack.damage(i, (LivingEntity) user, (p) -> {
					p.sendToolBreakStatus(hand);
				});
			}

			world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(),
					SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F,
					0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
		} else {
			world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(),
					SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F,
					0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			if (!world.isClient) {
				i = EnchantmentHelper.getLure(itemStack);
				int k = EnchantmentHelper.getLuckOfTheSea(itemStack);
				world.spawnEntity(new FishingBobberEntity(user, world, k, i));
			}

			user.incrementStat(Stats.USED.getOrCreateStat(this));
		}

		return TypedActionResult.method_29237(itemStack, world.isClient());
	}

	@Override
	public int getEnchantability() {
		return 2;
	}

}

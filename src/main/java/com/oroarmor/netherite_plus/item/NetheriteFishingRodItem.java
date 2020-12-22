package com.oroarmor.netherite_plus.item;

import com.oroarmor.netherite_plus.entity.NetheriteFishingBobberEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class NetheriteFishingRodItem extends FishingRodItem {

	public NetheriteFishingRodItem(Properties settings) {
		super(settings);
	}

	@Override
	public int getEnchantmentValue() {
		return 2;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		if (user.fishing != null) {
			if (!world.isClientSide) {
				int fishingLevelUsage = user.fishing.retrieve(itemStack);
				itemStack.hurtAndBreak(fishingLevelUsage, user, p -> p.broadcastBreakEvent(hand));
			}

			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		} else {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
			if (!world.isClientSide) {
				int lureLevel = EnchantmentHelper.getFishingSpeedBonus(itemStack);
				int luckOfTheSeaLevel = EnchantmentHelper.getFishingLuckBonus(itemStack);
				world.addFreshEntity(new NetheriteFishingBobberEntity(user, world, luckOfTheSeaLevel, lureLevel));
			}

			user.awardStat(Stats.ITEM_USED.get(this));
		}

		return InteractionResultHolder.success(itemStack);
	}

}

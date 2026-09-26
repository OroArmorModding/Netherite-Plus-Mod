/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        } else {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
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

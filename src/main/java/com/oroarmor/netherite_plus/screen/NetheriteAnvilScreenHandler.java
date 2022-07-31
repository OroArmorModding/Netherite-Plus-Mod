/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.screen;

import java.util.Iterator;
import java.util.Map;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;

public class NetheriteAnvilScreenHandler extends ForgingScreenHandler {
    public final Property levelCost;
    public int repairItemUsage;
    private String newItemName;

    public NetheriteAnvilScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public NetheriteAnvilScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(NetheritePlusScreenHandlers.NETHERITE_ANVIL, syncId, inventory, context);
        levelCost = Property.create();
        addProperty(levelCost);
    }

    public static int getNextCost(int cost) {
        return cost * 2 + 1;
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return (player.getAbilities().creativeMode || player.experienceLevel >= levelCost.get()) && levelCost.get() > 0;
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK);
    }

    public int getLevelCost() {
        return levelCost.get();
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        if (!player.getAbilities().creativeMode) {
            player.addExperienceLevels(-levelCost.get());
        }

        input.setStack(0, ItemStack.EMPTY);
        if (repairItemUsage > 0) {
            ItemStack itemStack = input.getStack(1);
            if (!itemStack.isEmpty() && itemStack.getCount() > repairItemUsage) {
                itemStack.decrement(repairItemUsage);
                input.setStack(1, itemStack);
            } else {
                input.setStack(1, ItemStack.EMPTY);
            }
        } else {
            input.setStack(1, ItemStack.EMPTY);
        }

        context.run((world, blockPos) -> {
            world.syncWorldEvent(1030, blockPos, 0);
        });

        levelCost.set(0);
    }

    public void setNewItemName(String string) {
        newItemName = string;
        if (getSlot(2).hasStack()) {
            ItemStack itemStack = getSlot(2).getStack();
            if (StringUtils.isBlank(string)) {
                itemStack.removeCustomName();
            } else {
                itemStack.setCustomName(Text.literal(newItemName));
            }
        }

        updateResult();
    }

    @Override
    public void updateResult() {
        ItemStack itemStack = input.getStack(0);
        levelCost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemStack.isEmpty()) {
            output.setStack(0, ItemStack.EMPTY);
            levelCost.set(0);
        } else {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = input.getStack(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack2);
            j = j + itemStack.getRepairCost() + (itemStack3.isEmpty() ? 0 : itemStack3.getRepairCost());
            repairItemUsage = 0;
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantmentNbt(itemStack3).isEmpty();
                int o;
                int p;
                int q;
                if (itemStack2.isDamageable() && itemStack2.getItem().canRepair(itemStack, itemStack3)) {
                    o = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    if (o <= 0) {
                        output.setStack(0, ItemStack.EMPTY);
                        levelCost.set(0);
                        return;
                    }

                    for (p = 0; o > 0 && p < itemStack3.getCount(); ++p) {
                        q = itemStack2.getDamage() - o;
                        itemStack2.setDamage(q);
                        ++i;
                        o = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    }

                    repairItemUsage = p;
                } else {
                    if (!bl && (itemStack2.getItem() != itemStack3.getItem() || !itemStack2.isDamageable())) {
                        output.setStack(0, ItemStack.EMPTY);
                        levelCost.set(0);
                        return;
                    }

                    if (itemStack2.isDamageable() && !bl) {
                        o = itemStack.getMaxDamage() - itemStack.getDamage();
                        p = itemStack3.getMaxDamage() - itemStack3.getDamage();
                        q = p + itemStack2.getMaxDamage() * 12 / 100;
                        int r = o + q;
                        int s = itemStack2.getMaxDamage() - r;
                        if (s < 0) {
                            s = 0;
                        }

                        if (s < itemStack2.getDamage()) {
                            itemStack2.setDamage(s);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map2 = EnchantmentHelper.get(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;
                    Iterator<Enchantment> var24 = map2.keySet().iterator();

                    label155:
                    while (true) {
                        Enchantment enchantment;
                        do {
                            if (!var24.hasNext()) {
                                if (bl3 && !bl2) {
                                    output.setStack(0, ItemStack.EMPTY);
                                    levelCost.set(0);
                                    return;
                                }
                                break label155;
                            }

                            enchantment = var24.next();
                        } while (enchantment == null);

                        int t = map.getOrDefault(enchantment, 0);
                        int u = map2.get(enchantment);
                        u = t == u ? u + 1 : Math.max(u, t);
                        boolean bl4 = enchantment.isAcceptableItem(itemStack);
                        if (player.getAbilities().creativeMode || itemStack.getItem() == Items.ENCHANTED_BOOK) {
                            bl4 = true;
                        }

                        for (Enchantment enchantment2 : map.keySet()) {
                            if (enchantment2 != enchantment && !enchantment.canCombine(enchantment2)) {
                                bl4 = false;
                                ++i;
                            }
                        }

                        if (!bl4) {
                            bl3 = true;
                        } else {
                            bl2 = true;
                            if (u > enchantment.getMaxLevel()) {
                                u = enchantment.getMaxLevel();
                            }

                            map.put(enchantment, u);
                            int v = switch (enchantment.getRarity()) {
                                case COMMON -> 1;
                                case UNCOMMON -> 2;
                                case RARE -> 4;
                                case VERY_RARE -> 8;
                            };

                            if (bl) {
                                v = Math.max(1, v / 2);
                            }

                            i += v * u;
                            if (itemStack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }
                }
            }

            if (StringUtils.isBlank(newItemName)) {
                if (itemStack.hasCustomName()) {
                    k = 1;
                    i += k;
                    itemStack2.removeCustomName();
                }
            } else if (!newItemName.equals(itemStack.getName().getString())) {
                k = 1;
                i += k;
                itemStack2.setCustomName(Text.literal(newItemName));
            }

            // this is the important line that changes things
            double cost = (1d - NetheritePlusMod.CONFIG.anvil.xp_reduction) * (j + i);

            levelCost.set(cost < 1 ? 1 : (int) cost);
            if (i <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && levelCost.get() >= 40) {
                levelCost.set(39);
            }

            if (levelCost.get() >= 40 && !player.getAbilities().creativeMode) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int w = itemStack2.getRepairCost();
                if (!itemStack3.isEmpty() && w < itemStack3.getRepairCost()) {
                    w = itemStack3.getRepairCost();
                }

                if (k != i || k == 0) {
                    w = getNextCost(w);
                }

                itemStack2.setRepairCost(w);
                EnchantmentHelper.set(map, itemStack2);
            }

            output.setStack(0, itemStack2);
            sendContentUpdates();
        }
    }
}

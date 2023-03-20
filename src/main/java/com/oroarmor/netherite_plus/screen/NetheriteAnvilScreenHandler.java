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

package com.oroarmor.netherite_plus.screen;

import java.util.Iterator;
import java.util.Map;

import com.mojang.logging.LogUtils;
import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import org.apache.commons.lang3.StringUtils;
import org.quiltmc.loader.api.QuiltLoader;
import org.slf4j.Logger;

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
import net.minecraft.unmapped.C_vkmtnvmw;
import net.minecraft.world.WorldEvents;

public class NetheriteAnvilScreenHandler extends ForgingScreenHandler {
    public static final int INPUT_SLOT = 0;
    public static final int ADDITION_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    public static final int MAX_NAME_LENGTH = 50;
    private int repairItemUsage;
    private String newItemName;
    private final Property levelCost = Property.create();
    private static final int FAIL_COST = 0;
    private static final int BASE_COST = 1;
    private static final int ADDED_BASE_COST = 1;
    private static final int MATERIAL_REPAIR_COST = 1;
    private static final int SACRIFICE_REPAIR_COST = 2;
    private static final int INCOMPATIBLE_PENALTY_COST = 1;
    private static final int RENAME_COST = 1;
    private static final int field_41894 = 27;
    private static final int field_41895 = 76;
    private static final int field_41896 = 134;
    private static final int field_41897 = 47;

    public NetheriteAnvilScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public NetheriteAnvilScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(NetheritePlusScreenHandlers.NETHERITE_ANVIL, syncId, inventory, context);
        this.addProperty(levelCost);
    }

    protected C_vkmtnvmw method_48352() {
        return C_vkmtnvmw.method_48364()
                .method_48374(0, 27, 47, stack -> true)
                .method_48374(1, 76, 47, stack -> true)
                .method_48373(2, 134, 47)
                .method_48372();
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
            player.addExperienceLevels(-this.levelCost.get());
        }

        input.setStack(INPUT_SLOT, ItemStack.EMPTY);
        if (repairItemUsage > 0) {
            ItemStack additionStack = input.getStack(ADDITION_SLOT);
            if (!additionStack.isEmpty() && additionStack.getCount() > repairItemUsage) {
                additionStack.decrement(repairItemUsage);
                input.setStack(ADDITION_SLOT, additionStack);
            } else {
                input.setStack(ADDITION_SLOT, ItemStack.EMPTY);
            }
        } else {
            input.setStack(ADDITION_SLOT, ItemStack.EMPTY);
        }

        levelCost.set(0);
        context.run((world, blockPos) -> world.syncWorldEvent(WorldEvents.ANVIL_USED, blockPos, 0));
    }

    public void setNewItemName(String string) {
        newItemName = string;
        if (getSlot(RESULT_SLOT).hasStack()) {
            ItemStack itemStack = getSlot(RESULT_SLOT).getStack();
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
        ItemStack inputStack = input.getStack(INPUT_SLOT);
        levelCost.set(BASE_COST);
        if (inputStack.isEmpty()) {
            output.setStack(INPUT_SLOT, ItemStack.EMPTY);
            levelCost.set(FAIL_COST);
        } else {
            ItemStack copiedInput = inputStack.copy();
            ItemStack addition = input.getStack(ADDITION_SLOT);
            Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.get(copiedInput);
            int repairCost = inputStack.getRepairCost() + (addition.isEmpty() ? FAIL_COST : addition.getRepairCost());
            this.repairItemUsage = 0;
            int uses = 0;
            int isRename = 0;
            if (!addition.isEmpty()) {
                boolean addingEnchantmentBook = addition.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantmentNbt(addition).isEmpty();
                if (copiedInput.isDamageable() && copiedInput.getItem().canRepair(inputStack, addition)) {
                    int additionRepairAmount = Math.min(copiedInput.getDamage(), copiedInput.getMaxDamage() / 4);
                    if (additionRepairAmount <= 0) {
                        output.setStack(0, ItemStack.EMPTY);
                        levelCost.set(FAIL_COST);
                        return;
                    }

                    int repairs = 0;
                    for (; additionRepairAmount > 0 && repairs < addition.getCount(); ++repairs) {
                        int newDamage = copiedInput.getDamage() - additionRepairAmount;
                        copiedInput.setDamage(newDamage);
                        ++uses;
                        additionRepairAmount = Math.min(copiedInput.getDamage(), copiedInput.getMaxDamage() / 4);
                    }

                    repairItemUsage = repairs;
                } else {
                    if (!addingEnchantmentBook && (copiedInput.getItem() != addition.getItem() || !copiedInput.isDamageable())) {
                        output.setStack(0, ItemStack.EMPTY);
                        levelCost.set(0);
                        return;
                    }

                    if (copiedInput.isDamageable() && !addingEnchantmentBook) {
                        int inputDamage = inputStack.getMaxDamage() - inputStack.getDamage();
                        int additionDamage = addition.getMaxDamage() - addition.getDamage();
                        int addedDamage = additionDamage + copiedInput.getMaxDamage() * 12 / 100;
                        int combinedDamage = inputDamage + addedDamage;
                        int newDamage = copiedInput.getMaxDamage() - combinedDamage;
                        if (newDamage < 0) {
                            newDamage = 0;
                        }

                        if (newDamage < copiedInput.getDamage()) {
                            copiedInput.setDamage(newDamage);
                            uses += 2;
                        }
                    }

                    Map<Enchantment, Integer> addedEnchantments = EnchantmentHelper.get(addition);
                    boolean addedAnyEnchantment = false;
                    boolean failedEnchantmentAdded = false;

                    for(Enchantment addedEnchantment : addedEnchantments.keySet()) {
                        if (addedEnchantment != null) {
                            int currentLevel = currentEnchantments.getOrDefault(addedEnchantment, 0);
                            int addedLevel = addedEnchantments.get(addedEnchantment);
                            addedLevel = currentLevel == addedLevel ? addedLevel + 1 : Math.max(addedLevel, currentLevel);
                            boolean canAddEnchantment = addedEnchantment.isAcceptableItem(inputStack);
                            if (this.player.getAbilities().creativeMode || inputStack.isOf(Items.ENCHANTED_BOOK)) {
                                canAddEnchantment = true;
                            }

                            for(Enchantment currentEnchantment : currentEnchantments.keySet()) {
                                if (currentEnchantment != addedEnchantment && !currentEnchantment.canCombine(addedEnchantment)) {
                                    canAddEnchantment = false;
                                    ++uses;
                                }
                            }

                            if (!canAddEnchantment) {
                                failedEnchantmentAdded = true;
                            } else {
                                addedAnyEnchantment = true;
                                if (addedLevel > addedEnchantment.getMaxLevel()) {
                                    addedLevel = addedEnchantment.getMaxLevel();
                                }

                                currentEnchantments.put(addedEnchantment, addedLevel);
                                int rarityCost = switch (addedEnchantment.getRarity()) {
                                    case COMMON -> 1;
                                    case UNCOMMON -> 2;
                                    case RARE -> 4;
                                    case VERY_RARE -> 8;
                                };

                                rarityCost = Math.max(1, rarityCost / 2);

                                uses += rarityCost * addedLevel;
                                if (inputStack.getCount() > 1) {
                                    uses = 40;
                                }
                            }
                        }
                    }

                    if (failedEnchantmentAdded && !addedAnyEnchantment) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(newItemName)) {
                if (inputStack.hasCustomName()) {
                    isRename = 1;
                    uses += isRename;
                    copiedInput.removeCustomName();
                }
            } else if (!newItemName.equals(inputStack.getName().getString())) {
                isRename = 1;
                uses += isRename;
                copiedInput.setCustomName(Text.literal(newItemName));
            }

            // this is the important line that changes things
            double cost = (1d - NetheritePlusMod.CONFIG.anvil.xp_reduction) * (repairCost + uses);

            levelCost.set(cost < BASE_COST ? BASE_COST : (int) cost);
            if (uses <= 0) {
                copiedInput = ItemStack.EMPTY;
            }

            if (isRename == uses && isRename > 0 && levelCost.get() >= 40) {
                levelCost.set(39);
            }

            if (levelCost.get() >= 40 && !player.getAbilities().creativeMode) {
                copiedInput = ItemStack.EMPTY;
            }

            if (!copiedInput.isEmpty()) {
                int copiedRepairCost = copiedInput.getRepairCost();
                if (!addition.isEmpty() && copiedRepairCost < addition.getRepairCost()) {
                    copiedRepairCost = addition.getRepairCost();
                }

                if (isRename != uses || isRename == 0) {
                    copiedRepairCost = getNextCost(copiedRepairCost);
                }

                copiedInput.setRepairCost(copiedRepairCost);
                EnchantmentHelper.set(currentEnchantments, copiedInput);
            }

            output.setStack(0, copiedInput);
            sendContentUpdates();
        }
    }
}

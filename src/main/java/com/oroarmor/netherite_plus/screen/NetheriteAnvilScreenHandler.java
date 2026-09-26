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

import java.util.Map;

import com.mojang.logging.LogUtils;
import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

public class NetheriteAnvilScreenHandler extends ItemCombinerMenu {
    public static final int INGREDIENT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    public static final int MAX_NAME_LENGTH = 50;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final boolean DEBUG_COST = false;
    private static final int FAIL_COST = 0;
    private static final int BASE_COST = 1;
    private static final int ADDED_BASE_COST = 1;
    private static final int MATERIAL_REPAIR_COST = 1;
    private static final int SACRIFICE_REPAIR_COST = 2;
    private static final int INCOMPATIBLE_PENALTY_COST = 1;
    private static final int RENAME_COST = 1;
    private static final int INGREDIENT_SLOT_X = 27;
    private static final int ADDITIONAL_SLOT_X = 76;
    private static final int RESULT_SLOT_X = 134;
    private static final int SLOT_Y = 47;
    private final DataSlot levelCost = DataSlot.standalone();
    private int repairItemUsage;
    @Nullable
    private String newItemName;

    public NetheriteAnvilScreenHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, ContainerLevelAccess.NULL);
    }

    public NetheriteAnvilScreenHandler(int syncId, Inventory inventory, ContainerLevelAccess context) {
        super(NetheritePlusScreenHandlers.NETHERITE_ANVIL, syncId, inventory, context);
        this.addDataSlot(levelCost);
    }

    public static int getNextCost(int cost) {
        return cost * 2 + 1;
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, stack -> true)
                .withSlot(1, 76, 47, stack -> true)
                .withResultSlot(2, 134, 47)
                .build();
    }

    @Override
    protected boolean mayPickup(Player player, boolean present) {
        return (player.getAbilities().instabuild || player.experienceLevel >= levelCost.get()) && levelCost.get() > 0;
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK);
    }

    public int getLevelCost() {
        return levelCost.get();
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-this.levelCost.get());
        }

        this.inputSlots.setItem(INGREDIENT_SLOT, ItemStack.EMPTY);
        if (repairItemUsage > 0) {
            ItemStack additionStack = this.inputSlots.getItem(ADDITIONAL_SLOT);
            if (!additionStack.isEmpty() && additionStack.getCount() > repairItemUsage) {
                additionStack.shrink(repairItemUsage);
                this.inputSlots.setItem(ADDITIONAL_SLOT, additionStack);
            } else {
                this.inputSlots.setItem(ADDITIONAL_SLOT, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(ADDITIONAL_SLOT, ItemStack.EMPTY);
        }

        levelCost.set(0);
        access.execute((world, blockPos) -> world.levelEvent(LevelEvent.SOUND_ANVIL_USED, blockPos, 0));
    }

    public void setNewItemName(String string) {
        newItemName = string;
        if (getSlot(RESULT_SLOT).hasItem()) {
            ItemStack itemStack = getSlot(RESULT_SLOT).getItem();
            if (StringUtils.isBlank(string)) {
                itemStack.resetHoverName();
            } else {
                itemStack.setHoverName(Component.literal(newItemName));
            }
        }

        createResult();
    }

    @Override
    public void createResult() {
        ItemStack inputStack = this.inputSlots.getItem(INGREDIENT_SLOT);
        levelCost.set(BASE_COST);
        if (inputStack.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            levelCost.set(FAIL_COST);
        } else {
            ItemStack copiedInput = inputStack.copy();
            ItemStack addition = this.inputSlots.getItem(ADDITIONAL_SLOT);
            Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.getEnchantments(copiedInput);
            int repairCost = inputStack.getBaseRepairCost() + (addition.isEmpty() ? FAIL_COST : addition.getBaseRepairCost());
            this.repairItemUsage = 0;
            int uses = 0;
            int isRename = 0;
            if (!addition.isEmpty()) {
                boolean addingEnchantmentBook = addition.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(addition).isEmpty();
                if (copiedInput.isDamageableItem() && copiedInput.getItem().isValidRepairItem(inputStack, addition)) {
                    int additionRepairAmount = Math.min(copiedInput.getDamageValue(), copiedInput.getMaxDamage() / 4);
                    if (additionRepairAmount <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        levelCost.set(FAIL_COST);
                        return;
                    }

                    int repairs = 0;
                    for (; additionRepairAmount > 0 && repairs < addition.getCount(); ++repairs) {
                        int newDamage = copiedInput.getDamageValue() - additionRepairAmount;
                        copiedInput.setDamageValue(newDamage);
                        ++uses;
                        additionRepairAmount = Math.min(copiedInput.getDamageValue(), copiedInput.getMaxDamage() / 4);
                    }

                    repairItemUsage = repairs;
                } else {
                    if (!addingEnchantmentBook && (copiedInput.getItem() != addition.getItem() || !copiedInput.isDamageableItem())) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        levelCost.set(0);
                        return;
                    }

                    if (copiedInput.isDamageableItem() && !addingEnchantmentBook) {
                        int inputDamage = inputStack.getMaxDamage() - inputStack.getDamageValue();
                        int additionDamage = addition.getMaxDamage() - addition.getDamageValue();
                        int addedDamage = additionDamage + copiedInput.getMaxDamage() * 12 / 100;
                        int combinedDamage = inputDamage + addedDamage;
                        int newDamage = copiedInput.getMaxDamage() - combinedDamage;
                        if (newDamage < 0) {
                            newDamage = 0;
                        }

                        if (newDamage < copiedInput.getDamageValue()) {
                            copiedInput.setDamageValue(newDamage);
                            uses += 2;
                        }
                    }

                    Map<Enchantment, Integer> addedEnchantments = EnchantmentHelper.getEnchantments(addition);
                    boolean addedAnyEnchantment = false;
                    boolean failedEnchantmentAdded = false;

                    for (Enchantment addedEnchantment : addedEnchantments.keySet()) {
                        if (addedEnchantment != null) {
                            int currentLevel = currentEnchantments.getOrDefault(addedEnchantment, 0);
                            int addedLevel = addedEnchantments.get(addedEnchantment);
                            addedLevel = currentLevel == addedLevel ? addedLevel + 1 : Math.max(addedLevel, currentLevel);
                            boolean canAddEnchantment = addedEnchantment.canEnchant(inputStack);
                            if (this.player.getAbilities().instabuild || inputStack.is(Items.ENCHANTED_BOOK)) {
                                canAddEnchantment = true;
                            }

                            for (Enchantment currentEnchantment : currentEnchantments.keySet()) {
                                if (currentEnchantment != addedEnchantment && !currentEnchantment.isCompatibleWith(addedEnchantment)) {
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
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(newItemName)) {
                if (inputStack.hasCustomHoverName()) {
                    isRename = 1;
                    uses += isRename;
                    copiedInput.resetHoverName();
                }
            } else if (!newItemName.equals(inputStack.getHoverName().getString())) {
                isRename = 1;
                uses += isRename;
                copiedInput.setHoverName(Component.literal(newItemName));
            }

            // this is the important line that changes things
            double cost = (1d - NetheritePlusMod.CONFIG.anvil.xp_reduction.value()) * (repairCost + uses);

            levelCost.set(cost < BASE_COST ? BASE_COST : (int) cost);
            if (uses <= 0) {
                copiedInput = ItemStack.EMPTY;
            }

            if (isRename == uses && isRename > 0 && levelCost.get() >= 40) {
                levelCost.set(39);
            }

            if (levelCost.get() >= 40 && !player.getAbilities().instabuild) {
                copiedInput = ItemStack.EMPTY;
            }

            if (!copiedInput.isEmpty()) {
                int copiedRepairCost = copiedInput.getBaseRepairCost();
                if (!addition.isEmpty() && copiedRepairCost < addition.getBaseRepairCost()) {
                    copiedRepairCost = addition.getBaseRepairCost();
                }

                if (isRename != uses || isRename == 0) {
                    copiedRepairCost = getNextCost(copiedRepairCost);
                }

                copiedInput.setRepairCost(copiedRepairCost);
                EnchantmentHelper.setEnchantments(currentEnchantments, copiedInput);
            }

            this.resultSlots.setItem(0, copiedInput);
            broadcastChanges();
        }
    }
}

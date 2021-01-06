package com.oroarmor.netherite_plus.screen;

import java.util.Iterator;
import java.util.Map;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteAnvilScreenHandler extends ItemCombinerMenu {
    private final DataSlot levelCost;
    private int repairItemUsage;
    private String newItemName;

    public NetheriteAnvilScreenHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, ContainerLevelAccess.NULL);
    }

    public NetheriteAnvilScreenHandler(int syncId, Inventory inventory, ContainerLevelAccess context) {
        super(NetheritePlusScreenHandlers.NETHERITE_ANVIL, syncId, inventory, context);
        levelCost = DataSlot.standalone();
        addDataSlot(levelCost);
    }

    public static int getNextCost(int cost) {
        return cost * 2 + 1;
    }

    @Override
    protected boolean mayPickup(Player player, boolean present) {
        return (player.abilities.instabuild || player.experienceLevel >= levelCost.get()) && levelCost.get() > 0;
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK.get());
    }

    @Environment(EnvType.CLIENT)
    public int getLevelCost() {
        return levelCost.get();
    }

    @Override
    protected ItemStack onTake(Player player, ItemStack stack) {
        if (!player.abilities.instabuild) {
            player.giveExperienceLevels(-levelCost.get());
        }

        inputSlots.setItem(0, ItemStack.EMPTY);
        if (repairItemUsage > 0) {
            ItemStack itemStack = inputSlots.getItem(1);
            if (!itemStack.isEmpty() && itemStack.getCount() > repairItemUsage) {
                itemStack.shrink(repairItemUsage);
                inputSlots.setItem(1, itemStack);
            } else {
                inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else {
            inputSlots.setItem(1, ItemStack.EMPTY);
        }

        access.execute((world, blockPos) -> {
            world.levelEvent(1030, blockPos, 0);
        });

        levelCost.set(0);
        return stack;

    }

    public void setNewItemName(String string) {
        newItemName = string;
        if (getSlot(2).hasItem()) {
            ItemStack itemStack = getSlot(2).getItem();
            if (StringUtils.isBlank(string)) {
                itemStack.resetHoverName();
            } else {
                itemStack.setHoverName(new TextComponent(newItemName));
            }
        }

        createResult();
    }

    @Override
    public void createResult() {
        ItemStack itemStack = inputSlots.getItem(0);
        levelCost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemStack.isEmpty()) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            levelCost.set(0);
        } else {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = inputSlots.getItem(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack2);
            j = j + itemStack.getBaseRepairCost() + (itemStack3.isEmpty() ? 0 : itemStack3.getBaseRepairCost());
            repairItemUsage = 0;
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(itemStack3).isEmpty();
                int o;
                int p;
                int q;
                if (itemStack2.isDamageableItem() && itemStack2.getItem().isValidRepairItem(itemStack, itemStack3)) {
                    o = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    if (o <= 0) {
                        resultSlots.setItem(0, ItemStack.EMPTY);
                        levelCost.set(0);
                        return;
                    }

                    for (p = 0; o > 0 && p < itemStack3.getCount(); ++p) {
                        q = itemStack2.getDamageValue() - o;
                        itemStack2.setDamageValue(q);
                        ++i;
                        o = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    }

                    repairItemUsage = p;
                } else {
                    if (!bl && (itemStack2.getItem() != itemStack3.getItem() || !itemStack2.isDamageableItem())) {
                        resultSlots.setItem(0, ItemStack.EMPTY);
                        levelCost.set(0);
                        return;
                    }

                    if (itemStack2.isDamageableItem() && !bl) {
                        o = itemStack.getMaxDamage() - itemStack.getDamageValue();
                        p = itemStack3.getMaxDamage() - itemStack3.getDamageValue();
                        q = p + itemStack2.getMaxDamage() * 12 / 100;
                        int r = o + q;
                        int s = itemStack2.getMaxDamage() - r;
                        if (s < 0) {
                            s = 0;
                        }

                        if (s < itemStack2.getDamageValue()) {
                            itemStack2.setDamageValue(s);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map2 = EnchantmentHelper.getEnchantments(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;
                    Iterator<Enchantment> var24 = map2.keySet().iterator();

                    label155:
                    while (true) {
                        Enchantment enchantment;
                        do {
                            if (!var24.hasNext()) {
                                if (bl3 && !bl2) {
                                    resultSlots.setItem(0, ItemStack.EMPTY);
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
                        boolean bl4 = enchantment.canEnchant(itemStack);
                        if (player.abilities.instabuild || itemStack.getItem() == Items.ENCHANTED_BOOK) {
                            bl4 = true;
                        }

                        Iterator<Enchantment> var17 = map.keySet().iterator();

                        while (var17.hasNext()) {
                            Enchantment enchantment2 = var17.next();
                            if (enchantment2 != enchantment && !enchantment.isCompatibleWith(enchantment2)) {
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
                            int v = 0;
                            switch (enchantment.getRarity()) {
                                case COMMON:
                                    v = 1;
                                    break;
                                case UNCOMMON:
                                    v = 2;
                                    break;
                                case RARE:
                                    v = 4;
                                    break;
                                case VERY_RARE:
                                    v = 8;
                            }

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
                if (itemStack.hasCustomHoverName()) {
                    k = 1;
                    i += k;
                    itemStack2.resetHoverName();
                }
            } else if (!newItemName.equals(itemStack.getHoverName().getString())) {
                k = 1;
                i += k;
                itemStack2.setHoverName(new TextComponent(newItemName));
            }

            // this is the important line that changes things
            double cost = (1d - NetheritePlusConfig.ANVIL.XP_REDUCTION.getValue()) * (j + i);

            levelCost.set(cost < 1 ? 1 : (int) cost);
            if (i <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && levelCost.get() >= 40) {
                levelCost.set(39);
            }

            if (levelCost.get() >= 40 && !player.abilities.instabuild) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int w = itemStack2.getBaseRepairCost();
                if (!itemStack3.isEmpty() && w < itemStack3.getBaseRepairCost()) {
                    w = itemStack3.getBaseRepairCost();
                }

                if (k != i || k == 0) {
                    w = getNextCost(w);
                }

                itemStack2.setRepairCost(w);
                EnchantmentHelper.setEnchantments(map, itemStack2);
            }

            resultSlots.setItem(0, itemStack2);
            broadcastChanges();
        }
    }
}

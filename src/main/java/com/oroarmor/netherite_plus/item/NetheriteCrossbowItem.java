package com.oroarmor.netherite_plus.item;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

public class NetheriteCrossbowItem extends CrossbowItem {
    public NetheriteCrossbowItem(Settings settings) {
        super(settings);
    }
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }
}

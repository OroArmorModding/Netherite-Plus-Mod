package com.oroarmor.netherite_plus.item;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public class NetheriteCrossbowItem extends CrossbowItem {
    public NetheriteCrossbowItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return stack.getItem() == NetheritePlusItems.NETHERITE_CROSSBOW.get();
    }

}

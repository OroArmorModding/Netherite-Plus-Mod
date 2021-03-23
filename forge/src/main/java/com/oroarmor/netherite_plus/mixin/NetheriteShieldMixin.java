package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.item.NetheriteShieldItem;
import javax.annotation.Nullable;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(NetheriteShieldItem.class)
public class NetheriteShieldMixin extends Item implements IForgeItem {
    private NetheriteShieldMixin(Settings properties) {
        super(properties);
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.OFFHAND;
    }
}

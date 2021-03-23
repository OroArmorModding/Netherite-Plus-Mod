package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheriteShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

@Mixin(NetheriteShieldItem.class)
public class NetheriteShieldMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"))
    private static Item.Settings editSettings(Item.Settings settings) {
        return new FabricItemSettings().maxDamage(NetheritePlusConfig.DURABILITIES.SHIELD_DURABILITY.getValue()).group(ItemGroup.COMBAT).fireproof().equipmentSlot(stack -> EquipmentSlot.OFFHAND);
    }
}

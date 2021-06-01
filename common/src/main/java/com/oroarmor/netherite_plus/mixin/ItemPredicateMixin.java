package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;

@Mixin(ItemPredicate.class)
public class ItemPredicateMixin {
    @Shadow
    @Final
    @Nullable
    private Item item;
    @ModifyVariable(method = "test", at = @At("HEAD"))
    public ItemStack letNetheriteShearsCountAsShears(ItemStack stack) {
        if(item != null && NetheritePlusConfig.ENABLED.ENABLED_SHEARS.getValue() && stack.getItem() == NetheritePlusItems.NETHERITE_SHEARS.get()) {
            ItemStack itemStack = new ItemStack(Items.SHEARS);
            itemStack.setCount(stack.getCount());
            itemStack.setTag(stack.getOrCreateTag());
            return itemStack;
        }
        return stack;
    }
}

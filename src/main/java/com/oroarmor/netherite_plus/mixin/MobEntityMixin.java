package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.oroarmor.netherite_plus.item.NetheriteElytraItem;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Overwrite
	public static EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
		Item item = stack.getItem();
		if (item != Blocks.CARVED_PUMPKIN.asItem()
				&& (!(item instanceof BlockItem) || !(((BlockItem) item).getBlock() instanceof AbstractSkullBlock))) {
			if (item instanceof ArmorItem) {
				return ((ArmorItem) item).getSlotType();
			} else if (NetheriteElytraItem.isElytra(stack)) {
				return EquipmentSlot.CHEST;
			} else {
				return item == Items.SHIELD ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
			}
		}
		return EquipmentSlot.HEAD;
	}
}

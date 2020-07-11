package com.oroarmor.netherite_plus.item;

import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

public enum NetheriteElytraArmorMaterials implements ArmorMaterial {
	NETHERITE_ELYTRA_MATERIAL("netherite", 37, new int[] { 3, 6, 4, 3 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
			3.0F, 0.1F, () -> {
				return Ingredient.ofItems(Items.NETHERITE_INGOT);
			});

	private static final int[] BASE_DURABILITY = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Lazy<Ingredient> repairIngredientSupplier;

	private NetheriteElytraArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts,
			int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance,
			Supplier<Ingredient> supplier) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		repairIngredientSupplier = new Lazy<>(supplier);
	}

	@Override
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * durabilityMultiplier;
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return protectionAmounts[slot.getEntitySlotId()];
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredientSupplier.get();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return knockbackResistance;
	}
}

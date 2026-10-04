/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.world.item;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Map;

import com.google.common.collect.Maps;
import com.oroarmor.netherite_plus.NetheritePlusMod;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public interface NetheritePlusElytraComponents {
    ArmorMaterial NETHERITE_ELYTRA_ARMOR_MATERIAL = new ArmorMaterial(
            ArmorMaterials.NETHERITE.durability(),
            Maps.newEnumMap(Map.of(ArmorType.CHESTPLATE, NetheritePlusMod.CONFIG.damage.elytraArmorPoints)),
            ArmorMaterials.NETHERITE.enchantmentValue(),
            ArmorMaterials.NETHERITE.equipSound(),
            ArmorMaterials.NETHERITE.toughness(),
            ArmorMaterials.NETHERITE.knockbackResistance(),
            ArmorMaterials.NETHERITE.repairIngredient(),
            ArmorMaterials.NETHERITE.assetId()
    );

    ResourceKey<EquipmentAsset> NETHERITE_ELYTRA_EQUIPMENT_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, id("netherite_elytra"));
}

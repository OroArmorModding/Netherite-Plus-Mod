package com.oroarmor.netherite_plus.item;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.util.Identifier;

public class NetheriteHorseArmorItem extends HorseArmorItem {

	public NetheriteHorseArmorItem(int bonus, Settings settings) {
		super(bonus, "diamond", settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Identifier getEntityTexture() {
		return id("textures/entity/netherite_horse_armor.png");
	}
}

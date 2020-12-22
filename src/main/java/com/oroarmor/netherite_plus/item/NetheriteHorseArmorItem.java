package com.oroarmor.netherite_plus.item;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;

public class NetheriteHorseArmorItem extends HorseArmorItem {

	public NetheriteHorseArmorItem(int bonus, Properties settings) {
		super(bonus, "diamond", settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ResourceLocation getTexture() {
		return id("textures/entity/netherite_horse_armor.png");
	}
}

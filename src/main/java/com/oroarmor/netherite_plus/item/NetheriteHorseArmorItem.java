package com.oroarmor.netherite_plus.item;

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
      return new Identifier("netherite_plus", "textures/entity/netherite_horse_armor.png");
   }
}

package com.oroarmor.netherite_plus.forge;

import com.oroarmor.netherite_plus.client.ForgeNetheritePlusModClient;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class NetheritePlusModPlatformImpl {
	public static Item.Settings setISTER(Item.Settings properties) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeNetheritePlusModClient.addISTER(properties));
		return properties;
	}
}

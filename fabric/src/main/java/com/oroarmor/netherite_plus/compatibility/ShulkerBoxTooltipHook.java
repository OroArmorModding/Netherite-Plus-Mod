package com.oroarmor.netherite_plus.compatibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.world.item.Item;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.oroarmor.netherite_plus.compatibility.shulkerboxtooltip.NetheriteShulkerBoxPreviewProvider;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

public class ShulkerBoxTooltipHook implements ShulkerBoxTooltipApi {

	@Override
	public String getModId() {
		return "netherite_plus";
	}

	@Override
	public void registerProviders(Map<PreviewProvider, List<Item>> previewProviders) {
		previewProviders.put(new NetheriteShulkerBoxPreviewProvider(), Util.make(new ArrayList<Item>(), items -> {
			items.add(NetheritePlusItems.NETHERITE_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_WHITE_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_ORANGE_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_MAGENTA_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_LIGHT_BLUE_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_YELLOW_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_LIME_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_PINK_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_GRAY_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_LIGHT_GRAY_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_CYAN_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_PURPLE_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_BLUE_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_BROWN_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_GREEN_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_RED_SHULKER_BOX);
			items.add(NetheritePlusItems.NETHERITE_BLACK_SHULKER_BOX);
		}));
	}

}

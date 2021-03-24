/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.compatibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.oroarmor.netherite_plus.compatibility.shulkerboxtooltip.NetheriteShulkerBoxPreviewProvider;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

public class ShulkerBoxTooltipHook implements ShulkerBoxTooltipApi {

	@Override
	public String getModId() {
		return "netherite_plus";
	}

	@Override
	public void registerProviders(Map<PreviewProvider, List<Item>> previewProviders) {
		if(!NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) {
			return;
		}

		previewProviders.put(new NetheriteShulkerBoxPreviewProvider(), Util.make(new ArrayList<>(), items -> {
			items.add(NetheritePlusItems.NETHERITE_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_WHITE_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_ORANGE_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_MAGENTA_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_LIGHT_BLUE_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_YELLOW_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_LIME_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_PINK_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_GRAY_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_LIGHT_GRAY_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_CYAN_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_PURPLE_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_BLUE_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_BROWN_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_GREEN_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_RED_SHULKER_BOX.get());
			items.add(NetheritePlusItems.NETHERITE_BLACK_SHULKER_BOX.get());
		}));
	}

}

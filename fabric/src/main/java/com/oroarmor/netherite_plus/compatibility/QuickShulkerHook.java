package com.oroarmor.netherite_plus.compatibility;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ShulkerBoxMenu;

public class QuickShulkerHook implements RegisterQuickShulker {

	@Override
	public void registerProviders() {
		QuickOpenableRegistry.register(NetheriteShulkerBoxBlock.class, (player, stack) -> player.openMenu(new SimpleMenuProvider((i, playerInventory, playerEntity) -> new ShulkerBoxMenu(i, player.inventory, new ItemStackInventory(stack, 27)), new TranslatableComponent("container.netheriteShulkerBox"))));
	}

}

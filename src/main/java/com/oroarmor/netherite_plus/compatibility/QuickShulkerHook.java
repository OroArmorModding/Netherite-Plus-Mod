package com.oroarmor.netherite_plus.compatibility;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;

import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;

public class QuickShulkerHook implements RegisterQuickShulker {

	@Override
	public void registerProviders() {
		QuickOpenableRegistry.register(NetheriteShulkerBoxBlock.class, (player, stack) -> player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new ShulkerBoxScreenHandler(i, player.inventory, new ItemStackInventory(stack, 27)), new TranslatableText("container.netheriteShulkerBox"))));
	}

}

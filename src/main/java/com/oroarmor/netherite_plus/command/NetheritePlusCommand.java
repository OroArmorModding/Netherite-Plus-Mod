package com.oroarmor.netherite_plus.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.util.config.ConfigItem;
import com.oroarmor.util.config.ConfigItemGroup;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

public class NetheritePlusCommand implements CommandRegistrationCallback {

	@SuppressWarnings("unchecked")
	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("netherite_plus")
				.requires(p -> p.hasPermissionLevel(2)).executes(c -> listConfigGroups(c));

		for (ConfigItemGroup group : NetheritePlusMod.CONFIG.getConfigs()) {
			LiteralArgumentBuilder<ServerCommandSource> configGroupCommand = literal(group.getName())
					.executes((c) -> listConfigGroup(c, group));
			for (ConfigItem<?> item : group.getConfigs()) {
				LiteralArgumentBuilder<ServerCommandSource> configItemCommand = literal(item.getName())
						.executes((c) -> listItem(c, item));

				switch (item.getType()) {
					case BOOLEAN:
						configItemCommand.then(argument("bool", BoolArgumentType.bool())
								.executes(c -> setItemBoolean(c, (ConfigItem<Boolean>) item)));
						break;
					case DOUBLE:
						configItemCommand.then(argument("dbl", DoubleArgumentType.doubleArg())
								.executes(c -> setItemDouble(c, (ConfigItem<Double>) item)));
						break;
					case GROUP:
						break;
					case INTEGER:
						configItemCommand.then(argument("int", IntegerArgumentType.integer())
								.executes(c -> setItemInteger(c, (ConfigItem<Integer>) item)));
						break;
					case STRING:
						configItemCommand.then(argument("string", StringArgumentType.string())
								.executes(c -> setItemString(c, (ConfigItem<String>) item)));
						break;
				}
				configGroupCommand.then(configItemCommand);
			}
			literalArgumentBuilder.then(configGroupCommand);
		}

		dispatcher.register(literalArgumentBuilder);
	}

	private int setItemString(CommandContext<ServerCommandSource> c, ConfigItem<String> item) {
		String result = StringArgumentType.getString(c, "string");

		return setPrintAndSaveConfig(c, item, result);
	}

	private int setItemInteger(CommandContext<ServerCommandSource> c, ConfigItem<Integer> item) {
		int result = IntegerArgumentType.getInteger(c, "int");

		return setPrintAndSaveConfig(c, item, result);
	}

	private <T> int setPrintAndSaveConfig(CommandContext<ServerCommandSource> c, ConfigItem<T> item, T result) {
		item.setValue(result);

		try {
			c.getSource().getPlayer().sendSystemMessage(
					new TranslatableText("command.netherite_plus.might_require_restart"), Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		NetheritePlusMod.CONFIG.saveConfigToFile();

		return 1;
	}

	private int setItemBoolean(CommandContext<ServerCommandSource> c, ConfigItem<Boolean> item) {
		boolean result = BoolArgumentType.getBool(c, "bool");

		return setPrintAndSaveConfig(c, item, result);
	}

	private int setItemDouble(CommandContext<ServerCommandSource> c, ConfigItem<Double> item) {
		double result = DoubleArgumentType.getDouble(c, "dbl");

		return setPrintAndSaveConfig(c, item, result);
	}

	private int listItem(CommandContext<ServerCommandSource> c, ConfigItem<?> item) {
		String configList = I18n.translate(item.getDetails()) + ": " + item.getValue() + "\n";

		configList = configList.substring(0, configList.length() - 1);

		try {
			c.getSource().getPlayer().sendSystemMessage(new LiteralText(configList), Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		return 1;
	}

	private int listConfigGroup(CommandContext<ServerCommandSource> c, ConfigItemGroup group) {
		String configList = "";

		configList += group.getName() + "\n";
		for (ConfigItem<?> item : group.getConfigs()) {
			configList += "  |--> " + I18n.translate(item.getDetails()) + ": " + item.getValue() + "\n";
		}

		configList = configList.substring(0, configList.length() - 1);

		try {
			c.getSource().getPlayer().sendSystemMessage(new LiteralText(configList), Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		return 1;
	}

	private int listConfigGroups(CommandContext<ServerCommandSource> c) {
		String configList = "";

		for (ConfigItemGroup group : NetheritePlusMod.CONFIG.getConfigs()) {
			configList += group.getName() + "\n";
			for (ConfigItem<?> item : group.getConfigs()) {
				configList += "  |--> " + I18n.translate(item.getDetails()) + ": " + item.getValue() + "\n";
			}
		}

		configList = configList.substring(0, configList.length() - 1);

		try {
			c.getSource().getPlayer().sendSystemMessage(new LiteralText(configList), Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		return 1;
	}

}

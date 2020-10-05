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
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.config.ConfigItem;
import com.oroarmor.util.config.ConfigItemGroup;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.HoverEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class NetheritePlusCommand implements CommandRegistrationCallback {

	private MutableText createItemText(ConfigItem<?> item, ConfigItemGroup group) {
		MutableText configList = new LiteralText("");
		boolean atDefault = item.getDefaultValue().equals(item.getValue());
		configList.append(new LiteralText("[" + I18n.translate(item.getDetails()) + "]"));
		configList.append(" : ");
		configList.append(new LiteralText("[" + item.getValue() + "]").formatted(atDefault ? Formatting.GREEN : Formatting.DARK_GREEN).styled(s -> s.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, new LiteralText((atDefault ? "At Default " : "") + "Value: " + (atDefault ? item.getDefaultValue() + ". Click to change value." : item.getValue() + ". Click to reset value.")))).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/netherite_plus " + group.getName() + " " + item.getName() + " " + (atDefault ? "value" : item.getDefaultValue())))));
		return configList;
	}

	private int listConfigGroup(CommandContext<ServerCommandSource> c, ConfigItemGroup group) {
		MutableText configList = new LiteralText("");

		configList.append(new LiteralText(group.getName() + "\n").formatted(Formatting.BOLD));
		for (ConfigItem<?> item : group.getConfigs()) {
			configList.append("  |--> ");
			configList.append(createItemText(item, group));
			configList.append("\n");
		}

		try {
			c.getSource().getPlayer().sendSystemMessage(configList, Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		return 1;
	}

	private int listConfigGroups(CommandContext<ServerCommandSource> c) {

		MutableText configList = new LiteralText("");

		for (ConfigItemGroup group : NetheritePlusMod.CONFIG.getConfigs()) {
			configList.append(new LiteralText(group.getName() + "\n").formatted(Formatting.BOLD));
			for (ConfigItem<?> item : group.getConfigs()) {
				configList.append("  |--> ");
				configList.append(createItemText(item, group));
				if (item != NetheritePlusConfig.GRAPHICS.LAVA_VISION_DISTANCE) {
					configList.append("\n");
				}
			}
		}

		try {
			c.getSource().getPlayer().sendSystemMessage(configList, Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		return 1;
	}

	private int listItem(CommandContext<ServerCommandSource> c, ConfigItem<?> item, ConfigItemGroup group) {
		try {
			c.getSource().getPlayer().sendSystemMessage(createItemText(item, group), Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("netherite_plus").requires(p -> p.hasPermissionLevel(2)).executes(c -> listConfigGroups(c));

		for (ConfigItemGroup group : NetheritePlusMod.CONFIG.getConfigs()) {
			LiteralArgumentBuilder<ServerCommandSource> configGroupCommand = literal(group.getName()).executes((c) -> listConfigGroup(c, group));
			for (ConfigItem<?> item : group.getConfigs()) {
				LiteralArgumentBuilder<ServerCommandSource> configItemCommand = literal(item.getName()).executes((c) -> listItem(c, item, group));

				switch (item.getType()) {
				case BOOLEAN:
					configItemCommand.then(argument("boolean", BoolArgumentType.bool()).executes(c -> setItemBoolean(c, (ConfigItem<Boolean>) item, group)));
					break;
				case DOUBLE:
					configItemCommand.then(argument("double", DoubleArgumentType.doubleArg()).executes(c -> setItemDouble(c, (ConfigItem<Double>) item, group)));
					break;
				case GROUP:
					break;
				case INTEGER:
					configItemCommand.then(argument("int", IntegerArgumentType.integer()).executes(c -> setItemInteger(c, (ConfigItem<Integer>) item, group)));
					break;
				case STRING:
					configItemCommand.then(argument("string", StringArgumentType.string()).executes(c -> setItemString(c, (ConfigItem<String>) item, group)));
					break;
				}
				configGroupCommand.then(configItemCommand);
			}
			literalArgumentBuilder.then(configGroupCommand);
		}

		dispatcher.register(literalArgumentBuilder);
	}

	private int setItemBoolean(CommandContext<ServerCommandSource> c, ConfigItem<Boolean> item, ConfigItemGroup group) {
		boolean result = BoolArgumentType.getBool(c, "boolean");
		return setPrintAndSaveConfig(c, item, result);
	}

	private int setItemDouble(CommandContext<ServerCommandSource> c, ConfigItem<Double> item, ConfigItemGroup group) {
		double result = DoubleArgumentType.getDouble(c, "double");
		return setPrintAndSaveConfig(c, item, result);
	}

	private int setItemInteger(CommandContext<ServerCommandSource> c, ConfigItem<Integer> item, ConfigItemGroup group) {
		int result = IntegerArgumentType.getInteger(c, "int");
		return setPrintAndSaveConfig(c, item, result);
	}

	private int setItemString(CommandContext<ServerCommandSource> c, ConfigItem<String> item, ConfigItemGroup group) {
		String result = StringArgumentType.getString(c, "string");
		return setPrintAndSaveConfig(c, item, result);
	}

	private <T> int setPrintAndSaveConfig(CommandContext<ServerCommandSource> c, ConfigItem<T> item, T result) {
		item.setValue(result);

		try {
			c.getSource().getPlayer().sendSystemMessage(new TranslatableText("command.netherite_plus.might_require_restart").formatted(Formatting.RED, Formatting.ITALIC), Util.NIL_UUID);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		NetheritePlusMod.CONFIG.saveConfigToFile();

		return 1;
	}

}

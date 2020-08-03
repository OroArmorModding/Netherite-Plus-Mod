package com.oroarmor.netherite_plus.compatibility;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.config.ConfigItem;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class ModMenuIntegration implements ModMenuApi {

	@Override
	public String getModId() {
		return "netherite_plus";
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> {
			ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen)
					.setTitle(new LiteralText("Netherite Plus Config"));

			builder.setSavingRunnable(NetheritePlusMod.CONFIG::saveConfigToFile);

			ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

			ConfigCategory enabledFeatures = builder
					.getOrCreateCategory(new LiteralText("Netherite Plus: Enabled Features"));

			ConfigCategory durabilities = builder
					.getOrCreateCategory(new LiteralText("Netherite Plus: Item Durabilities"));

			ConfigCategory damage = builder.getOrCreateCategory(new LiteralText("Netherite Plus: Damage Features"));

			ConfigCategory anvil = builder.getOrCreateCategory(new LiteralText("Netherite Plus: Anvil Features"));

			NetheritePlusConfig.ENABLED.OPTIONS.stream()
					.forEach(ci -> setupBooleanConfigItem((ConfigItem<Boolean>) ci, enabledFeatures, entryBuilder));

			NetheritePlusConfig.DURABILITIES.OPTIONS.stream()
					.forEach(ci -> setupIntegerConfigItem((ConfigItem<Integer>) ci, durabilities, entryBuilder));

			NetheritePlusConfig.DAMAGE.OPTIONS.stream()
					.forEach(ci -> setupDoubleConfigItem((ConfigItem<Double>) ci, damage, entryBuilder));

			NetheritePlusConfig.ANVIL.OPTIONS.stream()
					.forEach(ci -> setupDoubleConfigItem((ConfigItem<Double>) ci, anvil, entryBuilder));

			return builder.build();
		};
	}

	private void setupDoubleConfigItem(ConfigItem<Double> ci, ConfigCategory category,
			ConfigEntryBuilder entryBuilder) {
		category.addEntry(entryBuilder.startDoubleField(new TranslatableText(ci.getDetails()), ci.getValue())
				.setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
	}

	private void setupIntegerConfigItem(ConfigItem<Integer> ci, ConfigCategory category,
			ConfigEntryBuilder entryBuilder) {
		category.addEntry(entryBuilder.startIntField(new TranslatableText(ci.getDetails()), ci.getValue())
				.setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
	}

	private void setupBooleanConfigItem(ConfigItem<Boolean> ci, ConfigCategory category,
			ConfigEntryBuilder entryBuilder) {
		category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText(ci.getDetails()), ci.getValue())
				.setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
	}

}

package com.oroarmor.netherite_plus.compatibility;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.config.ConfigItem;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;

public class ModMenuIntegration implements ModMenuApi {

	private ConfigCategory createCategory(ConfigBuilder builder, String categoryName) {
		return builder.getOrCreateCategory(new TranslatableText(categoryName));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> {
			ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(new TranslatableText("config.netherite_plus"));

			builder.setSavingRunnable(NetheritePlusMod.CONFIG::saveConfigToFile);

			ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

			ConfigCategory enabledFeatures = createCategory(builder, "config.netherite_plus.enabled");
			ConfigCategory durabilities = createCategory(builder, "config.netherite_plus.durabilities");
			ConfigCategory damage = createCategory(builder, "config.netherite_plus.damage");
			ConfigCategory anvil = createCategory(builder, "config.netherite_plus.anvil");
			ConfigCategory graphics = createCategory(builder, "config.netherite_plus.graphics");

			NetheritePlusConfig.ENABLED.OPTIONS.stream().forEach(ci -> setupBooleanConfigItem((ConfigItem<Boolean>) ci, enabledFeatures, entryBuilder));

			NetheritePlusConfig.DURABILITIES.OPTIONS.stream().forEach(ci -> setupIntegerConfigItem((ConfigItem<Integer>) ci, durabilities, entryBuilder));

			NetheritePlusConfig.DAMAGE.OPTIONS.stream().forEach(ci -> setupDoubleConfigItem((ConfigItem<Double>) ci, damage, entryBuilder));

			NetheritePlusConfig.ANVIL.OPTIONS.stream().forEach(ci -> setupDoubleConfigItem((ConfigItem<Double>) ci, anvil, entryBuilder));

			NetheritePlusConfig.GRAPHICS.OPTIONS.stream().forEach(ci -> setupDoubleConfigItem((ConfigItem<Double>) ci, graphics, entryBuilder));

			return builder.build();
		};
	}

	@Override
	public String getModId() {
		return "netherite_plus";
	}

	private void setupBooleanConfigItem(ConfigItem<Boolean> ci, ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText(ci.getDetails()), ci.getValue()).setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
	}

	private void setupDoubleConfigItem(ConfigItem<Double> ci, ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		category.addEntry(entryBuilder.startDoubleField(new TranslatableText(ci.getDetails()), ci.getValue()).setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
	}

	private void setupIntegerConfigItem(ConfigItem<Integer> ci, ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		category.addEntry(entryBuilder.startIntField(new TranslatableText(ci.getDetails()), ci.getValue()).setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
	}

}

package com.oroarmor.netherite_plus.recipe;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import net.minecraft.util.DyeColor;

public class ShulkerBlockDrops {

	public static final String completeFile = "{\r\n" + "  \"type\": \"minecraft:block\",\r\n" + "  \"pools\": [\r\n"
			+ "    {\r\n" + "      \"rolls\": 1,\r\n" + "      \"entries\": [\r\n" + "        {\r\n"
			+ "          \"type\": \"minecraft:item\",\r\n"
			+ "          \"name\": \"netherite_plus:netherite_shulker_box\",\r\n" + "          \"functions\": [\r\n"
			+ "            {\r\n" + "              \"function\": \"minecraft:copy_name\",\r\n"
			+ "              \"source\": \"block_entity\"\r\n" + "            },\r\n" + "            {\r\n"
			+ "              \"function\": \"minecraft:copy_nbt\",\r\n"
			+ "              \"source\": \"block_entity\",\r\n" + "              \"ops\": [\r\n"
			+ "                {\r\n" + "                  \"source\": \"Lock\",\r\n"
			+ "                  \"target\": \"BlockEntityTag.Lock\",\r\n" + "                  \"op\": \"replace\"\r\n"
			+ "                },\r\n" + "                {\r\n" + "                  \"source\": \"LootTable\",\r\n"
			+ "                  \"target\": \"BlockEntityTag.LootTable\",\r\n"
			+ "                  \"op\": \"replace\"\r\n" + "                },\r\n" + "                {\r\n"
			+ "                  \"source\": \"LootTableSeed\",\r\n"
			+ "                  \"target\": \"BlockEntityTag.LootTableSeed\",\r\n"
			+ "                  \"op\": \"replace\"\r\n" + "                }\r\n" + "              ]\r\n"
			+ "            },\r\n" + "            {\r\n" + "              \"function\": \"minecraft:set_contents\",\r\n"
			+ "              \"entries\": [\r\n" + "                {\r\n"
			+ "                  \"type\": \"minecraft:dynamic\",\r\n"
			+ "                  \"name\": \"minecraft:contents\"\r\n" + "                }\r\n" + "              ]\r\n"
			+ "            }\r\n" + "          ]\r\n" + "        }\r\n" + "      ]\r\n" + "    }\r\n" + "  ]\r\n" + "}";

	public static void main(String[] args) {

		Arrays.stream(DyeColor.values()).forEach(ShulkerBlockDrops::genFile);

	}

	public static void genFile(DyeColor c) {

		String thisFile = completeFile.replaceAll("shulker", c.getName() + "_shulker");

		try {
			File f = new File("./src/main/resources/data/netherite_plus/loot_tables/blocks/netherite_" + c.getName()
					+ "_shulker_box.json");

//			f.delete();
			if (f.createNewFile()) {
				System.out.println(f.getAbsolutePath());
				FileOutputStream stream = new FileOutputStream(f);
				stream.write(thisFile.getBytes());
				stream.close();
			}
		} catch (Exception e) {
		}

	}

}

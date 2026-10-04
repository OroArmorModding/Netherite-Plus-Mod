/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.data;

import com.oroarmor.netherite_plus.client.data.NetheritePlusAtlasProvider;
import com.oroarmor.netherite_plus.client.data.models.NetheritePlusEquipmentAssetProvider;
import com.oroarmor.netherite_plus.client.data.models.NetheritePlusModelProvider;
import com.oroarmor.netherite_plus.data.advancements.NetheritePlusAdvancementProvider;
import com.oroarmor.netherite_plus.data.loot.NetheritePlusBlockLootSubProvider;
import com.oroarmor.netherite_plus.data.loot.NetheritePlusFishingLootTableSubProvider;
import com.oroarmor.netherite_plus.data.recipes.NetheritePlusRecipeProvider;
import com.oroarmor.netherite_plus.data.tags.NetheritePlusBlockTagProvider;
import com.oroarmor.netherite_plus.data.tags.NetheritePlusItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class NetheritePlusDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(NetheritePlusAdvancementProvider::new);
        FabricTagsProvider.BlockTagsProvider blockTagProvider = pack.addProvider(NetheritePlusBlockTagProvider::new);
        pack.addProvider((output, registriesFuture) -> new NetheritePlusItemTagProvider(output, registriesFuture, blockTagProvider));
        pack.addProvider(NetheritePlusRecipeProvider::new);
        pack.addProvider(NetheritePlusBlockLootSubProvider::new);
        pack.addProvider(NetheritePlusFishingLootTableSubProvider::new);

        pack.addProvider(NetheritePlusModelProvider::new);
        pack.addProvider(NetheritePlusAtlasProvider::new);
        pack.addProvider(NetheritePlusEquipmentAssetProvider::new);
    }
}

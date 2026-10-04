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

package com.oroarmor.netherite_plus.client.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.oroarmor.netherite_plus.client.renderer.NetheritePlusSheets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.renderer.SpriteMapper;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class NetheritePlusAtlasProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public NetheritePlusAtlasProvider(FabricPackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "atlases");
    }

    private static SpriteSource forMapper(SpriteMapper mapper) {
        return new DirectoryLister(mapper.prefix(), mapper.prefix() + "/");
    }

    private static List<SpriteSource> simpleMapper(final SpriteMapper mapper) {
        return List.of(forMapper(mapper));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.allOf(
                this.storeAtlas(cache, AtlasIds.SHIELD_PATTERNS, simpleMapper(NetheritePlusSheets.NETHERITE_SHIELD_MAPPER)),
                this.storeAtlas(cache, AtlasIds.SHULKER_BOXES, simpleMapper(NetheritePlusSheets.NETHERITE_SHULKER_MAPPER))
        );
    }

    private CompletableFuture<?> storeAtlas(CachedOutput cache, Identifier atlasId, List<SpriteSource> contents) {
        return DataProvider.saveStable(cache, SpriteSources.FILE_CODEC, contents, this.pathProvider.json(atlasId));
    }

    @Override
    public String getName() {
        return "Netherite Plus Atlas Definitions";
    }
}

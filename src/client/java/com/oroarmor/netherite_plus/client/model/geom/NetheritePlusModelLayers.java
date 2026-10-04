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

package com.oroarmor.netherite_plus.client.model.geom;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.client.renderer.special.NetheriteShieldSpecialRenderer;
import com.oroarmor.netherite_plus.client.renderer.special.NetheriteTridentSpecialRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class NetheritePlusModelLayers {
    public static final ModelLayerLocation NETHERITE_SHIELD = new ModelLayerLocation(id("netherite_shield"), "main");
    public static final ModelLayerLocation NETHERITE_TRIDENT = new ModelLayerLocation(id("netherite_trident"), "main");

    public static void init() {
        SpecialModelRenderers.ID_MAPPER.put(id("netherite_shield"), NetheriteShieldSpecialRenderer.Unbaked.MAP_CODEC);
        ModelLayerRegistry.registerModelLayer(NETHERITE_SHIELD, ShieldModel::createLayer);

        SpecialModelRenderers.ID_MAPPER.put(id("netherite_trident"), NetheriteTridentSpecialRenderer.Unbaked.MAP_CODEC);
        ModelLayerRegistry.registerModelLayer(NETHERITE_TRIDENT, TridentModel::createLayer);
    }
}

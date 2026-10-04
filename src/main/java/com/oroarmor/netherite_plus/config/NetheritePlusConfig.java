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

package com.oroarmor.netherite_plus.config;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.IntegerRange;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.SerializedNameConvention;
import folk.sisby.kaleido.lib.quiltconfig.api.metadata.NamingSchemes;

public class NetheritePlusConfig extends WrappedConfig {
    @Comment("Config values for anvils")
    public AnvilConfigs anvil = new AnvilConfigs();
    @Comment("Config values for damage")
    public DamageConfigs damage = new DamageConfigs();

    @Comment("Config values for durability")
    public DurabilityConfigs durability = new DurabilityConfigs();

    @Comment("Config values for graphics")
    public GraphicsConfigs graphics = new GraphicsConfigs();

    public static class AnvilConfigs implements Section {
        @FloatRange(min = 0, max = 1)
        @Comment("The xp reduction percentage.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public float xpReduction = 0.5f;
    }

    public static class DamageConfigs implements Section {
        @FloatRange(min = 0, max = Float.MAX_VALUE)
        @Comment("The bow damage addition over vanilla.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public float bow_damage_addition = 0.0f;

        @FloatRange(min = 0, max = Float.MAX_VALUE)
        @Comment("The bow damage multiplier.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public float bowDamageMultiplier = 1.0f;

        @FloatRange(min = 0, max = Float.MAX_VALUE)
        @Comment("The crossbow damage addition over vanilla.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public float crossbowDamageAddition = 0.0f;

        @FloatRange(min = 0, max = Float.MAX_VALUE)
        @Comment("The crossbow damage multiplier.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public float crossbowDamageMultiplier = 1.0f;

        @FloatRange(min = 0, max = Float.MAX_VALUE)
        @Comment("The trident damage addition over vanilla.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public Double tridentDamageAddition = 0.0;

        @FloatRange(min = 0, max = Float.MAX_VALUE)
        @Comment("The trident damage multiplier.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public Double tridentDamageMultiplier = 1.0;

        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The elytra armor points.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public Integer elytraArmorPoints = 4;

    }

    public static class DurabilityConfigs implements Section {
        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The bow durability points.")
        public Integer bow = 768;

        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The crossbow durability points.")
        public Integer crossbow = 562;

        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The elytra durability points.")
        public Integer elytra = 864;

        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The fishing rod durability points.")
        public Integer fishing_rod = 128;

        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The shield durability points.")
        public Integer shield = 672;

        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The trident durability points.")
        public Integer trident = 500;

        @IntegerRange(min = 0, max = Integer.MAX_VALUE)
        @Comment("The shears durability points.")
        public Integer shears = 476;
    }

    public static class GraphicsConfigs implements Section {
        @FloatRange(min = 0, max = Float.MAX_VALUE)
        @Comment("Distance to see in lava.")
        @SerializedNameConvention(NamingSchemes.SNAKE_CASE)
        public float lavaVisionDistance = 0.25f;
    }
}

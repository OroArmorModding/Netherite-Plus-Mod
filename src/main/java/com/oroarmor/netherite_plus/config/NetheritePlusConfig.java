/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

import org.quiltmc.config.api.WrappedConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;

public final class NetheritePlusConfig extends WrappedConfig {
    @Comment("Config values for anvils")
    public final AnvilConfigs anvil = new AnvilConfigs();

    @Comment("Config values for damage")
    public final DamageConfigs damage = new DamageConfigs();

    @Comment("Config values for durability")
    public final DurabilityConfigs durability = new DurabilityConfigs();

    @Comment("Config values for enabled features")
    public final EnabledConfigs enabled = new EnabledConfigs();

    @Comment("Config values for graphics")
    public final GraphicsConfigs graphics = new GraphicsConfigs();

//    public static void createLavaVisionUpdatePacket(Double configItem) {
//        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
//        passedData.writeDouble(configItem.getValue());
//        NetheritePlusMod.CONNECTED_CLIENTS.forEach(_player -> NetworkManager.sendToPlayer((ServerPlayerEntity) _player, LavaVisionUpdatePacket.ID, passedData));
//    }
//
//    public static void createLavaVisionUpdatePacket(PlayerEntity player) {
//        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
//        passedData.writeDouble(GRAPHICS.LAVA_VISION_DISTANCE.getValue());
//        NetworkManager.sendToPlayer((ServerPlayerEntity) player, LavaVisionUpdatePacket.ID, passedData);
//    }

    public static class AnvilConfigs implements Section {
        @FloatRange(min = 0, max = 1)
        @Comment("The xp reduction percentage.")
        public final double xp_reduction = 0.5;
    }

    public static class DamageConfigs implements Section {
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The bow damage addition over vanilla.")
        public final double bow_damage_addition = 0;
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The bow damage multiplier.")
        public final double bow_damage_multiplier = 1;
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The crossbow damage addition over vanilla.")
        public final double crossbow_damage_addition = 0;
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The crossbow damage multiplier.")
        public final double crossbow_damage_multiplier = 1;
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The trident damage addition over vanilla.")
        public final double trident_damage_addition = 0;
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The trident damage multiplier.")
        public final double trident_damage_multiplier = 1;
        @Comment("The elytra armor points.")
        public final int elytra_armor_points = 4;
    }

    public static class DurabilityConfigs implements Section {
        @Comment("The bow durability points.")
        public final int bow = 768;
        @Comment("The crossbow durability points.")
        public final int crossbow = 562;
        @Comment("The elytra durability points.")
        public final int elytra = 864;
        @Comment("The fishing rod durability points.")
        public final int fishing_rod = 128;
        @Comment("The shield durability points.")
        public final int shield = 672;
        @Comment("The trident durability points.")
        public final int trident = 500;
        @Comment("The shears durability points.")
        public final int shears = 476;
    }

    public static class EnabledConfigs implements Section {
        @Comment("Enables debug print feature.")
        public final boolean config_debug_print = false;
        @Comment("Enables anvil features.")
        public final boolean anvils = true;
        @Comment("Enables bow and crossbow features.")
        public final boolean bows_and_crossbows = true;
        @Comment("Enables elytra features.")
        public final boolean elytra = true;
        @Comment("Enables fake netherite block features.")
        public final boolean fake_netherite_blocks = true;
        @Comment("Enables fishing rod features.")
        public final boolean fishing_rod = true;
        @Comment("Enables horse armor features.")
        public final boolean horse_armor = true;
        @Comment("Enables shields features.")
        public final boolean shields = true;
        @Comment("Enables shulker box features.")
        public final boolean shulker_boxes = true;
        @Comment("Enables trident features.")
        public final boolean trident = true;
        @Comment("Enables beacon features.")
        public final boolean beacon = true;
        @Comment("Enables shears features.")
        public final boolean shears = true;
    }

    public static class GraphicsConfigs implements Section {
        @Comment("Distance to see in lava.")
        public final double lava_vision_distance = 0.25;
    }
}

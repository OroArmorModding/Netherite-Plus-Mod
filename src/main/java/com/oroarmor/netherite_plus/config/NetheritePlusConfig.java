/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
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

import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.WrappedConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;
import org.quiltmc.config.api.values.TrackedValue;

public final class NetheritePlusConfig extends ReflectiveConfig {
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

    public static class AnvilConfigs extends Section {
        @FloatRange(min = 0, max = 1)
        @Comment("The xp reduction percentage.")
        public final TrackedValue<Double> xp_reduction = value(0.5);
    }

    public static class DamageConfigs extends Section {
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The bow damage addition over vanilla.")
        public final TrackedValue<Double> bow_damage_addition = value(0.0);
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The bow damage multiplier.")
        public final TrackedValue<Double> bow_damage_multiplier = value(1.0);
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The crossbow damage addition over vanilla.")
        public final TrackedValue<Double> crossbow_damage_addition = value(0.0);
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The crossbow damage multiplier.")
        public final TrackedValue<Double> crossbow_damage_multiplier = value(1.0);
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The trident damage addition over vanilla.")
        public final TrackedValue<Double> trident_damage_addition = value(0.0);
        @FloatRange(min = 0, max = Double.MAX_VALUE)
        @Comment("The trident damage multiplier.")
        public final TrackedValue<Double> trident_damage_multiplier = value(1.0);
        @Comment("The elytra armor points.")
        public final TrackedValue<Integer> elytra_armor_points = value(4);
    }

    public static class DurabilityConfigs extends Section {
        @Comment("The bow durability points.")
        public final TrackedValue<Integer> bow = value(768);
        @Comment("The crossbow durability points.")
        public final TrackedValue<Integer> crossbow = value(562);
        @Comment("The elytra durability points.")
        public final TrackedValue<Integer> elytra = value(864);
        @Comment("The fishing rod durability points.")
        public final TrackedValue<Integer> fishing_rod = value(128);
        @Comment("The shield durability points.")
        public final TrackedValue<Integer> shield = value(672);
        @Comment("The trident durability points.")
        public final TrackedValue<Integer> trident = value(500);
        @Comment("The shears durability points.")
        public final TrackedValue<Integer> shears = value(476);
    }

    public static class EnabledConfigs extends Section {
        @Comment("Enables debug print feature.")
        public final TrackedValue<Boolean> config_debug_print = value(false);
        @Comment("Enables anvil features.")
        public final TrackedValue<Boolean> anvils = value(true);
        @Comment("Enables bow and crossbow features.")
        public final TrackedValue<Boolean> bows_and_crossbows = value(true);
        @Comment("Enables elytra features.")
        public final TrackedValue<Boolean> elytra = value(true);
        @Comment("Enables fake netherite block features.")
        public final TrackedValue<Boolean> fake_netherite_blocks = value(true);
        @Comment("Enables fishing rod features.")
        public final TrackedValue<Boolean> fishing_rod = value(true);
        @Comment("Enables horse armor features.")
        public final TrackedValue<Boolean> horse_armor = value(true);
        @Comment("Enables shields features.")
        public final TrackedValue<Boolean> shields = value(false);
        @Comment("Enables shulker box features.")
        public final TrackedValue<Boolean> shulker_boxes = value(true);
        @Comment("Enables trident features.")
        public final TrackedValue<Boolean> trident = value(true);
        @Comment("Enables beacon features.")
        public final TrackedValue<Boolean> beacon = value(true);
        @Comment("Enables shears features.")
        public final TrackedValue<Boolean> shears = value(true);
    }

    public static class GraphicsConfigs extends Section {
        @Comment("Distance to see in lava.")
        public final TrackedValue<Double> lava_vision_distance = value(0.25);
    }
}

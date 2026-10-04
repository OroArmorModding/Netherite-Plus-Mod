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

package com.oroarmor.netherite_plus.network.protocol.game;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.effect.MobEffect;

public record ServerboundSetNetheriteBeacon(Optional<Holder<MobEffect>> primary, Optional<Holder<MobEffect>> secondary, Optional<Holder<MobEffect>> tertiary) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundSetNetheriteBeacon> TYPE = new CustomPacketPayload.Type<>(id("set_netherite_beacon"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetNetheriteBeacon> STREAM_CODEC = StreamCodec.composite(
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional), ServerboundSetNetheriteBeacon::primary,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional), ServerboundSetNetheriteBeacon::secondary,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional), ServerboundSetNetheriteBeacon::tertiary,
            ServerboundSetNetheriteBeacon::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

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

package com.oroarmor.netherite_plus.network;

import java.util.Optional;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.BeaconUpdateC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class UpdateNetheriteBeaconC2SPacket extends BeaconUpdateC2SPacket {

    public static final Identifier ID = id("netherite_beacon_update_packet");

    private final Optional<StatusEffect> tertiaryEffect;

    public UpdateNetheriteBeaconC2SPacket(Optional<StatusEffect> primaryEffect, Optional<StatusEffect> secondaryEffect, Optional<StatusEffect> tertiaryEffect) {
        super(primaryEffect, secondaryEffect);
        this.tertiaryEffect = tertiaryEffect;
    }

    public UpdateNetheriteBeaconC2SPacket(PacketByteBuf packetByteBuf) {
        super(packetByteBuf);
        this.tertiaryEffect = packetByteBuf.readOptional(byteBuf -> byteBuf.readFromIterable(Registries.STATUS_EFFECT));
    }

    @Override
    public void write(PacketByteBuf buf) {
        super.write(buf);
        buf.writeOptional(this.tertiaryEffect, (packetByteBuf, statusEffect) -> packetByteBuf.writeFromIterable(Registries.STATUS_EFFECT, statusEffect));
    }

    public Optional<StatusEffect> getTertiaryEffect() {
        return tertiaryEffect;
    }
}

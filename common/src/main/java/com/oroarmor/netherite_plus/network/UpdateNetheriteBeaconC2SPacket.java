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

package com.oroarmor.netherite_plus.network;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class UpdateNetheriteBeaconC2SPacket extends UpdateBeaconC2SPacket {

    public static final Identifier ID = id("netherite_beacon_update_packet");

    private int tertiaryEffectId;

    @Environment(EnvType.CLIENT)
    public UpdateNetheriteBeaconC2SPacket(int primaryEffectId, int secondaryEffectId, int tertiaryEffectId) {
        super(primaryEffectId, secondaryEffectId);
        this.tertiaryEffectId = tertiaryEffectId;
    }

    public UpdateNetheriteBeaconC2SPacket() {
    }

    public UpdateNetheriteBeaconC2SPacket(PacketByteBuf buf) {
        try {
            this.read(buf);
        } catch (IOException e) {
        }
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        super.read(buf);
        tertiaryEffectId = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        super.write(buf);
        buf.writeVarInt(tertiaryEffectId);
    }

    public int getTertiaryEffectId() {
        return tertiaryEffectId;
    }
}

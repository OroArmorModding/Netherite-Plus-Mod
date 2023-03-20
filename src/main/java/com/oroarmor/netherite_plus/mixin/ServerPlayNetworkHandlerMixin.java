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

package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.network.LavaVisionUpdatePacket;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.SharedConstants;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ItemRenameC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        NetheritePlusMod.CONNECTED_CLIENTS.add(player);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(NetheritePlusMod.CONFIG.graphics.lava_vision_distance);
        ServerPlayNetworking.send(player, LavaVisionUpdatePacket.ID, buf);
    }

    @Inject(method = "disconnect", at = @At("RETURN"))
    public void disconnect(Text reason, CallbackInfo info) {
        NetheritePlusMod.CONNECTED_CLIENTS.remove(((ServerPlayNetworkHandler) (Object) this).player);
    }

    @Inject(method = "onItemRename", at = @At("RETURN"))
    public void onRenameItem(ItemRenameC2SPacket packet, CallbackInfo info) {
        if (((ServerPlayNetworkHandler) (Object) this).player.currentScreenHandler instanceof NetheriteAnvilScreenHandler anvilScreenHandler) {
            String string = SharedConstants.stripInvalidCharacters(packet.getName());
            if (string.length() <= 35) {
                anvilScreenHandler.setNewItemName(string);
            }
        }
    }
}

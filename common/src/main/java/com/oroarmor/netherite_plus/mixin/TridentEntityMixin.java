package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;

@Mixin(AbstractArrow.class)
public class TridentEntityMixin  {
    @Inject(method = "getAddEntityPacket()Lnet/minecraft/network/protocol/Packet;", at = @At("HEAD"))
    public void sendTridentStackOnSpawn(CallbackInfoReturnable<Packet<?>> info){
        if((Object) this instanceof ThrownTrident) {
            FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
            passedData.writeInt(Registry.ITEM.getId(((ThrownTrident)(Object)this).tridentItem.getItem()));
            NetworkManager.sendToPlayers(((Entity) (Object) this).level.getServer().getPlayerList().getPlayers(), NetheritePlusMod.id("netherite_trident"), passedData);
        }
    }
}
package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;

@Mixin(PersistentProjectileEntity.class)
public class TridentEntityMixin {
    @SuppressWarnings("cast")
    @Inject(method = "createSpawnPacket", at = @At("HEAD"))
    public void sendTridentStackOnSpawn(CallbackInfoReturnable<Packet<?>> info) {
        if (((Object) this) instanceof TridentEntity) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeInt(Registry.ITEM.getRawId(((TridentEntity) (Object) this).tridentStack.getItem()));
            NetworkManager.sendToPlayers(((Entity) (Object) this).world.getServer().getPlayerManager().getPlayerList(), NetheritePlusMod.id("netherite_trident"), passedData);
        }
    }
}

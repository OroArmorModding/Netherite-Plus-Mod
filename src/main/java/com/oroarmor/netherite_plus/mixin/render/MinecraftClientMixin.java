package com.oroarmor.netherite_plus.mixin.render;

import com.oroarmor.netherite_plus.client.NetheritePlusClientMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;reload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReload;", shift = At.Shift.BEFORE))
    public void init(RunArgs runArgs, CallbackInfo ci) {
        NetheritePlusClientMod.registerBuiltinItemRenderers((MinecraftClient) (Object) this);
    }
}

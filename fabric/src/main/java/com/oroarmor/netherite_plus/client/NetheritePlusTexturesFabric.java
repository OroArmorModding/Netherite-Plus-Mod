package com.oroarmor.netherite_plus.client;

import java.util.Arrays;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.item.DyeColor;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
import static com.oroarmor.netherite_plus.client.NetheritePlusTextures.*;

public class NetheritePlusTexturesFabric {
    public static void register() {
        ClientSpriteRegistryCallback.event(SHULKER_BOXES_ATLAS_TEXTURE).register(NetheritePlusTexturesFabric::registerShulkerBoxTextures);
        ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register(NetheritePlusTexturesFabric::registerShieldTextures);
    }

    public static void registerShieldTextures(TextureAtlas atlas, ClientSpriteRegistryCallback.Registry registry) {
        registry.register(NETHERITE_SHIELD_BASE.texture());
        registry.register(NETHERITE_SHIELD_BASE_NO_PATTERN.texture());
    }

    public static void registerShulkerBoxTextures(TextureAtlas atlas, ClientSpriteRegistryCallback.Registry registry) {
        registry.register(id(makePath(null)));
        Arrays.stream(DyeColor.values()).forEach(c -> registry.register(id(makePath(c))));
    }
}

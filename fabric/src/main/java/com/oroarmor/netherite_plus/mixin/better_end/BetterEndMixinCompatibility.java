package com.oroarmor.netherite_plus.mixin.better_end;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class BetterEndMixinCompatibility implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(!FabricLoader.getInstance().isModLoaded("betterend")) {
            return false;
        }

        if (mixinClassName.equals("com.oroarmor.netherite_plus.mixin.better_end.NetheriteAnvilScreenMixin")) {
            return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return new ArrayList<String>() {{
            if(FabricLoader.getInstance().isModLoaded("betterend")) {
                this.add("better_end.NetheriteAnvilScreenHandlerMixin");
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                    this.add("better_end.NetheriteAnvilScreenMixin");
                }
            }
        }};
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}

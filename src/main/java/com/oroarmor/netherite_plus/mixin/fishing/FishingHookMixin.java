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

package com.oroarmor.netherite_plus.mixin.fishing;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.oroarmor.netherite_plus.loot.NetheritePlusLootManager;
import com.oroarmor.netherite_plus.world.entity.NetheritePlusEntityAttachments;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootTable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
    public FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Unique
    boolean isNetheriteHook() {
        return this.getAttachedOrElse(NetheritePlusEntityAttachments.IS_NETHERITE, false);
    }

    @Definition(id = "is", method = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z")
    @Definition(id = "WATER", field = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;")
    @Expression("?.is(WATER)")
    @WrapOperation(method = {"tick", "getOpenWaterTypeForBlock"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    boolean allowInLava(FluidState instance, TagKey<Fluid> tagKey, Operation<Boolean> original) {
        return original.call(instance, tagKey) || this.isNetheriteHook() && original.call(instance, FluidTags.LAVA);
    }


    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 0.2))
    double provideLavaUpwardForce(double constant) {
        return this.isNetheriteHook() ? 0.4 : constant;
    }

    @Definition(id = "is", method = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z")
    @Definition(id = "FISHING_ROD", field = "Lnet/minecraft/world/item/Items;FISHING_ROD:Lnet/minecraft/world/item/Item;")
    @Expression("?.is(FISHING_ROD)")
    @WrapOperation(method = "shouldStopFishing", at = @At("MIXINEXTRAS:EXPRESSION"))
    boolean allowHoldingNetheriteRod(ItemStack instance, Object item, Operation<Boolean> original) {
        return original.call(instance, item) || this.isNetheriteHook() && original.call(instance, NetheritePlusItems.NETHERITE_FISHING_ROD);
    }

    @Inject(method = "catchingFish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/FishingHook;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    void allowLavaForParticles(BlockPos blockPos, CallbackInfo ci, @Share("isInLava") LocalRef<Boolean> isInLavaRef) {
        isInLavaRef.set(this.isNetheriteHook() && this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA));
    }

    @WrapOperation(method = "catchingFish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    boolean allowLavaForParticles(BlockState instance, Object block, Operation<Boolean> original, @Share("isInLava") LocalRef<Boolean> isInLavaRef) {
        boolean isInLava = this.isNetheriteHook() && original.call(instance, Blocks.LAVA);
        isInLavaRef.set(isInLava);
        return original.call(instance, block) || isInLava;
    }

    @SuppressWarnings("unchecked")
    @ModifyArg(method = "catchingFish", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    <T extends ParticleOptions> T useLavaFishingParticles(T particle, @Share("isInLava") LocalRef<Boolean> isInLavaRef) {
        if (!this.isNetheriteHook() || !isInLavaRef.get()) {
            return particle;
        } else if (particle == ParticleTypes.BUBBLE) {
            return (T) ParticleTypes.LANDING_LAVA;
        } else if (particle == ParticleTypes.FISHING) {
            return (T) ParticleTypes.FLAME;
        } else if (particle == ParticleTypes.SPLASH) {
            return (T) ParticleTypes.SMOKE;
        } else {
            return particle;
        }
    }

    @ModifyArg(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ReloadableServerRegistries$Holder;getLootTable(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/storage/loot/LootTable;"))
    ResourceKey<LootTable> useLavaFishingLootTable(ResourceKey<LootTable> id) {
        if (this.isNetheriteHook()) {
            BlockPos blockPos = this.blockPosition();
            FluidState fluidState = this.level().getFluidState(blockPos);
            FluidState fluidStateBelow = this.level().getFluidState(blockPos.below());

            if (fluidState.is(FluidTags.LAVA) || fluidStateBelow.is(FluidTags.LAVA)) {
                return NetheritePlusLootManager.LAVA_FISHING_LOOT_TABLE;
            }
        }

        return id;
    }

    @ModifyArg(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    Entity makeLavaFishingInvulnerableForHalfASecond(Entity entity) {
        if (this.isNetheriteHook()) {
            // TODO: only for lava fishing?
            entity.setInvulnerableTime(10);
        }
        return entity;
    }
}

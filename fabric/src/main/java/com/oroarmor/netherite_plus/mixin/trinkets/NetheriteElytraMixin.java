package com.oroarmor.netherite_plus.mixin.trinkets;

import java.util.List;
import java.util.Objects;

import com.oroarmor.netherite_plus.compatibility.NetheritePlusTrinketsRenderer;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import dev.emi.trinkets.api.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

@Mixin(NetheriteElytraItem.class)
public class NetheriteElytraMixin extends Item implements Trinket {
    private NetheriteElytraMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Item.Settings settings, CallbackInfo info) {
        DispenserBlock.registerBehavior(this, TrinketItem.TRINKET_DISPENSER_BEHAVIOR);

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (stack.getItem() == this) {
                lines.add(new TranslatableText("warning.netherite_elytra.trinkets"));
            }
        });
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return slot.equals(Slots.CAPE) || group.equals(SlotGroups.CHEST);
    }

    @Override
    public void onEquip(PlayerEntity player, ItemStack stack) {
        Objects.requireNonNull(player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ARMOR)).setBaseValue(NetheritePlusConfig.DAMAGE.ELYTRA_ARMOR_POINTS.getValue() + Objects.requireNonNull(player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ARMOR)).getBaseValue());
    }

    @Override
    public void onUnequip(PlayerEntity player, ItemStack stack) {
        Objects.requireNonNull(player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ARMOR)).setBaseValue(-NetheritePlusConfig.DAMAGE.ELYTRA_ARMOR_POINTS.getValue() + Objects.requireNonNull(player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ARMOR)).getBaseValue());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity livingEntity, float headYaw, float headPitch) {
        NetheritePlusTrinketsRenderer.renderTrinketsElytra(matrixStack, vertexConsumerProvider, light, livingEntity, TrinketsApi.getTrinketComponent(livingEntity).getStack(slot));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        return Trinket.equipTrinket(player, hand);
    }
}

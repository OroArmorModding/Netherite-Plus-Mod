package com.oroarmor.netherite_plus.item;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oroarmor.netherite_plus.compatibility.NetheritePlusTrinketsRenderer;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteElytraItem_Trinkets extends NetheriteElytraItem implements Trinket {

    public NetheriteElytraItem_Trinkets(Properties settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, TrinketItem.TRINKET_DISPENSER_BEHAVIOR);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(new TranslatableComponent("warning.netherite_elytra.trinkets"));
        super.appendHoverText(stack, world, tooltip, context);
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return slot.equals(Slots.CAPE);
    }

    @Override
    public void onEquip(Player player, ItemStack stack) {
        player.getAttributes().getInstance(Attributes.ARMOR).setBaseValue(4 + player.getAttributes().getInstance(Attributes.ARMOR).getBaseValue());
    }

    @Override
    public void onUnequip(Player player, ItemStack stack) {
        player.getAttributes().getInstance(Attributes.ARMOR).setBaseValue(-4 + player.getAttributes().getInstance(Attributes.ARMOR).getBaseValue());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(String slot, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, PlayerModel<AbstractClientPlayer> model, AbstractClientPlayer livingEntity, float headYaw, float headPitch) {

        NetheritePlusTrinketsRenderer.renderTrinketsElytra(matrixStack, vertexConsumerProvider, light, livingEntity, this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        return Trinket.equipTrinket(player, hand);
    }

}

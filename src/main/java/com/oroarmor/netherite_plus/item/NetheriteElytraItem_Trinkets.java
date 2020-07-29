package com.oroarmor.netherite_plus.item;

import java.util.List;

import com.oroarmor.netherite_plus.render.NetheriteElytraFeatureRenderer;
import com.oroarmor.util.item.UniqueItemRegistry;

import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NetheriteElytraItem_Trinkets extends ArmorItem implements Trinket {

	public NetheriteElytraItem_Trinkets(Settings settings) {
		super(NetheriteElytraArmorMaterials.NETHERITE_ELYTRA_MATERIAL, EquipmentSlot.CHEST, settings);
		DispenserBlock.registerBehavior(this, TrinketItem.TRINKET_DISPENSER_BEHAVIOR);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return Trinket.equipTrinket(player, hand);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Items.PHANTOM_MEMBRANE;
	}

	public static boolean isStackUsableAsElytra(ItemStack itemStack) {
		return UniqueItemRegistry.ELYTRA.isItemInRegistry(itemStack.getItem()) && ElytraItem.isUsable(itemStack);
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return slot.equals(Slots.CAPE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final ElytraEntityModel<AbstractClientPlayerEntity> elytra = new ElytraEntityModel();

	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
			PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity livingEntity, float headYaw,
			float headPitch) {

		matrixStack.push();
		matrixStack.scale(1.5f, 1.5f, 1.5f);
		matrixStack.translate(0.0D, -0.775D, 0.125D);
		this.elytra.setAngles(livingEntity, 0f, 0f, 0f, 0f, 0f);
		VertexConsumer vertexConsumer = ItemRenderer.method_29711(vertexConsumerProvider,
				this.elytra.getLayer(NetheriteElytraFeatureRenderer.NETHERITE_ELYTRA_SKIN), false,
				this.getStackForRender().hasGlint());
		this.elytra.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("warning.netherite_elytra.trinkets"));
		super.appendTooltip(stack, world, tooltip, context);
	}

}

package com.oroarmor.netherite_plus.client.render;

import com.oroarmor.netherite_plus.entity.NetheriteTridentEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class NetheriteTridentEntityRenderer extends EntityRenderer<NetheriteTridentEntity> {
   public static final Identifier TEXTURE = new Identifier("netherite_plus","textures/entity/netherite_trident.png");
   private final TridentEntityModel model = new TridentEntityModel();

   public NetheriteTridentEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
      super(entityRenderDispatcher);
   }

   public NetheriteTridentEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, EntityRendererRegistry.Context context) {
      this(entityRenderDispatcher);
   }

   public void render(NetheriteTridentEntity tridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
      matrixStack.push();
      matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, tridentEntity.prevYaw, tridentEntity.yaw) - 90.0F));
      matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, tridentEntity.prevPitch, tridentEntity.pitch) + 90.0F));
      VertexConsumer vertexConsumer = ItemRenderer.getDirectGlintVertexConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(tridentEntity)), false, tridentEntity.isEnchanted());
      this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
      matrixStack.pop();
      super.render(tridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
   }

   public Identifier getTexture( NetheriteTridentEntity tridentEntity) {
      return TEXTURE;
   }
}

package com.oroarmor.netherite_plus.client.render;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NetheriteBeaconBlockEntityRenderer extends BlockEntityRenderer<NetheriteBeaconBlockEntity> {
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

    public NetheriteBeaconBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    @Override
    public void render(NetheriteBeaconBlockEntity beaconBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
        long l = beaconBlockEntity.getLevel().getGameTime();
        List<NetheriteBeaconBlockEntity.BeamSegment> list = beaconBlockEntity.getBeamSegments();
        int k = 0;

        for (int m = 0; m < list.size(); ++m) {
            NetheriteBeaconBlockEntity.BeamSegment beamSegment = list.get(m);
            render(matrixStack, vertexConsumerProvider, f, l, k, m == list.size() - 1 ? 1024 : beamSegment.getHeight(), beamSegment.getColor());
            k += beamSegment.getHeight();
        }

    }

    private static void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, float f, long l, int i, int j, float[] fs) {
        renderLightBeam(matrixStack, vertexConsumerProvider, BEAM_TEXTURE, f, 1.0F, l, i, j, fs, 0.2F, 0.25F);
    }

    public static void renderLightBeam(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, ResourceLocation identifier, float f, float g, long l, int i, int j, float[] fs, float h, float k) {
        int m = i + j;
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.0D, 0.5D);
        float n = Math.floorMod(l, 40L) + f;
        float o = j < 0 ? n : -n;
        float p = Mth.frac(o * 0.2F - Mth.floor(o * 0.1F));
        float q = fs[0];
        float r = fs[1];
        float s = fs[2];
        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(n * 2.25F - 45.0F));
        float af = 0.0F;
        float ai = 0.0F;
        float aj = -h;
        float aa = -h;
        float ap = -1.0F + p;
        float aq = j * g * (0.5F / h) + ap;
        method_22741(matrixStack, vertexConsumerProvider.getBuffer(RenderType.beaconBeam(identifier, false)), q, r, s, 1.0F, i, m, 0.0F, h, h, 0.0F, aj, 0.0F, 0.0F, aa, 0.0F, 1.0F, aq, ap);
        matrixStack.popPose();
        af = -k;
        float ag = -k;
        ai = -k;
        aj = -k;
        ap = -1.0F + p;
        aq = j * g + ap;
        method_22741(matrixStack, vertexConsumerProvider.getBuffer(RenderType.beaconBeam(identifier, true)), q, r, s, 0.125F, i, m, af, ag, k, ai, aj, k, k, k, 0.0F, 1.0F, aq, ap);
        matrixStack.popPose();
    }

    private static void method_22741(PoseStack matrixStack, VertexConsumer vertexConsumer, float f, float g, float h, float i, int j, int k, float l, float m, float n, float o, float p, float q, float r, float s, float t, float u, float v, float w) {
        PoseStack.Pose entry = matrixStack.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, l, m, n, o, t, u, v, w);
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, r, s, p, q, t, u, v, w);
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, n, o, r, s, t, u, v, w);
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, p, q, l, m, t, u, v, w);
    }

    private static void method_22740(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float f, float g, float h, float i, int j, int k, float l, float m, float n, float o, float p, float q, float r, float s) {
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, k, l, m, q, r);
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, l, m, q, s);
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, n, o, p, s);
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, k, n, o, p, r);
    }

    private static void method_23076(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float f, float g, float h, float i, int j, float k, float l, float m, float n) {
        vertexConsumer.vertex(matrix4f, k, j, l).color(f, g, h, i).uv(m, n).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public boolean shouldRenderOffScreen(NetheriteBeaconBlockEntity beaconBlockEntity) {
        return true;
    }
}

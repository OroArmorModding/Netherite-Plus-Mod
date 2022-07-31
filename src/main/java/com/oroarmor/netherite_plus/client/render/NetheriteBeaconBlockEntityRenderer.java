/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.client.render;

import java.util.List;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NetheriteBeaconBlockEntityRenderer implements BlockEntityRenderer<NetheriteBeaconBlockEntity> {
    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    public NetheriteBeaconBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    private static void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, float f, long l, int i, int j, float[] fs) {
        renderLightBeam(matrixStack, vertexConsumerProvider, BEAM_TEXTURE, f, 1.0F, l, i, j, fs, 0.2F, 0.25F);
    }

    public static void renderLightBeam(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Identifier identifier, float f, float g, long l, int i, int j, float[] fs, float h, float k) {
        int m = i + j;
        matrixStack.push();
        matrixStack.translate(0.5D, 0.0D, 0.5D);
        float n = Math.floorMod(l, 40L) + f;
        float o = j < 0 ? n : -n;
        float p = MathHelper.fractionalPart(o * 0.2F - MathHelper.floor(o * 0.1F));
        float q = fs[0];
        float r = fs[1];
        float s = fs[2];
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(n * 2.25F - 45.0F));
        float af = 0.0F;
        float ai = 0.0F;
        float aj = -h;
        float aa = -h;
        float ap = -1.0F + p;
        float aq = j * g * (0.5F / h) + ap;
        method_22741(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(identifier, false)), q, r, s, 1.0F, i, m, 0.0F, h, h, 0.0F, aj, 0.0F, 0.0F, aa, 0.0F, 1.0F, aq, ap);
        matrixStack.pop();
        af = -k;
        float ag = -k;
        ai = -k;
        aj = -k;
        ap = -1.0F + p;
        aq = j * g + ap;
        method_22741(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(identifier, true)), q, r, s, 0.125F, i, m, af, ag, k, ai, aj, k, k, k, 0.0F, 1.0F, aq, ap);
        matrixStack.pop();
    }

    private static void method_22741(MatrixStack matrixStack, VertexConsumer vertexConsumer, float f, float g, float h, float i, int j, int k, float l, float m, float n, float o, float p, float q, float r, float s, float t, float u, float v, float w) {
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPosition();
        Matrix3f matrix3f = entry.getNormal();
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
        vertexConsumer.vertex(matrix4f, k, j, l).color(f, g, h, i).uv(m, n).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
    }

    @Override
    public void render(NetheriteBeaconBlockEntity beaconBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        long l = beaconBlockEntity.getWorld().getTime();
        List<NetheriteBeaconBlockEntity.BeamSegment> list = beaconBlockEntity.getBeamSegments();
        int k = 0;

        for (int m = 0; m < list.size(); ++m) {
            NetheriteBeaconBlockEntity.BeamSegment beamSegment = list.get(m);
            render(matrixStack, vertexConsumerProvider, f, l, k, m == list.size() - 1 ? 1024 : beamSegment.getHeight(), beamSegment.getColor());
            k += beamSegment.getHeight();
        }

    }

    @Override
    public boolean rendersOutsideBoundingBox(NetheriteBeaconBlockEntity blockEntity) {
        return true;
    }
}

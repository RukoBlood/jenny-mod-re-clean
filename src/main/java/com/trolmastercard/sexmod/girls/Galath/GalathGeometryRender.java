/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.util.Arrays;
import java.util.Objects;

import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlRenderer;
import com.trolmastercard.sexmod.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.processor.IBone;

public class GalathGeometryRender {

    public static Vec3d[][] generateAdvancedJointMesh(
            GirlEntity entity,
            float partialTicks,
            String firstPointKey,
            String secondPointKey,
            String thirdPointKey,
            float width1,
            float height1,
            float width2,
            float height2,
            String parentBoneName
    ) {
        Vec3d[] verts = GalathGeometryRender.calculateJointVertices(
                entity,
                partialTicks,
                firstPointKey,
                secondPointKey,
                thirdPointKey,
                width1,
                height1,
                width2,
                height2,
                parentBoneName
        );

        return GalathGeometryRender.constructJointFaces(verts);
    }

    public static Vec3d[][] generateBoxMesh(
            GirlEntity entity,
            float partialTicks,
            String startPointKey, String endPointKey,
            Vector3fSexmodSpecial startOffset, Vector3fSexmodSpecial endOffset
    ) {
        Vec3d[] verts = GalathGeometryRender.CalculateBoxVertices(entity, partialTicks, startPointKey, endPointKey, startOffset, endOffset);
        return GalathGeometryRender.constructCubeFaces(verts);
    }

    static Vec3d[] CalculateBoxVertices(GirlEntity entity, float partialTicks,
                                        String startPointKey, String endPointKey,
                                        Vector3fSexmodSpecial so, Vector3fSexmodSpecial eo) {
        int i;
        Vec3d startPos = entity.getCachedBoneOffset(startPointKey);
        Vec3d endPos = entity.getCachedBoneOffset(endPointKey);
        Vec3d[] v = new Vec3d[8];
        if (so.x == 0.0f && eo.x == 0.0f) {
            v[0] = new Vec3d(0.0, so.y, so.z);
            v[1] = new Vec3d(0.0, -so.y, so.z);
            v[2] = new Vec3d(0.0, -so.y, -so.z);
            v[3] = new Vec3d(0.0, so.y, -so.z);
            v[4] = new Vec3d(0.0, eo.y, eo.z);
            v[5] = new Vec3d(0.0, -eo.y, eo.z);
            v[6] = new Vec3d(0.0, -eo.y, -eo.z);
            v[7] = new Vec3d(0.0, eo.y, -eo.z);
        } else {
            v[0] = new Vec3d(so.x, so.y, 0.0);
            v[1] = new Vec3d(-so.x, so.y, 0.0);
            v[2] = new Vec3d(-so.x, -so.y, 0.0);
            v[3] = new Vec3d(so.x, -so.y, 0.0);
            v[4] = new Vec3d(eo.x, eo.y, 0.0);
            v[5] = new Vec3d(-eo.x, eo.y, 0.0);
            v[6] = new Vec3d(-eo.x, -eo.y, 0.0);
            v[7] = new Vec3d(eo.x, -eo.y, 0.0);
        }
        for (i = 0; i < v.length; ++i) {
            v[i] = VectorMath.rotate(v[i], partialTicks);
        }
        for (i = 0; i < 4; ++i) {
            v[i] = v[i].add(startPos);
        }
        for (i = 4; i < 8; ++i) {
            v[i] = v[i].add(endPos);
        }
        return v;
    }

    static Vec3d[][] constructCubeFaces(Vec3d[] v) {
        Vec3d[][] f = new Vec3d[6][4];
        f[0][0] = v[0];
        f[0][1] = v[1];
        f[0][2] = v[2];
        f[0][3] = v[3];
        f[1][0] = v[4];
        f[1][1] = v[5];
        f[1][2] = v[6];
        f[1][3] = v[7];
        f[2][0] = v[1];
        f[2][1] = v[2];
        f[2][2] = v[6];
        f[2][3] = v[5];
        f[3][0] = v[3];
        f[3][1] = v[7];
        f[3][2] = v[4];
        f[3][3] = v[0];
        f[4][0] = v[1];
        f[4][1] = v[0];
        f[4][2] = v[4];
        f[4][3] = v[5];
        f[5][0] = v[2];
        f[5][1] = v[3];
        f[5][2] = v[7];
        f[5][3] = v[6];
        return f;
    }

    static Vec3d[] calculateJointVertices(GirlEntity entity, float partialTicks,
                                          String firstPointKey, String secondPointKey, String thirdPointKey,
                                          float w1, float h1, float w2, float h2,
                                          String parentBoneName
    ) {
        int i;
        IBone bone = entity.getAnimationProcessor().getBone(parentBoneName);

        if (bone == null) {
            Vec3d[] zeroArray = new Vec3d[12];
            Arrays.fill(zeroArray, Vec3d.ZERO);
            return zeroArray;
        }
        float rotY = TrigMath.toDegrees(bone.getRotationY());
        float rotZ = TrigMath.toDegrees(bone.getRotationZ());
        Vec3d firstPos = entity.getCachedBoneOffset(firstPointKey);
        Vec3d secondPos = entity.getCachedBoneOffset(secondPointKey);
        Vec3d thirdPos = entity.getCachedBoneOffset(thirdPointKey);

        Vec3d[] v = new Vec3d[]{
                new Vec3d(w1, 0.0, -h1), new Vec3d(-w1, 0.0, -h1),
                new Vec3d(-w1, 0.0, h1), new Vec3d(w1, 0.0, h1),
                new Vec3d(w1, h1, 0.0), new Vec3d(-w1, h1, 0.0),
                new Vec3d(-w1, -h1, 0.0), new Vec3d(w1, -h1, 0.0),
                new Vec3d(w2, 0.0, -h2), new Vec3d(-w2, 0.0, -h2),
                new Vec3d(-w2, 0.0, h2), new Vec3d(w2, 0.0, h2)
        };

        for (i = 0; i < v.length; ++i) {
            v[i] = VectorMath.rotate(v[i], partialTicks);
        }
        for (i = 0; i < 4; ++i) {
            v[i] = VectorMath.rotateEuler(v[i], 0.0f, rotY, rotZ);
        }
        for (i = 0; i < 4; ++i) {
            v[i] = v[i].add(firstPos);
        }
        for (i = 4; i < 8; ++i) {
            v[i] = v[i].add(secondPos);
        }
        for (i = 8; i < 12; ++i) {
            v[i] = v[i].add(thirdPos);
        }
        return v;
    }

    static Vec3d[][] constructJointFaces(Vec3d[] v) {
        Vec3d[][] f = new Vec3d[10][4];
        f[0][0] = v[0];
        f[0][1] = v[1];
        f[0][2] = v[5];
        f[0][3] = v[4];
        f[1][0] = v[1];
        f[1][1] = v[2];
        f[1][2] = v[6];
        f[1][3] = v[5];
        f[2][0] = v[3];
        f[2][1] = v[2];
        f[2][2] = v[6];
        f[2][3] = v[7];
        f[3][0] = v[0];
        f[3][1] = v[4];
        f[3][2] = v[7];
        f[3][3] = v[3];
        f[4][0] = v[0];
        f[4][1] = v[1];
        f[4][2] = v[2];
        f[4][3] = v[3];
        f[5][0] = v[4];
        f[5][1] = v[5];
        f[5][2] = v[9];
        f[5][3] = v[8];
        f[6][0] = v[9];
        f[6][1] = v[10];
        f[6][2] = v[6];
        f[6][3] = v[5];
        f[7][0] = v[10];
        f[7][1] = v[11];
        f[7][2] = v[7];
        f[7][3] = v[6];
        f[8][0] = v[4];
        f[8][1] = v[7];
        f[8][2] = v[11];
        f[8][3] = v[8];
        f[9][0] = v[8];
        f[9][1] = v[9];
        f[9][2] = v[10];
        f[9][3] = v[11];
        return f;
    }

    //Optimized and enhanced
    public static void drawMesh(BufferBuilder builder, Vec3d[][] faces, ColorRGBA col) {
        for (Vec3d[] face : faces) {
            for (Vec3d v : face) {
                builder.pos(v.x, v.y, v.z).tex(0.0, 0.0).color(col.r, col.g, col.b, col.a).endVertex();
            }
        }
    }

    public static void setupRenderTranslations(Minecraft mc, GirlEntity girl, float partialTicks) {
        EntityPlayerSP player = mc.player;
        if (player == null) {
            return;
        }

        GlStateManager.translate(0.0, 0.01, 0.0);

        Entity renderBase = ((GirlRenderer<?>) Objects.requireNonNull(mc.getRenderManager().getEntityRenderObject(girl))).resolveTargetEntity(girl);
        Vec3d girlPos = girl.isAnchored() ? girl.getTargetPosition() : Reference.LerpVec3d(new Vec3d(renderBase.lastTickPosX, renderBase.lastTickPosY, renderBase.lastTickPosZ), renderBase.getPositionVector(), (double)partialTicks);
        Vec3d playerPos = Reference.LerpVec3d(new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ), player.getPositionVector(), (double)partialTicks);
        Vec3d translationVec = girlPos.subtract(playerPos);
        translationVec = girl.getInterpolatedRenderPos(translationVec, partialTicks);
        GlStateManager.translate(translationVec.x, translationVec.y, translationVec.z);
    }
}


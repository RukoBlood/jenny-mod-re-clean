/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls.Galath;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import javax.annotation.Nullable;

import com.trolmastercard.sexmod.girls.Galath.GalathCoin.GalathCoinRenderer;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlRenderer;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieModel;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieRenderer;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.util.anim.BoneDeformProcessor;
import com.trolmastercard.sexmod.util.interfaces.IGirlRenderer;
import com.trolmastercard.sexmod.util.interfaces.IGalath;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

//da_class171
public class GalathRenderer extends GirlRenderer<GalathEntity> implements IGirlRenderer {
    final static public int WING_VERTICES_COUNT = 14;
    final static public HashSet<String> BLACKLISTED_BONES = new HashSet<String>(){
        {
            this.add("static");
            this.add("turnable");
            this.add("slip");
            this.add("boobs");
            this.add("booty");
            this.add("vagina");
            this.add("fuckhole");
            this.add("futaBallLR");
            this.add("futaBallLL");
            this.add("coin");
            this.add("pentagram");
        }
    };

    final static public Vector3fSexmodSpecial OVERLAY_COLOR_NONE = new Vector3fSexmodSpecial(0.0f, 0.0f, 0.0f);
    final static ColorRGBA RIBBON_COLOR_PRIMARY = new ColorRGBA(152, 45, 62, 255);
    final static ColorRGBA RIBBON_COLOR_SECONDARY = new ColorRGBA(84, 66, 88, 255);
    final static Rotation2f WING_UV_OFFSET_1 = new Rotation2f(0.25f, 0.125f);
    final static Rotation2f WING_UV_OFFSET_2 = new Rotation2f(0.375f, 0.125f);
    final static float WING_UV_SIZE = 0.125f;
    final static ResourceLocation STAR_TEXTURE = new ResourceLocation("sexmod", "textures/star.png");
    final static int Value_v = 105;
    final static int Value_A = 125;

    final static float HAIR_THICKNESS_START_R = 0.0296875f;
    final static float HAIR_THICKNESS_MID_R = 0.06484375f;
    final static float HAIR_THICKNESS_START_L = 0.026124999f;
    final static float HAIR_THICKNESS_MID_L = 0.0570625f;
    
    final static ProceduralRibbonGenerator.RibbonSettings LICKING_RIBBON_SETTINGS = new ProceduralRibbonGenerator.RibbonSettings(
            RIBBON_COLOR_PRIMARY, 0.1f, 12, 0.035f, (n, f) -> (float)(Math.sin((double)f * 0.3 + -0.2 * (double)n) * 15.0),
            (n, f) -> (float)(Math.sin((double)f * -0.15 + -0.2 * (double)n) * 3.0), (n, f) -> 0.0f, 0.03f, 0.005f);

    final static ProceduralRibbonGenerator.RibbonSettings SITTING_RIBBON_SETTINGS = new ProceduralRibbonGenerator.RibbonSettings(
            RIBBON_COLOR_PRIMARY, 0.0f, 12, 0.0f, (n, f) -> (float)(Math.sin((double)f * 0.3 + -0.2 * (double)n) * 15.0),
            (n, f) -> (float)(Math.sin((double)f * -0.15 + -0.2 * (double)n) * 3.0), (n, f) -> 0.0f, 0.03f, 0.005f);

    boolean isBlacklistInitialized = false;
    float lastRenderYawOffset = 0.0f;

    public GalathRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double shadowSize) {
        super(renderManager, animatedGeoModel, shadowSize);
    }

    //OLD_TODO rename to e
    //formerly a
    @Override
    @Nullable
    protected Vector3fSexmodSpecial getAdditionalOverlayColor(GalathEntity entity) {
        if (entity.world instanceof FakeWorld) {
            return null;
        }
        if (entity.isRenderingOverlayDisabled) {
            return null;
        }
        return OVERLAY_COLOR_NONE;
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        if (!this.isBlacklistInitialized) {
            BLACKLISTED_BONES.addAll(BoneDeformProcessor.EXCLUDED_MESH_BONES);
            BLACKLISTED_BONES.addAll(ManglelieRenderer.B);
            this.isBlacklistInitialized = true;
        }
        return BLACKLISTED_BONES;
    }

    @Override
    protected void drawOverlayLines(Tessellator tessellator, BufferBuilder buffer, GirlEntity girl, Vector3fSexmodSpecial rgb, float thickness) {
        GalathRenderer.drawCustomOverlayBundle(tessellator, buffer, girl, rgb, thickness);
    }

    @Override
    protected void preRenderCallback(GalathEntity entity) {
        float yaw;
        if (entity.getCurrentAction() != Action.MASTERBATE) {
            return;
        }
        entity.rotationYaw = yaw = entity.getYawRotation();
        entity.prevRenderYawOffset = yaw;
        entity.renderYawOffset = yaw;
        entity.prevRotationYawHead = yaw;
        entity.rotationYawHead = yaw;
    }

    @Override
    public void doRender(GalathEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Vec3d dashPosition = GalathRenderer.calculateDashPosition(entity, partialTicks);
        if (dashPosition != null) {
            entity.setTargetPositionDirect(dashPosition);
        }
        entity.targetDashPosition = dashPosition;
        GalathEntity.getAimYaw(entity, partialTicks);

        this.updateRapeChargeYaw(entity);
        this.updateFlyingRenderYaw(entity);

        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        GalathRenderer.renderCustomEffects(entity, partialTicks);
        if (entity.isHuggingManglelie()) {
            ManglelieRenderer.getInterpolatedYaw(entity, partialTicks);
        }
    }

    void updateFlyingRenderYaw(GalathEntity entity) {
        if (entity.getCurrentAction() != Action.RAPE_CHARGE) {
            return;
        }
        entity.prevRenderYawOffset = entity.renderYawOffset = entity.getYawRotation();
    }

    void updateRapeChargeYaw(GalathEntity entity) {
        //boolean bl;
        if (entity.getDataManager().get(GalathEntity.IS_FLYING_FLAG)) {
            Vec3d prevPos = new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
            Vec3d motionVec = entity.getPositionVector().subtract(prevPos);

            boolean isStationary = Math.abs(motionVec.x) + Math.abs(motionVec.z) < (double) 0.05f;
            if (isStationary) {
                entity.renderYawOffset = this.lastRenderYawOffset;
                entity.prevRenderYawOffset = this.lastRenderYawOffset;
            } else {
                float calculatedYaw = (float) (TrigMath.sinDegrees(Math.atan2(motionVec.z, motionVec.x)) - 90.0);
                entity.renderYawOffset = calculatedYaw;
                entity.prevRenderYawOffset = calculatedYaw;
                this.lastRenderYawOffset = calculatedYaw;
            }
        }
    }

    @Nullable
    public static Vec3d calculateDashPosition(GalathEntity entity, float partialTicks) {
        float attackProgress = entity.getSwordAttackProgress();
        if (attackProgress == -1.0f) {
            entity.dashStartWorldTime = -1L;
            entity.dashEndWorldTime = -1L;
            return null;
        }

        EntityLivingBase target = entity.getAttackTarget();
        if (target == null) {
            return null;
        }

        Vec3d targetInterpolatedPos = RotationHelper.LerpVec3d(new Vec3d(target.prevPosX, target.prevPosY, target.prevPosZ), target.getPositionVector(), partialTicks);

        if (attackProgress == 24.0f && entity.dashStartWorldTime == -1L) {
            entity.dashStartWorldTime = GalathRenderer.mc.world.getTotalWorldTime();
            entity.dashEndWorldTime = entity.dashStartWorldTime + 8L;
        }

        if (ThreadNames.isValueInBounds(attackProgress, 24.0, 32.0)) {
            Vec3d backOffset = VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 3.0), entity.getYawRotation() + 180.0f);
            Vec3d anchorPos = entity.getAnchorTargetPosition();
            Vec3d destinationPos = targetInterpolatedPos.add(0.0, target.getEyeHeight(), 0.0).add(backOffset);
            float progress = ((float) GalathRenderer.mc.world.getTotalWorldTime() + partialTicks - (float)entity.dashStartWorldTime) / (float)(entity.dashEndWorldTime - entity.dashStartWorldTime);
            return RotationHelper.LerpVec3d(anchorPos, destinationPos, progress);
        }

        if (ThreadNames.isValueInBounds(attackProgress, 32.0, 54.0)) {
            Vec3d closeOffset = VectorMath.rotateByYaw(new Vec3d(0.0, 0.0, 1.5), entity.getYawRotation() + 180.0f);
            return targetInterpolatedPos.add(closeOffset);
        }

        return null;
    }

    public static void renderCustomEffects(GirlEntity girl, float partialTicks) {
        EntityPlayerSP player = mc.player;
        if (player == null) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GalathGeometryRender.setupRenderTranslations(mc, girl, partialTicks);
        mc.getTextureManager().bindTexture(LINE);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();

        GalathRenderer.renderHairStrands(girl, buffer, tessellator, RotationHelper.LerpFloat(girl.prevRenderYawOffset, girl.renderYawOffset, partialTicks));
        GalathRenderer.renderFloatingStars(girl, buffer, tessellator, partialTicks);
        GalathRenderer.renderWingsMesh(girl, buffer, tessellator);

        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }

    static void renderFloatingStars(GirlEntity girl, BufferBuilder buffer, Tessellator tessellator, float partialTicks) {
        double cos;
        double sin;

        if (!(girl instanceof GalathEntity)) {
            return;
        }
        if (!girl.getDataManager().get(GalathEntity.IS_FLYING_FLAG)) {
            return;
        }
        if (girl.getDataManager().get(GalathEntity.HIDE_EFFECTS_FLAG)) {
            return;
        }

        GlStateManager.pushMatrix();
        Vec3d starOffset = girl.getCachedBoneOffset("stars");
        GlStateManager.translate(starOffset.x, starOffset.y, starOffset.z);

        float time = (float) GalathRenderer.mc.world.getTotalWorldTime() + partialTicks;
        float rotX = (float) (Math.sin(time * 0.2) * 5.0);
        float rotY = (float) (Math.cos(time * 0.2) * 5.0);
        float rotZ = (float) (time * 3.0);

        GlStateManager.rotate(rotX, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(rotZ, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(rotY, 0.0f, 0.0f, 1.0f);

        float angleStep = TrigMath.toRadians(9.0);

        Vector3fSexmodSpecial color = GalathEntity.STAR_PARTICLE_COLOR;
        mc.getTextureManager().bindTexture(LINE);
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.glLineWidth(GalathRenderer.calculateLineThickness(girl, partialTicks, 1.0f, 3.0f));

        float angle = 0.0f;
        while ((double)angle < Math.PI * 2) {
            sin = Math.sin(angle) * (double)0.3f;
            cos = Math.cos(angle) * (double)0.3f;
            buffer.pos(sin, 0.0, cos).tex(0.0, 0.0).color(color.x, color.y, color.z, 1.0f).endVertex();
            angle += angleStep;
        }
        tessellator.draw();

        mc.getTextureManager().bindTexture(STAR_TEXTURE);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        angleStep = TrigMath.toRadians(60.0);
        angle = 0.0f;
        while ((double)angle < Math.PI * 2) {
            sin = Math.sin(angle) * (double)0.3f;
            cos = Math.cos(angle) * (double)0.3f;
            buffer.pos(sin - (double)0.1f, 0.1f, cos).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            buffer.pos(sin + (double)0.1f, 0.1f, cos).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            buffer.pos(sin + (double)0.1f, -0.1f, cos).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            buffer.pos(sin - (double)0.1f, -0.1f, cos).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            angle += angleStep;
        }

        tessellator.draw();
        GlStateManager.popMatrix();
    }

    static void renderHairStrands(GirlEntity girl, BufferBuilder buffer, Tessellator tessellator, float interpolatedYaw) {
        if (girl.getCurrentAction() == Action.GIVE_COIN && Action.GIVE_COIN.ticksPlaying[1] > 100) {
            return;
        }
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        Vec3d[][] rightHairMesh = GalathGeometryRender.generateAdvancedJointMesh(girl, interpolatedYaw,
                "hairStrandStartR", "hairStrandMidR", "hairStrandEndR",
                HAIR_THICKNESS_START_R, HAIR_THICKNESS_MID_R, HAIR_THICKNESS_START_L, HAIR_THICKNESS_MID_L, "head");

        Vec3d[][] vec3dArray2 = GalathGeometryRender.generateAdvancedJointMesh(girl, interpolatedYaw, "hairStrandStartL", "hairStrandMidL", "hairStrandEndL",
                HAIR_THICKNESS_START_R, HAIR_THICKNESS_MID_R, HAIR_THICKNESS_START_L, HAIR_THICKNESS_MID_L, "head");

        GalathGeometryRender.drawMesh(buffer, rightHairMesh, RIBBON_COLOR_SECONDARY);
        GalathGeometryRender.drawMesh(buffer, vec3dArray2, RIBBON_COLOR_SECONDARY);
        tessellator.draw();
    }

    static void renderWingsMesh(GirlEntity girl, BufferBuilder buffer, Tessellator tessellator) {
        if (((IGalath) girl).areWingsAnimated()) {
            mc.getTextureManager().bindTexture(GalathModel.GALATH_TEXTURE);

            Vec3d[] rightWingVertices = new Vec3d[WING_VERTICES_COUNT];
            Vec3d[] leftWingVertices = new Vec3d[WING_VERTICES_COUNT];

            for (int i = 0; i < 14; ++i) {
                rightWingVertices[i] = girl.getCachedBoneOffset("wingRV" + i);
                leftWingVertices[i] = girl.getCachedBoneOffset("wingLV" + i);
            }

            GalathRenderer.renderWingTrianglesAndQuads(buffer, tessellator, rightWingVertices);
            GalathRenderer.renderWingTrianglesAndQuads(buffer, tessellator, leftWingVertices);
        }
    }

    static void renderWingTrianglesAndQuads(BufferBuilder buffer, Tessellator tessellator, Vec3d[] v) {
        buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(v[0].x, v[0].y, v[0].z).tex(GalathRenderer.WING_UV_OFFSET_1.pitch, GalathRenderer.WING_UV_OFFSET_1.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[1].x, v[1].y, v[1].z).tex(GalathRenderer.WING_UV_OFFSET_1.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_1.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[2].x, v[2].y, v[2].z).tex(GalathRenderer.WING_UV_OFFSET_1.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_1.yaw + 0.125f).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[11].x, v[11].y, v[11].z).tex(GalathRenderer.WING_UV_OFFSET_1.pitch, GalathRenderer.WING_UV_OFFSET_1.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[12].x, v[12].y, v[12].z).tex(GalathRenderer.WING_UV_OFFSET_1.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_1.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[13].x, v[13].y, v[13].z).tex(GalathRenderer.WING_UV_OFFSET_1.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_1.yaw + 0.125f).color(255, 255, 255, 255).endVertex();
        tessellator.draw();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(v[3].x, v[3].y, v[3].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch, GalathRenderer.WING_UV_OFFSET_2.yaw + 0.125f).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[4].x, v[4].y, v[4].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch, GalathRenderer.WING_UV_OFFSET_2.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[5].x, v[5].y, v[5].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_2.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[6].x, v[6].y, v[6].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_2.yaw + 0.125f).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[7].x, v[7].y, v[7].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch, GalathRenderer.WING_UV_OFFSET_2.yaw + 0.125f).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[8].x, v[8].y, v[8].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch, GalathRenderer.WING_UV_OFFSET_2.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[9].x, v[9].y, v[9].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_2.yaw).color(255, 255, 255, 255).endVertex();
        buffer.pos(v[10].x, v[10].y, v[10].z).tex(GalathRenderer.WING_UV_OFFSET_2.pitch + 0.125f, GalathRenderer.WING_UV_OFFSET_2.yaw + 0.125f).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
    }

    @Override
    protected void renderModelBuffer(GeoModel model, BufferBuilder buffer, GalathEntity entity, float r, float g, float b, float a, float partialTicks) {
        GeoBone rootBone = model.topLevelBones.get(0);
        GeoBone bodyBone = null;
        GeoBone coinBone = null;
        GeoBone steveBone = null;
        GeoBone body2Bone = null;

        for (GeoBone childBone : rootBone.childBones) {
            switch (childBone.getName()) {
                case "steve": {
                    steveBone = childBone;
                    break;
                }
                case "body": {
                    bodyBone = childBone;
                    break;
                }
                case "coin": {
                    coinBone = childBone;
                    break;
                }
                case "body2": {
                    body2Bone = childBone;
                }
            }
        }

        MATRIX_STACK.push();
        MATRIX_STACK.translate(rootBone);
        MATRIX_STACK.moveToPivot(rootBone);
        MATRIX_STACK.rotate(rootBone);
        MATRIX_STACK.scale(rootBone);
        MATRIX_STACK.moveBackFromPivot(rootBone);

        this.renderRecursively(buffer, bodyBone, r, g, b, a);
        Tessellator.getInstance().draw();

        this.renderCoinBone(buffer, coinBone, entity, partialTicks);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getGoblinTexture(this.renderEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.renderRecursively(buffer, steveBone, r, g, b, this.renderEntity.getRenderScaleFactor());
        Tessellator.getInstance().draw();
        if (body2Bone != null) {
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            Minecraft.getMinecraft().renderEngine.bindTexture(ManglelieModel.TEXTURE_MANGELIE);
            this.renderRecursively(buffer, body2Bone, r, g, b, this.renderEntity.getRenderScaleFactor());
            Tessellator.getInstance().draw();
        }
        MATRIX_STACK.pop();
    }

    @Override
    protected void onBoneProcessing(BufferBuilder buffer, String boneName, GeoBone bone) {
        switch (boneName) {
            case "hairBack": {
                if (mc.isGamePaused()) break;
                IBone headBone = this.renderEntity.getAnimationProcessor().getBone("head");
                float rotX = TrigMath.toDegrees(headBone.getRotationX());
                if (rotX < 0.0f) {
                    bone.setRotationX(TrigMath.wrapDegrees(-rotX));
                    break;
                }
                float factor = Math.min(1.0f, rotX / 45.0f);
                bone.setRotationX(TrigMath.wrapDegrees(-rotX));
                bone.setPositionY(bone.getPositionY() + factor * 1.5f);
                break;
            }
            case "hairDownSideL": {
                //TODO: If it shoud be like that
                if (mc.isGamePaused()) break;
                IBone headBone = this.renderEntity.getAnimationProcessor().getBone("head");
                float rotX = TrigMath.toDegrees(headBone.getRotationX());
                if (rotX < 0.0f) {
                    bone.setRotationX(TrigMath.wrapDegrees(-rotX));
                    break;
                }
                float factor = Math.min(1.0f, rotX / 45.0f);
                bone.setRotationX(TrigMath.wrapDegrees(-rotX));
                bone.setPositionY(bone.getPositionY() + factor * 1.5f);
                break;
            }
            case "hairDownSideR": {
                if (mc.isGamePaused()) break;
                IBone headBone = this.renderEntity.getAnimationProcessor().getBone("head");
                float rotX = TrigMath.toDegrees(headBone.getRotationX());
                if (rotX < 0.0f) {
                    bone.setRotationX(TrigMath.wrapDegrees(-rotX / 2.0f));
                    break;
                }
                float factor = Math.min(1.0f, rotX / 45.0f);
                bone.setRotationX(TrigMath.wrapDegrees(-rotX));
                bone.setPositionY(bone.getPositionY() + factor);
                break;
            }
            case "head": {
                EntityLivingBase entityLivingBase;
                this.updateHeadBlowjobAnimation(bone);
                Action action = this.renderEntity.getCurrentAction();
                if (action != Action.FLY && action != Action.ATTACK_SWORD || (entityLivingBase = this.renderEntity.getAttackTarget()) == null) break;
                float partialTicks = mc.getRenderPartialTicks();

                Vec3d galathPos = RotationHelper.LerpVec3d(new Vec3d(this.renderEntity.lastTickPosX, this.renderEntity.lastTickPosY, this.renderEntity.lastTickPosZ), this.renderEntity.getPositionVector(), partialTicks);
                Vec3d targetPos = RotationHelper.LerpVec3d(new Vec3d(entityLivingBase.lastTickPosX, entityLivingBase.lastTickPosY, entityLivingBase.lastTickPosZ), this.renderEntity.getPositionVector(), partialTicks);
                Vec3d diff = galathPos.subtract(targetPos);

                float rotatedZ = (float) VectorMath.rotateByYaw(diff, this.renderEntity.renderYawOffset).z;
                float pitchAngle = (float)Math.atan2(diff.y, rotatedZ);
                break;
            }
            case "weapon": {
                if (!this.renderEntity.hasSwordEquipped) break;
                GlStateManager.pushMatrix();
                Tessellator.getInstance().draw();
                MatrixHelper.bindOpenGLToBone(MATRIX_STACK, bone);
                GL11.glEnable(GL11.GL_LIGHTING);
                GlStateManager.scale(1.5, 1.0, 2.0);
                GlStateManager.translate(0.0, 0.0, 0.05);
                GlStateManager.rotate(110.0f, 1.0f, 0.0f, 0.0f);
                Minecraft.getMinecraft().getItemRenderer().renderItem(this.renderEntity, new ItemStack(Items.IRON_SWORD), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
                this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                GlStateManager.popMatrix();
                break;
            }
            case "tongue": {
                this.processTongueRibbon(buffer, bone);
                break;
            }
            case "mangTongue": {
                this.processManglelieTongue(buffer, bone);
                break;
            }
            case "head3": {
                this.updateMouthBlowjobAnimation(bone);
                break;
            }
            case "irisL": {}
            case "irisR": {
                this.updateIrisBlowjobOffset(bone);
                break;
            }
            case "irsisFaceR2": 
            case "irsisFaceR3": {
                this.updateEyeBlowjobOffset(bone);
                break;
            }
            case "armL": 
            case "armR": {
                EntityLivingBase target;
                if (this.renderEntity.getCurrentAction() != Action.RAPE_CHARGE || (target = this.renderEntity.getAttackTarget()) == null) break;
                float yaw = this.renderEntity.renderYawOffset;
                Vec3d relTargetPos = target.getPositionVector().subtract(this.renderEntity.getPositionVector());
                relTargetPos = VectorMath.rotateByYaw(relTargetPos, yaw);
                double clampedX = -ThreadNames.clamp(relTargetPos.x, -1.0, 1.0);
                bone.setRotationZ(bone.getRotationZ() + TrigMath.toRadians(45.0 * clampedX));
            }
        }
        if (this.renderEntity.isHuggingManglelie()) {
            ManglelieRenderer.a(this.renderEntity, boneName, bone, true);
        }
    }

    void processTongueRibbon(BufferBuilder buffer, GeoBone bone) {
        if (Action.isAnyAction(this.renderEntity, Action.PUSSY_LICKING, Action.MASTERBATE_SITTING)) {
            this.renderLickingRibbon(buffer, bone);
        } else if (Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW)) {
            this.renderBlowjobRibbon(buffer, bone);
        }
    }

    void processManglelieTongue(BufferBuilder buffer, GeoBone bone) {
        if (!Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW) && !this.renderEntity.isTransformingManglelie) {
            return;
        }
        float progress = this.renderEntity.isTransformingManglelie ? 1.0f - Math.min(0.29f, Action.getActionTickSeconds(this.renderEntity, mc.getRenderPartialTicks())) / 0.29f : 1.0f;
        this.renderAnimatedRibbon(buffer, bone, progress);
        this.bindTexture(ManglelieModel.TEXTURE_MANGELIE);
    }

    void updateMouthBlowjobAnimation(GeoBone bone) {
        if (!Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW, Action.MORNING_BLOWJOB_FAST)) {
            return;
        }
        if (mc.isGamePaused()) {
            return;
        }
        float time = (float) GalathRenderer.mc.player.ticksExisted + mc.getRenderPartialTicks();
        float rotY = (float)(Math.sin(time * 0.1f) * (double)0.1f) + 0.2f;
        float rotZ = (float)Math.sin(time * 0.1f) * 0.1f;
        if (Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW)) {
            bone.setRotationY(bone.getRotationY() + rotY);
            bone.setRotationZ(bone.getRotationZ() + rotZ);
            return;
        }
        if (!this.renderEntity.isTransformingManglelie) {
            return;
        }
        float factor = 1.0f - Math.min(0.5f, Action.getActionTickSeconds(this.renderEntity, mc.getRenderPartialTicks())) / 0.5f;
        bone.setRotationY(bone.getRotationY() + rotY * factor);
        bone.setRotationZ(bone.getRotationZ() + rotZ * factor);
    }

    void updateHeadBlowjobAnimation(GeoBone geoBone) {
        if (!Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW, Action.MORNING_BLOWJOB_FAST)) {
            return;
        }
        if (mc.isGamePaused()) {
            return;
        }
        float time = (float) GalathRenderer.mc.player.ticksExisted + mc.getRenderPartialTicks();
        float rotY = (float)Math.sin(time * -0.1f) * 0.1f;
        float rotZ = (float)Math.sin(time * 0.1f) * 0.1f;
        if (Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW)) {
            geoBone.setRotationY(geoBone.getRotationY() + rotY);
            geoBone.setRotationZ(geoBone.getRotationZ() + rotZ);
            return;
        }
        if (!this.renderEntity.isTransformingManglelie) {
            return;
        }
        float factor = Math.min(0.5f, Action.getActionTickSeconds(this.renderEntity, mc.getRenderPartialTicks())) / 0.5f;
        geoBone.setRotationY(geoBone.getRotationY() + rotY * factor);
        geoBone.setRotationZ(geoBone.getRotationZ() + rotZ * factor);
        //super.a((GeoBone)null);
    }

    // OLD_TODO
    //  this doesnt appear to override anything
    //@Override
    void updateIrisBlowjobOffset(GeoBone geoBone) {
        if (!Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW)) {
            return;
        }
        if (mc.isGamePaused()) {
            return;
        }
        float time = (float) GalathRenderer.mc.player.ticksExisted + mc.getRenderPartialTicks();
        geoBone.setPositionX((float)((double)geoBone.getPositionX() + Math.sin(time * 0.1f) * (double)-0.1f));
    }

    //@Override
    void updateEyeBlowjobOffset(GeoBone geoBone) {
        if (!Action.isAnyAction(this.renderEntity, Action.MORNING_BLOWJOB_SLOW)) {
            return;
        }
        if (mc.isGamePaused()) {
            return;
        }
        float time = (float) GalathRenderer.mc.player.ticksExisted + mc.getRenderPartialTicks();
        geoBone.setPositionX((float)((double)geoBone.getPositionX() + Math.sin(time * 0.1f) * (double)-0.15f));
    }

    void renderAnimatedRibbon(BufferBuilder buffer, GeoBone bone, float alphaFactor) {
        float tickProgress = Action.getActionTimeScale(this.renderEntity, mc.getRenderPartialTicks());
        float lengthStep = alphaFactor * (float)((double)0.02f * ((double)-0.4f * Math.cos(Math.PI * 2 * (double)tickProgress + 1.05) + (double)0.6f));
        ProceduralRibbonGenerator.RibbonSettings settings = new ProceduralRibbonGenerator.RibbonSettings(RIBBON_COLOR_PRIMARY, 0.0f, 12, lengthStep, (n, f3) -> alphaFactor * (float)(Math.cos(Math.PI * 2 * (double)tickProgress + (double)0.35f + (double)(-0.2f * (float)n)) * -10.0), (n, f) -> 0.0f, (n, f3) -> alphaFactor * (float)(Math.cos(Math.PI * 2 * (double)tickProgress + 1.25 + (double)(-0.1f * (float)n)) * -5.0), 0.03f, 0.005f);
        this.renderRibbonAtBone(buffer, bone, settings);
    }

    void renderBlowjobRibbon(BufferBuilder buffer, GeoBone bone) {
        float tickProgress = Action.getActionTimeScale(this.renderEntity, mc.getRenderPartialTicks());
        ProceduralRibbonGenerator.RibbonSettings settings = new ProceduralRibbonGenerator.RibbonSettings(RIBBON_COLOR_PRIMARY, 0.0f, 12, 0.02f, (n, f2) -> (float)(Math.cos(Math.PI * 2 * (double)tickProgress + (double)(-0.2f * (float)n)) * 15.0), (n, f2) -> (float)(Math.cos(Math.PI * 2 * (double)tickProgress + (double)(-0.2f * (float)n)) * 5.0), (n, f) -> 0.0f, 0.03f, 0.005f);
        this.renderRibbonAtBone(buffer, bone, settings);
    }

    void renderLickingRibbon(BufferBuilder buffer, GeoBone bone) {
        float interpolateFactor = this.renderEntity.getSwordAttackProgres(mc.getRenderPartialTicks());
        if (interpolateFactor == 0.0f) {
            this.renderRibbonAtBone(buffer, bone, LICKING_RIBBON_SETTINGS);
            return;
        }
        if (interpolateFactor == 1.0f) {
            this.renderRibbonAtBone(buffer, bone, SITTING_RIBBON_SETTINGS);
            return;
        }
        ProceduralRibbonGenerator.RibbonSettings settings = LICKING_RIBBON_SETTINGS.getSettings();
        settings.LengthStep = RotationHelper.LerpFloat(GalathRenderer.LICKING_RIBBON_SETTINGS.LengthStep, 0.0f, interpolateFactor);
        settings.initialOffset = RotationHelper.LerpFloat(GalathRenderer.LICKING_RIBBON_SETTINGS.initialOffset, 0.0f, interpolateFactor);
        this.renderRibbonAtBone(buffer, bone, settings);
    }

    void renderRibbonAtBone(BufferBuilder buffer, GeoBone bone, ProceduralRibbonGenerator.RibbonSettings settings) {
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        MatrixHelper.bindOpenGLToBone(MATRIX_STACK, bone);
        GlStateManager.disableCull();
        this.bindTexture(LINE);
        ProceduralRibbonGenerator.renderRibbon(buffer, Tessellator.getInstance(), mc, settings);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    void renderCoinBone(BufferBuilder buffer, GeoBone bone, GalathEntity entity, float partialTicks) {
        if (entity.getCurrentAction() != Action.GIVE_COIN) {
            return;
        }

        tempBuffer = buffer;
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);

        if (!this.activeCustomPartBones.contains(bone.getName())) {
            for (GeoCube cube : bone.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.currentRenderingBone = bone;
                this.renderCubeGeometry(buffer, cube, 1.0f, 1.0f, 1.0f, 1.0f, 0.0);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
        }

        Tessellator.getInstance().draw();
        GeoBone childCoinBone = bone.childBones.get(0);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);

        float currentTick = ThreadNames.clamp((float) Action.GIVE_COIN.ticksPlaying[1] + partialTicks, 105.0f, 125.0f);
        float progress = (currentTick - 105.0f) / 20.0f;
        float lightmapCoords = RotationHelper.LerpFloat(120.0f, 240.0f, progress);

        Vector3fSexmodSpecial coinColor = RotationHelper.LerpVector3f(GalathCoinRenderer.COIN_COLOR_DARK, GalathCoinRenderer.COIN_COLOR, progress);

        float lastLightX = OpenGlHelper.lastBrightnessX;
        float lastLightY = OpenGlHelper.lastBrightnessY;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapCoords, lightmapCoords);

        MATRIX_STACK.push();
        MATRIX_STACK.translate(childCoinBone);
        MATRIX_STACK.moveToPivot(childCoinBone);
        MATRIX_STACK.rotate(childCoinBone);
        MATRIX_STACK.scale(childCoinBone);
        MATRIX_STACK.moveBackFromPivot(childCoinBone);

        if (!this.activeCustomPartBones.contains(childCoinBone.getName())) {
            for (GeoCube cube : childCoinBone.childCubes) {
                MATRIX_STACK.push();
                GlStateManager.pushMatrix();
                this.currentRenderingBone = childCoinBone;
                this.renderCubeGeometry(buffer, cube, coinColor.x, coinColor.y, coinColor.z, 1.0f, 0.0);
                GlStateManager.popMatrix();
                MATRIX_STACK.pop();
            }
        }
        MATRIX_STACK.pop();
        MATRIX_STACK.pop();
        Tessellator.getInstance().draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastLightX, lastLightY);
    }

    @Override
    protected Vec3d applyCustomTranslationOffsets(GalathEntity entity, float partialTicks, Vec3d baseVector) {
        if (entity.getCurrentAction() == Action.RUN) {
            float yaw;
            entity.rotationYaw = yaw = entity.getYawRotation();
            entity.prevRenderYawOffset = yaw;
            entity.renderYawOffset = yaw;
            entity.prevRotationYawHead = yaw;
            entity.rotationYawHead = yaw;
        }
        return baseVector;
    }

    //@Override
    //@Nullable
    //protected f7_class292 e(GirlEntity em_class2582) {
    //    return this.a((GalathEntity)em_class2582);
    //}

    //public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GalathEntity)em_class2582, d, d2, d3, f, f2);
    //}

    //@Override
    //protected void b(GirlEntity em_class2582) {
    //    this.b((GalathEntity)em_class2582);
    //}

    //@Override
    //protected Vec3d a(GirlEntity em_class2582, float f, Vec3d vec3d) {
    //    return this.a((GalathEntity)em_class2582, f, vec3d);
    //}

    //@Override
    //protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, GirlEntity em_class2582, float f, float f2, float f3, float f4, float f5) {
    //    this.a(geoModel, bufferBuilder, (GalathEntity)em_class2582, f, f2, f3, f4, f5);
    //}

    //@Override
    //public void doRender(EntityLivingBase entityLivingBase, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GalathEntity)entityLivingBase, d, d2, d3, f, f2);
    //}
//
    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GalathEntity)entity, d, d2, d3, f, f2);
    //}
}

